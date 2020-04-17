package com.atixlabs.semillasmiddleware.app.bondarea.service;

import com.atixlabs.semillasmiddleware.app.bondarea.dto.BondareaLoanDto;
import com.atixlabs.semillasmiddleware.app.bondarea.dto.BondareaLoanResponse;
import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.bondarea.repository.LoanRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BondareaService {


    private LoanRepository loanRepository;

    @Autowired
    public BondareaService (LoanRepository loanRepository){
        this.loanRepository = loanRepository;
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

    private BondareaLoanDto getMockBondareaLoan(){
        BondareaLoanDto loan = new BondareaLoanDto();
        loan.setIdBondareaLoan("1L");
        loan.setDni(123456L);
        loan.setStatusName("Activo");
        loan.setAmount((float) 1000);
        loan.setExpiredAmount((float) 0);
        loan.setStatus(55);

        return loan;
    }


    public List<BondareaLoanDto> getMockLoans(String idAccount, String loanState){
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

    public List<BondareaLoanDto> secondLoansDataAllNew(){
        List<BondareaLoanDto> loans = new ArrayList<>();

        //id 1 is deleted

        //loan 2 is the same
        BondareaLoanDto loan2 = getMockBondareaLoan();
        loan2.setIdBondareaLoan("2L");
        loans.add(loan2);

        //loan 3 modified tag
        BondareaLoanDto loan3 = getMockBondareaLoan();
        loan3.setIdBondareaLoan("3L");
        loan3.setTagBondareaLoan("nuevo tag");
        loans.add(loan3);

        //loan 4 is in default
        BondareaLoanDto loan4 = getMockBondareaLoan();
        loan4.setIdBondareaLoan("4L");
        loan4.setExpiredAmount((float) 100);
        loans.add(loan4);

        //new loan
        BondareaLoanDto loan5 = getMockBondareaLoan();
        loan5.setIdBondareaLoan("5L");
        loans.add(loan5);

        return loans;
    }

    public List<BondareaLoanDto> getLoans(String idAccount, String loanState) throws Exception {

        initializeBondareaApi();
        log.info("getLoans:");

        //Fill url params
        loanColumns = "sta|t|id|staInt|id_pg|pg|fOt|fPri|sv|usr|cuentasTag|dni|pp|ppt|m";


        Call<BondareaLoanResponse> callSync = bondareaEndpoint.getLoans("comunidad","wspre", "prerepprestamos", access_key, access_token, idm, /*idAccount,*/ loanColumns, loanState);
        log.info("url request " + callSync.request().url());

        BondareaLoanResponse bondareaLoanResponse;
        try {
            Response<BondareaLoanResponse> response = callSync.execute();
            log.info("el response envio ... " + response.raw().toString());
            log.info("Response getLoans status : " + response.code() + " " + response.message());

            if (response.code() == 200) {
                bondareaLoanResponse = response.body();
                log.info("RESPONSE:");
                log.info("Response body " + response.body());
                log.info("  " + bondareaLoanResponse.toString());

            }
            else
            {
                return Collections.emptyList();
            }
        }catch(JsonSyntaxException  ex){
            log.error(" Bondarea retrieved object does not match with model ", ex);
            throw new Exception("retrived object does not match with model ");
        }
        catch (SocketTimeoutException ex){
                log.error(" Bondarea timeout ", ex);
             throw new Exception("Tiemout, please try again");
            }

        catch(Exception ex){
            log.error("Bondarea error ", ex);
            throw new Exception("Error, please try again");
        }

        return bondareaLoanResponse.getLoans();

    }

    //Second endpoint -> request for one id and get the status


    public void updateExistingLoans(List<Loan> newLoans) {
       // List<Loan> finalLoans = new ArrayList<>();

        List<Loan> existingLoans = loanRepository.findAll();


        List<String> idsExistingLoans = existingLoans.stream().map(Loan::getIdBondareaLoan).collect(Collectors.toList());
        List<String> idsNewLoans = newLoans.stream().map(Loan::getIdBondareaLoan).collect(Collectors.toList());

        //idsExistingLoans now have the ids that no loger exist in the new loan list
        idsExistingLoans.removeAll(idsNewLoans);

        for (Loan existingLoan : existingLoans) {
            //Search for the loan that has been deleated in the new list
            if (idsExistingLoans.contains(existingLoan.getIdBondareaLoan())) {
                //this loan has been deleted on the new list, so this loan needs to be checked (is eliminated or is terminated)
                existingLoan.setPending(true);
                loanRepository.save(existingLoan);
            }
        }

        //update the existing loans

        for (Loan loanToSave : newLoans) {
            //if the newLoan existed previously -> update. Else create.
            Optional<Loan> opLoanToUpdate = existingLoans.stream().filter(previousLoan -> previousLoan.getIdBondareaLoan().equals(loanToSave.getIdBondareaLoan())).findFirst();
            if (opLoanToUpdate.isPresent()) {
                // if(newLoan.getStatus() == 60) { //60 -> finalizado (esto va en la segunda api)
                if (loanToSave.getExpiredAmount() > 0.0) {
                    loanToSave.setIsActive(false);
                }

                    Loan loanToUpdate = opLoanToUpdate.get();
                    loanToUpdate.merge(loanToSave);
                    loanRepository.save(loanToUpdate);

            }
            else {
                loanRepository.save(loanToSave);
            }

        }
        //loanRepository.saveAll(finalLoans);
        // status de alguno es finalizado. En ese caso se pondra como inactivo. Puedo tomar directamente el nuevo loan, cambiarlo ahi y guardar. (Hay que verificar si updatearia el que ya existe en la base en este caso)
        // se hace en api 2 tomando los status pendiente
    }




}
