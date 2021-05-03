package com.atixlabs.semillasmiddleware.app.bondarea.service;

import com.atixlabs.semillasmiddleware.app.bondarea.dto.BondareaLoanDto;
import com.atixlabs.semillasmiddleware.app.bondarea.dto.BondareaLoanResponse;
import com.atixlabs.semillasmiddleware.app.bondarea.exceptions.BondareaSyncroException;
import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.bondarea.model.constants.BondareaLoanStatusCodes;
import com.atixlabs.semillasmiddleware.app.bondarea.model.constants.LoanStateCodes;
import com.atixlabs.semillasmiddleware.app.bondarea.model.constants.LoanStatusCodes;
import com.atixlabs.semillasmiddleware.app.bondarea.repository.LoanRepository;
import com.atixlabs.semillasmiddleware.app.exceptions.InvalidExpiredConfigurationException;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.configuration.ParameterConfiguration;
import com.atixlabs.semillasmiddleware.app.model.configuration.constants.ConfigurationCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.processControl.exception.InvalidProcessException;
import com.atixlabs.semillasmiddleware.app.processControl.model.ProcessControl;
import com.atixlabs.semillasmiddleware.app.processControl.model.constant.ProcessControlStatusCodes;
import com.atixlabs.semillasmiddleware.app.processControl.model.constant.ProcessNamesCodes;
import com.atixlabs.semillasmiddleware.app.processControl.service.ProcessControlService;
import com.atixlabs.semillasmiddleware.app.repository.ParameterConfigurationRepository;
import com.atixlabs.semillasmiddleware.app.repository.PersonRepository;
import com.atixlabs.semillasmiddleware.app.service.CredentialCreditService;
import com.atixlabs.semillasmiddleware.app.service.CredentialService;
import com.atixlabs.semillasmiddleware.util.DateUtil;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import retrofit2.Call;
//import retrofit2.GsonConverterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Slf4j
public class BondareaService {

    private LoanRepository loanRepository;
    private ParameterConfigurationRepository parameterConfigurationRepository;
    private PersonRepository personRepository;
    private ProcessControlService processControlService;
    private LoanService loanService;
    private CredentialService credentialService;

    @Autowired
    public BondareaService(LoanRepository loanRepository, ParameterConfigurationRepository parameterConfigurationRepository, PersonRepository personRepository, ProcessControlService processControlService, LoanService loanService, CredentialService credentialService) {
        this.loanRepository = loanRepository;
        this.parameterConfigurationRepository = parameterConfigurationRepository;
        this.personRepository = personRepository;
        this.processControlService = processControlService;
        this.loanService = loanService;
        this.credentialService = credentialService;
    }

    @Value("${bondarea.base_url}")
    private String serviceURL;

    @Value("${bondarea.access_key}")
    private String access_key;

    @Value("${bondarea.access_token}")
    private String access_token;

    @Value("${bondarea.idm}")
    private String idm;

    // ID de la cuenta representado
    //private String idAccount;

    // Valores separados por "|" (pipe) de las columnas a mostrar para cada registro. (Ej. sta|t|id|staInt|id_pg|pg|fOt|fPri|sv|usr|cuentasTag|dni|pp|ppt|m)
    private String loanColumns;

    // Valores separados por "|" (pipe) de los estados de préstamos a listar.  (Ej. 55|60)
    // private String loanstate;


    //contains json converter and date format configuration
    private Gson gson = null;

    //contains service link with factory
    private Retrofit retrofit = null;

    private BondareaEndpoint bondareaEndpoint = null;


    public void initializeBondareaApi() {
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl(serviceURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        bondareaEndpoint = retrofit.create(BondareaEndpoint.class);


        log.info("initialize bondarea api:");

    }

    /**
     * MOCK PURPOSE
     *
     * @return BpndareaLoanDto
     */
    private BondareaLoanDto getMockBondareaLoan() {
        BondareaLoanDto loan = new BondareaLoanDto();
        loan.setIdBondareaLoan("1L");
        loan.setDni(24580963L); //dni from survey_ok
        loan.setStatusFullDescription("Activo");
        loan.setIdGroup("group1");
        loan.setAmount(BigDecimal.valueOf(1000));
        loan.setExpiredAmount(BigDecimal.valueOf(0));
        loan.setCycle("Ciclo 1");
        loan.setCreationDate("27/04/2020");
        loan.setStatusDescription("");
        loan.setStatus(55);

        return loan;
    }


    /**
     * MOCK PURPOSE:
     * Synchronize new loans mock. It can be used to create loans for the 1st time.
     * Then to test the creation of credit credential and benefits credentials
     *
     * @param loanState
     * @param idBondareaLoan
     * @param date
     * @return
     */
    public List<BondareaLoanDto> getLoansMock(String loanState, String idBondareaLoan, String date) {
        List<BondareaLoanDto> loans = new ArrayList<>();

        BondareaLoanDto loan = getMockBondareaLoan();
        loans.add(loan);

        BondareaLoanDto loan2 = getMockBondareaLoan();
        loan2.setIdBondareaLoan("2L");
        loans.add(loan2);

        BondareaLoanDto loan3 = getMockBondareaLoan();
        loan3.setIdBondareaLoan("3L");
        loans.add(loan3);

        BondareaLoanDto loan4 = getMockBondareaLoan();
        loan4.setIdBondareaLoan("4L");
        loans.add(loan4);

        return loans;
    }

    /**
     * MOCK PURPOSE:
     * Synchronize loans. This is used the 2nd time to update the loans that have already been saved.
     *
     * @param loanState
     * @param idBondareaLoan
     * @param date
     * @return
     */
    public List<BondareaLoanDto> getLoansMockSecond(String loanState, String idBondareaLoan, String date) {
        List<BondareaLoanDto> loans = new ArrayList<>();

        //id 1 is not with state 55

        //id 2 is not with state 55

        //loan 3 modified cycle
        BondareaLoanDto loan3 = getMockBondareaLoan();
        loan3.setIdBondareaLoan("3L");
        loan3.setCycle("Ciclo 2");
        loans.add(loan3);

        //loan 4 is in default
        BondareaLoanDto loan4 = getMockBondareaLoan();
        loan4.setIdBondareaLoan("4L");
        loan4.setExpiredAmount(BigDecimal.valueOf(10500));
        loans.add(loan4);

        //new loan
        BondareaLoanDto loan5 = getMockBondareaLoan();
        loan5.setIdBondareaLoan("5L");
        loans.add(loan5);

        return loans;
    }


    /**
     * MOCK PURPOSE:
     * Synchronize loans. This get the 4 loans but one is in default.
     *
     * @param loanState
     * @param idBondareaLoan
     * @param date
     * @return
     */
    public List<BondareaLoanDto> getLoansOneInDefaultMock(String loanState, String idBondareaLoan, String date) {

        BondareaLoanDto loan = getMockBondareaLoan();
        List<BondareaLoanDto> loans = new ArrayList<>();

        loans.add(loan);

        BondareaLoanDto loan2 = getMockBondareaLoan();
        loan2.setIdBondareaLoan("2L");
        loans.add(loan2);

        BondareaLoanDto loan3 = getMockBondareaLoan();
        loan3.setIdBondareaLoan("3L");
        loans.add(loan3);

        //loan 4 is in default
        BondareaLoanDto loan4 = getMockBondareaLoan();
        loan4.setIdBondareaLoan("4L");
        loan4.setExpiredAmount(BigDecimal.valueOf(10500));
        loans.add(loan4);

        return loans;
    }


    private List<BondareaLoanDto> getLoansFinalizedMock(String code, String idBondareaLoan, String date) {
        List<BondareaLoanDto> loans = new ArrayList<>();
        //loan 2 is finalize
        BondareaLoanDto loan2 = getMockBondareaLoan();
        loan2.setIdBondareaLoan("2L");
        loan2.setStatusDescription("Finalizado");
        loan2.setStatus(60);
        loans.add(loan2);

        return loans;
    }


    /**
     * Productive Version !
     * Bondarea url documentation: https://docs.google.com/document/d/1eb46Rr67EZwolSzNlg4muwHyXEvbHpRldtttWZSnpBU/edit
     *
     * @param loanState
     * @param idBondareaLoan
     * @param date
     * @return List<BondareaLoanDto>
     * @throws Exception
     */
    public List<BondareaLoanDto> getLoans(String loanState, String idBondareaLoan, String date) throws BondareaSyncroException {

        initializeBondareaApi();
        log.info("getBondareaLoans:");

        //Fill url params
        loanColumns = "sta|t|id|staInt|id_pg|pg|fOt|fPri|sv|usr|cuentasTag|dni|pp|ppt|m|tc|nc";


        Call<BondareaLoanResponse> callSync = bondareaEndpoint.getLoans("comunidad", "wspre", "prerepprestamos", access_key, access_token, idm, loanColumns, loanState, idBondareaLoan, date);

        BondareaLoanResponse bondareaLoanResponse;
        try {
            Response<BondareaLoanResponse> response = callSync.execute();

            if (response.code() == HttpStatus.OK.value()) {
                if (idBondareaLoan == null || idBondareaLoan.isEmpty()) {
                    if(response.body() == null || response.body().getLoans() == null || response.body().getLoans().isEmpty()) throw new RuntimeException("No body or loans received in sync, aborting");
                }
                bondareaLoanResponse = response.body();
                log.debug("Bondarea get loans has been successfully executed " + response.body());
            } else {
                log.error("Sync call to get Bonadarea Loan Response was not OK", response.errorBody());
                throw new BondareaSyncroException("Bondarea response was not OK");
            }

        } catch (JsonSyntaxException ex) {
            log.error(" Bondarea retrieved object does not match with model ", ex);
            throw new BondareaSyncroException("retrived object does not match with model ");
        } catch (SocketTimeoutException ex) {
            log.error(" Bondarea timeout ", ex);
            throw new BondareaSyncroException("Tiemout, please try again");
        } catch (Exception ex) {
            log.error("Bondarea error ", ex);
            throw new BondareaSyncroException("Error, please try again");
        }

        return bondareaLoanResponse.getLoans();

    }


    //TODO set as syncronized method

    /**
     * Synchronize loans process from Bondarea
     *
     * @throws InvalidProcessException
     */
    public boolean synchronizeLoans() throws InvalidProcessException {
        //check if this process and credentials are not running
        if (!processControlService.isProcessRunning(ProcessNamesCodes.CREDENTIALS) && !processControlService.isProcessRunning(ProcessNamesCodes.BONDAREA)) {

            ProcessControl process = processControlService.setStatusToProcess(ProcessNamesCodes.BONDAREA, ProcessControlStatusCodes.RUNNING);

            LocalDateTime startTime = process.getStartTime();

            try {

                LocalDate todayPlusOne = DateUtil.getLocalDateWithFormat("dd/MM/yyyy").plusDays(1); //get the loans with the actual day +1
                log.info("BONDAREA - GET LOANS -- from " + todayPlusOne.toString());

                List<BondareaLoanDto> loansDto;

                try {
                    loansDto = this.getLoans(BondareaLoanStatusCodes.ACTIVE.getCode(), "", todayPlusOne.toString());
                    log.info("BONDAREA - GET LOANS -- " + (loansDto!=null ? loansDto.size():0) +" recieved");
                } catch (BondareaSyncroException ex) {
                    log.error("Could not synchronized data from Bondarea ! ", ex);
                    processControlService.setStatusToProcess(ProcessNamesCodes.BONDAREA, ProcessControlStatusCodes.FAIL);
                    return false;
                }

                this.createAndUpdateLoans(loansDto, startTime);
                this.handlePendingLoans(startTime);

                try {
                       this.checkCreditsForDefault();
                } catch (InvalidExpiredConfigurationException ex) {
                    log.error("Error checking defaults loans ",ex);
                    processControlService.setStatusToProcess(ProcessNamesCodes.BONDAREA, ProcessControlStatusCodes.FAIL);
                    return false;
                }

                //finish process
                processControlService.setStatusToProcess(ProcessNamesCodes.BONDAREA, ProcessControlStatusCodes.OK);
                return true;
            } catch (Exception ex) {
                log.error("Exception unknown ", ex);
                processControlService.setStatusToProcess(ProcessNamesCodes.BONDAREA, ProcessControlStatusCodes.FAIL);
                return false;
            }

        } else {
            log.info("Synchronize bondarea can't run ! Process " + ProcessNamesCodes.CREDENTIALS.getCode() + " or " + ProcessNamesCodes.BONDAREA.getCode() + " is still running");
            return false;
        }
    }


    /**
     * Getting the new loans from synchronize, for each loan -> find if exists on DB:
     * if not -> create.
     * if exists -> merge with the new one.
     * 2nd step -> get from DB the ones that has not been modified -> set them to pending.
     *
     * @param newLoans
     * @param startTimeProcess
     */
    public void createAndUpdateLoans(List<BondareaLoanDto> newLoans, LocalDateTime startTimeProcess) {
        //update or create loans
        log.info("Synchronize Bondarea Credits");
        for (BondareaLoanDto loanDtoToSave : newLoans) {

            try {
                //if the newLoan existed previously -> update. Else create.
                Optional<Loan> opLoanToUpdate = loanRepository.findByIdBondareaLoan(loanDtoToSave.getIdBondareaLoan());
                //There is a previous loan
                if (opLoanToUpdate.isPresent()) {
                    //update
                    Loan loanToUpdate = opLoanToUpdate.get();
                    Loan loanToSave = new Loan(loanDtoToSave, this.calculateFeeNumber(loanDtoToSave), this.calculateExpirationDate(loanDtoToSave));

                    log.info("--- loanToSave creation date " + loanToSave.getCreationDate());

                    if (!loanToUpdate.equals(loanToSave)) {
                        log.info("Updating credit " + loanDtoToSave.getIdBondareaLoan());
                        loanToUpdate.merge(loanToSave);
                        loanToUpdate.setUpdateTime(startTimeProcess);
                        loanToUpdate.setSynchroTime(startTimeProcess);
                        loanRepository.save(loanToUpdate);
                    } else {
                        log.info("credit recived with no changes " + loanDtoToSave.getIdBondareaLoan());
                        //update the sync time to know the credit is still active
                        loanToUpdate.setSynchroTime(startTimeProcess);
                        loanRepository.save(loanToUpdate);
                    }
                } else {
                    log.info("new credit " + loanDtoToSave.getIdBondareaLoan());
                    Loan newLoan = new Loan(loanDtoToSave, this.calculateFeeNumber(loanDtoToSave), this.calculateExpirationDate(loanDtoToSave));
                    //set the loan on active
                    newLoan.setStatus(LoanStatusCodes.ACTIVE.getCode());
                    newLoan.setSynchroTime(startTimeProcess);
                    newLoan.setUpdateTime(startTimeProcess);
                    loanRepository.save(newLoan);
                }
            } catch (BondareaSyncroException e){
                log.error("Error al importar credito {} ", loanDtoToSave.getIdBondareaLoan(), e);
                //TODO add alarm
            }
        }
        int modifiedRows = loanRepository.updateStateBySynchroTimeLessThanAndActive(startTimeProcess, LoanStatusCodes.PENDING.getCode(), LoanStatusCodes.ACTIVE.getCode());
        log.info(modifiedRows + " Loans have been updated to pending state");

        log.info("Synchronize Bondarea Credits Ended Successfully");
    }


    /**
     * Determinate for each loan in pending state whether it has been canceled or has finished.
     *
     * @param startTime
     */
    public void handlePendingLoans(LocalDateTime startTime) {

        List<Loan> pendingLoans = loanRepository.findAllByStatus(LoanStatusCodes.PENDING.getCode());

        log.info("Pending Loans to verify " + (pendingLoans != null ? pendingLoans.size() : 0));

        for (Loan pendingLoan : pendingLoans) {

            log.info("Determining the final state for loan : " + pendingLoan.getIdBondareaLoan());

            try {
                List<BondareaLoanDto> loansDto = this.getLoans(BondareaLoanStatusCodes.FINALIZED.getCode(), pendingLoan.getIdBondareaLoan(), "");

                if (!CollectionUtils.isEmpty(loansDto)) {
                    //TODO check default group
                    pendingLoan.setExpiredAmount(loansDto.get(0).getExpiredAmount());
                    pendingLoan.setStatus(LoanStatusCodes.FINALIZED.getCode());
                    log.info("loan has FINALIZED");
                } else {
                    // if there is no loan, is because it has been cancelled
                    pendingLoan.setStatus(LoanStatusCodes.CANCELLED.getCode());
                    log.info("loan was CANCELLED");
                }
                pendingLoan.setUpdateTime(startTime);
                loanRepository.save(pendingLoan);

            } catch (Exception ex) {
                log.error("Error determining pending loans ", ex);
            }
        }
    }

    /**
     * Synchronize MOCK loans process from Bondarea
     *
     * @param bondareaMock
     * @return boolean
     * @throws InvalidProcessException
     */
    public boolean synchronizeMockLoans(List<BondareaLoanDto> bondareaMock) throws InvalidProcessException {
        //check if this process and credentials are not running
        if (!processControlService.isProcessRunning(ProcessNamesCodes.CREDENTIALS) && !processControlService.isProcessRunning(ProcessNamesCodes.BONDAREA)) {

            ProcessControl process = processControlService.setStatusToProcess(ProcessNamesCodes.BONDAREA, ProcessControlStatusCodes.RUNNING);

            LocalDateTime startTime = process.getStartTime();

            try {

                LocalDate todayPlusOne = DateUtil.getLocalDateWithFormat("dd/MM/yyyy").plusDays(1); //get the loans with the actual day +1
                log.info("BONDAREA - GET LOANS -- from " + todayPlusOne.toString());

                List<BondareaLoanDto> loansDto;

                    loansDto = bondareaMock;
                    log.info("BONDAREA - GET LOANS -- " + (loansDto!=null ? loansDto.size():0) +" recieved");

                    log.info("Creatriondate "+loansDto.get(0).getCreationDate());

                this.createAndUpdateLoans(loansDto, startTime);
                this.handlePendingLoans(startTime);

                try {
                    this.checkCreditsForDefault();
                } catch (InvalidExpiredConfigurationException ex) {
                    log.error("Error checking defaults loans ",ex);
                    processControlService.setStatusToProcess(ProcessNamesCodes.BONDAREA, ProcessControlStatusCodes.FAIL);
                    return false;
                }

                //finish process
                processControlService.setStatusToProcess(ProcessNamesCodes.BONDAREA, ProcessControlStatusCodes.OK);
                return true;
            } catch (Exception ex) {
                log.error("Exception unknown ", ex);
                processControlService.setStatusToProcess(ProcessNamesCodes.BONDAREA, ProcessControlStatusCodes.FAIL);
                return false;
            }

        } else {
            log.info("Synchronize bondarea can't run ! Process " + ProcessNamesCodes.CREDENTIALS.getCode() + " or " + ProcessNamesCodes.BONDAREA.getCode() + " is still running");
            return false;
        }
    }

    /**
     * Determinate for each loan in pending state whether it has been canceled or has finished.
     *
     * @param startTime
     */
    public void setPendingLoansFinalStatusMock(LocalDateTime startTime) {
        log.info("Determining the final state of the loans in pending state");
        List<Loan> pendingLoans = loanRepository.findAllByStatus(LoanStatusCodes.PENDING.getCode());

        for (Loan pendingLoan : pendingLoans) {
            try {
                List<BondareaLoanDto> loansDto = this.getLoansFinalizedMock(BondareaLoanStatusCodes.FINALIZED.getCode(), pendingLoan.getIdBondareaLoan(), "");

                if (loansDto.size() > 0) {
                    // if there is a loan will be the one we filtered with the same id and status finalized
                    pendingLoan.setStatus(LoanStatusCodes.FINALIZED.getCode());
                    log.debug("Loan with id bondarea : " + pendingLoan.getIdBondareaLoan() + "is finalized");
                } else {
                    // if there is no loan, is because it has been cancelled
                    pendingLoan.setStatus(LoanStatusCodes.CANCELLED.getCode());
                    log.debug("Loan with id bondarea : " + pendingLoan.getIdBondareaLoan() + "has been cancelled");
                }
                pendingLoan.setUpdateTime(startTime);
                loanRepository.save(pendingLoan);
            } catch (Exception ex) {
                log.error("Error determining pending loans " + ex.getMessage());
            }
        }
    }

    /**
     * Given the modified credits:
     * - check if the group is the group is in default (set each person as defaulter)
     * - check if the group is no longer in default (remove each person of being defaulter)
     *
     * @throws InvalidExpiredConfigurationException
     * @throws InvalidProcessException
     */
    public void checkCreditsForDefault() throws InvalidExpiredConfigurationException, Exception {

        log.info("Checking active credits for defaults");
        if (processControlService.isProcessRunning(ProcessNamesCodes.BONDAREA) && !processControlService.isProcessRunning(ProcessNamesCodes.CHECK_DEFAULTERS)) {

            LocalDateTime lastTimeProcessRan = processControlService.getProcessTimeByProcessCode(ProcessNamesCodes.CHECK_DEFAULTERS);
            try {

                ProcessControl processCheckDefaultersControl = processControlService.setStatusToProcess(ProcessNamesCodes.CHECK_DEFAULTERS, ProcessControlStatusCodes.RUNNING);
                LocalDateTime startProcessTime = processCheckDefaultersControl.getStartTime();

                this.handleDefaultFinalizedLoans(startProcessTime);

                //get the modified loans (actives) -- to check default
                log.info("check modifed loans from "+lastTimeProcessRan);
                List<Loan> modifiedLoans = loanService.findLastLoansModified(lastTimeProcessRan, List.of(LoanStatusCodes.ACTIVE.getCode()));
                modifiedLoans.addAll(loanRepository.findAllByStatusAndState(LoanStatusCodes.FINALIZED.getCode(), LoanStateCodes.DEFAULT.getCode()));
                if (CollectionUtils.isEmpty(modifiedLoans)) {

                    log.info("no active credits to check");
                    processControlService.setStatusToProcess(ProcessNamesCodes.CHECK_DEFAULTERS, ProcessControlStatusCodes.OK);
                    return;
                }
                Optional<ParameterConfiguration> config = parameterConfigurationRepository.findByConfigurationName(ConfigurationCodes.MAX_EXPIRED_AMOUNT.getCode());
                if (config.isPresent()) {

                    this.checkAndUpdateDefaultGroupCredits(modifiedLoans,config.get(),lastTimeProcessRan);
                    //set process finish ok
                    processControlService.setStatusToProcess(ProcessNamesCodes.CHECK_DEFAULTERS, ProcessControlStatusCodes.OK);
                } else {

                    processControlService.setStatusToProcess(ProcessNamesCodes.CHECK_DEFAULTERS, ProcessControlStatusCodes.FAIL);
                    //set the process start time as the one before, to it would check again from that time
                    processControlService.setProcessStartTimeManually(ProcessNamesCodes.CHECK_DEFAULTERS.getCode(), lastTimeProcessRan);
                    throw new InvalidExpiredConfigurationException("There is no configuration for getting the maximum expired amount. Impossible to check the credential credit");
                }
            } catch (Exception ex) {

                //exception unknown
                //set the process start time as the one before, to it would check again from that time
                processControlService.setProcessStartTimeManually(ProcessNamesCodes.CHECK_DEFAULTERS.getCode(), lastTimeProcessRan);
                processControlService.setStatusToProcess(ProcessNamesCodes.CHECK_DEFAULTERS, ProcessControlStatusCodes.FAIL);
                throw ex;
            }
        } else {

            log.info("Check Defaults can't run ! Process " + ProcessNamesCodes.CHECK_DEFAULTERS.getCode() + " is still running or " + ProcessNamesCodes.BONDAREA.getCode() + " is not running");
        }
    }

    /**
     * Update loans finalized and default merge with Bondarea data.
     * @param startProcessTime
     */
    public void handleDefaultFinalizedLoans(LocalDateTime startProcessTime) throws BondareaSyncroException {

        List<Loan> loansFinalizedDefault = loanRepository.findAllByStatusAndState(LoanStatusCodes.FINALIZED.getCode(), LoanStateCodes.DEFAULT.getCode());

        for (Loan loan : loansFinalizedDefault ){

            List<BondareaLoanDto> loansDto = this.getLoans(BondareaLoanStatusCodes.FINALIZED.getCode(), loan.getIdBondareaLoan(), "");
            if (!CollectionUtils.isEmpty(loansDto)) {

                loan.setExpiredAmount(loansDto.get(0).getExpiredAmount());
                loan.setUpdateTime(startProcessTime);
                loanRepository.save(loan);
            }
        }
    }

    /**
     * Check and update the default credits status for each group
     * @param loans
     * @param parameterConfiguration
     * @param updateTime
     */
    public void checkAndUpdateDefaultGroupCredits(List<Loan> loans, ParameterConfiguration parameterConfiguration,
                                                  LocalDateTime updateTime){

        List<String> processedGroupLoans = new ArrayList<>();
        log.info("loans to verify: "+loans.size());
        for (Loan credit : loans) {

            //if the group was not processed..
            if (!processedGroupLoans.contains(credit.getIdGroup())) {
                // get the group active loans to check their expired money
                List<Loan> oneGroup = loanRepository.findAllByIdGroup(credit.getIdGroup());
                BigDecimal amountExpiredOfGroup = sumExpiredAmount(oneGroup);

                BigDecimal maxAmount = new BigDecimal(parameterConfiguration.getValue());
                if (amountExpiredOfGroup.compareTo(maxAmount) >= 0) {
                    //loanset beneficiaries with this credit in default
                    for (Loan loan : oneGroup) {
                        addCreditInDefaultForHolder(loan, updateTime);
                    }
                } else {
                    //credit group is ok.
                    // if this credit has been in default, is needed to delete this one.
                    for (Loan loan : oneGroup) {
                        checkToDeleteCreditInDefault(loan, updateTime);
                    }
                }

                // given the group, delete this group from the actual list so it wont be repeated.
                if (!processedGroupLoans.contains(credit.getIdGroup())) {
                    processedGroupLoans.add(credit.getIdGroup());
                }
            }
        }
    }


    /**
     * Accumulate the expired amount of the credit group.
     * This able to check if the group is default.
     *
     * @param group
     * @return BigDecimal (sum)
     */
    private BigDecimal sumExpiredAmount(List<Loan> group) {
        BigDecimal amountExpired = BigDecimal.ZERO;
        for (Loan credit : group) {
            amountExpired = amountExpired.add(new BigDecimal(credit.getExpiredAmount().toString()));
        }

        return amountExpired;
    }


    private void addCreditInDefaultForHolder(Loan loan, LocalDateTime processStartTime) {
        log.info("Credit with group: " + loan.getIdGroup() + " is in default");
        setDefaultStateToCredit(loan, processStartTime);

        String actualGroup = loan.getIdGroup();

        Optional<Person> opBeneficiary = personRepository.findByDocumentNumber(loan.getDniPerson());
        if (opBeneficiary.isEmpty()) {
            log.info("Person with dni " + loan.getDniPerson() + " has not been loaded in the survey yet");
            return;
        }
        Person beneficiary = opBeneficiary.get();

        //for the beneficiary THE loan will be added to the default list
        List<Loan> defaultList = beneficiary.getDefaults();
        //check if the loan is already set
        if (defaultList.stream().noneMatch(aLoan -> aLoan.getIdGroup().equals(actualGroup))) {
            defaultList.add(loan);
            beneficiary.setDefaults(defaultList);
            personRepository.save(beneficiary);
            log.info("Credit has been saved in holder " + beneficiary.getDocumentNumber() + " as default");
        }
    }


    private void setDefaultStateToCredit(Loan loanInDefault, LocalDateTime processStartTime) {
        if (!loanInDefault.getState().equals(LoanStateCodes.DEFAULT.getCode())) {
            loanInDefault.setState(LoanStateCodes.DEFAULT.getCode());
            //set the update time because the credit state change
            loanInDefault.setUpdateTime(processStartTime);
            log.info("Set credit to DEFAULT for dni " + loanInDefault.getDniPerson());
            loanRepository.save(loanInDefault);
        }
    }


    /**
     * Set credit to OK state if it was not, and remove the credit group from the holder default list
     * @param loan
     * @param processStartTime
     */
    private void checkToDeleteCreditInDefault(Loan loan, LocalDateTime processStartTime) {
        log.info("Checking if necessary to remove credit from default list with group: " + loan.getIdGroup() + " and dni: " + loan.getDniPerson());
        //set the credit as ok if it was not in it.
        setOkStateToCreditGroup(loan, processStartTime);
        String groupId = loan.getIdGroup();

        Optional<Person> opBeneficiary = personRepository.findByDocumentNumber(loan.getDniPerson());

        if (opBeneficiary.isEmpty()) {
            log.info("Person with dni " + loan.getDniPerson() + " has not been loaded in the survey yet");
            return;
        }

        Person holder = opBeneficiary.get();
        //check if the person has the group credit saved as default.
        for (Loan credit : holder.getDefaults()) {
            if (credit.getIdGroup().equals(groupId)) {
                //the credit was in default but now is ok. we take it out.
                holder.getDefaults().remove(credit);
                personRepository.save(holder);
                log.info("Credit for group " + credit.getIdGroup() + " is ok now, removing from default list for holder: " + holder.getDocumentNumber());
                log.info("Remove credit credentials emmited in default for loan id bondarea["+credit.getIdBondareaLoan()+"]");
                credentialService.revokeDefaultCredentialsForLoan(credit);
                credentialService.createNewCreditCredential(credit);
                break;
            }
        }
    }

    private void setOkStateToCreditGroup(Loan loanOK, LocalDateTime processStartTime) {
        if (!loanOK.getState().equals(LoanStateCodes.OK.getCode())) {
            loanOK.setState(LoanStateCodes.OK.getCode());
            //set the update time because the credit state change
            loanOK.setUpdateTime(processStartTime);
            log.info("Credit is now in state OK for dni" + loanOK.getDniPerson());
            loanRepository.save(loanOK);
        }
    }


    /**
     *
     Cuánto dura la cuota (tc): s: semana / m: mes -( ej.: 1_m : 1 mes / 2_s : 2 semanas / 0.5_m : 0.5 meses)

     fecha actual - fpri : Cantidad de días que pasaron.

     Calcular cuántos días dura el ciclo en base al tc.

     Cuota actual = redondear para abajo ( días que pasaron / días que dura el ciclo) +1

     Fecha de primera cuota (fpri).

     Cantidad de cuotas (nc).

     * @param loanDto
     * @return
     */

    public LocalDate calculateExpirationDate(BondareaLoanDto loanDto) throws BondareaSyncroException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate d1 = DateUtil.getLocalDateNow();
        LocalDate d2 = LocalDate.parse(loanDto.getDateFirstInstalment(), formatter);
        long daysPassed = Period.between(d2, d1).getDays();
        LocalDate expirationDate = null;
        try {
            double dayPeriodicity = this.getTcDays(loanDto.getFeeDuration());
            double diff;
            if (daysPassed > dayPeriodicity) {
                diff = daysPassed % dayPeriodicity;
            } else {
                diff = dayPeriodicity - daysPassed;
            }
            expirationDate = d1.plusDays((long) diff);
        } catch (Exception e) {
            log.error("Error on calculateFeeNumber {}", e.getMessage(), e);
            throw new BondareaSyncroException(e.getMessage());
        } finally {
            return expirationDate;
        }
    }

    public Integer calculateFeeNumber(BondareaLoanDto loanDto) throws BondareaSyncroException {

        LocalDate today = DateUtil.getLocalDateNow();

        LocalDate firstFeeDate = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {

            firstFeeDate = LocalDate.parse(loanDto.getDateFirstInstalment(), formatter);
        }catch (Exception e){
            log.error("Error on calculateFeeNumber {}", e.getMessage(), e);
            throw  new BondareaSyncroException(e.getMessage());
        }

        Long daysOfFisrt = firstFeeDate.until(today, DAYS);
        if(daysOfFisrt < 0) return 0;

        Double tcDays = this.getTcDays(loanDto.getFeeDuration());

        if(tcDays==null || tcDays < 1){
            throw  new BondareaSyncroException("Invalid tc number "+tcDays);
        }

        Double currentFee = Math.floor((daysOfFisrt/tcDays));

        return currentFee.intValue() + 1;
    }


    public Double getTcDays(String tc) throws BondareaSyncroException {
        if((tc!=null) && !tc.isEmpty()){
            String[] tcParsed = tc.split("_");
            if(tcParsed.length==2){
                String quantityOfTypes = tcParsed[0];
                String type = tcParsed[1];

                Integer quantityForType = this.getQuantityDayForTypeFee(type.trim().toLowerCase());
                Double quantity = Double.parseDouble(quantityOfTypes);

                Double valueTcDays = quantity*quantityForType;
                return  Math.floor(valueTcDays.doubleValue());

            }
        }

        return  null;
    }

    public Integer getQuantityDayForTypeFee(String type) throws BondareaSyncroException {
        switch (type){
            case "s":
                return 7;
            case "m":
                return 30;
            default:
                throw new BondareaSyncroException(" {} type for tc unknown");

        }
    }

}


