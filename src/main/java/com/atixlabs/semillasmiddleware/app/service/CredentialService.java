package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.bondarea.repository.LoanRepository;
import com.atixlabs.semillasmiddleware.app.model.DIDHistoric.DIDHisotoric;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefits;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.*;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.repository.*;
import com.atixlabs.semillasmiddleware.app.dto.CredentialDto;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CredentialService {

    private CredentialRepository credentialRepository;
    private CredentialCreditRepository credentialCreditRepository;
    private PersonRepository personRepository;
    private LoanRepository loanRepository;
    private CredentialBenefitsRepository credentialBenefitsRepository;
    private DIDHistoricRepository didHistoricRepository;
    private CredentialStateRepository credentialStateRepository;

    @Autowired
    public CredentialService(CredentialCreditRepository credentialCreditRepository, CredentialRepository credentialRepository, PersonRepository personRepository, LoanRepository loanRepository, CredentialBenefitsRepository credentialBenefitsRepository, DIDHistoricRepository didHistoricRepository, CredentialStateRepository credentialStateRepository) {
        this.credentialCreditRepository = credentialCreditRepository;
        this.credentialRepository = credentialRepository;
        this.personRepository = personRepository;
        this.loanRepository = loanRepository;
        this.credentialBenefitsRepository = credentialBenefitsRepository;
        this.didHistoricRepository = didHistoricRepository;
        this.credentialStateRepository = credentialStateRepository;
    }



    public List<Credential> findCredentials(String credentialType, String name, String dniBeneficiary, String idDidiCredential, String dateOfExpiry, String dateOfIssue, List<String> credentialState) {
        List<Credential> credentials;
        try {
         credentials = credentialRepository.findCredentialsWithFilter(credentialType, name, dniBeneficiary, idDidiCredential, dateOfExpiry, dateOfIssue, credentialState);
        }
        catch (Exception e){
                log.info("There has been an error searching for credentials " + e);
                return Collections.emptyList();
            }
         return  credentials;
    }



    public void createNewCreditCredentials(Loan loan) throws Exception {
        //beneficiarieSSSS -> the credit group will be created by separate (not together)
        Optional<CredentialCredit> opCreditExistence = credentialCreditRepository.findByIdBondareaCredit(loan.getIdBondareaLoan());
        if(opCreditExistence.isEmpty()) {
            Optional<Person> opBeneficiary = personRepository.findByDocumentNumber(loan.getDniPerson());
            if (opBeneficiary.isPresent()) {
                //the documents must coincide
                CredentialCredit credit = this.buildCreditCredential(loan, opBeneficiary.get());
                loan.setHasCredential(true);

                credentialCreditRepository.save(credit);
                loanRepository.save(loan);

                //after create credit, will create benefit credential
                this.createBenefitsCredential(opBeneficiary.get());
            } else {
                throw new Exception("Person should have been created before...");
                //this eror is important, have to be shown in front
            }
        }
        else{
            loan.setHasCredential(true);
            log.error("The credit with idBondarea " + loan.getIdBondareaLoan() + " has an existent credential ");
        }
    }


    private CredentialCredit buildCreditCredential(Loan loan, Person beneficiary) {
        log.info("Creating credit credential");

        CredentialCredit credentialCredit = new CredentialCredit();
        credentialCredit.setIdBondareaCredit(loan.getIdBondareaLoan());
        // TODO we need the type from bondarea - credentialCredit.setCreditType();
        credentialCredit.setIdGroup(loan.getIdGroup());
        credentialCredit.setCurrentCycle(loan.getCycleDescription()); // si cambia, se tomara como cambio de ciclo
        //TODO data for checking - credentialCredit.totalCycles;

        credentialCredit.setAmountExpiredCycles(0);
        credentialCredit.setCreditState(loan.getStatusDescription());
        credentialCredit.setExpiredAmount(loan.getExpiredAmount());
        credentialCredit.setCreationDate(loan.getCreationDate());
        credentialCredit.setDniBeneficiary(beneficiary.getDocumentNumber());

        //Credential Parent fields
        credentialCredit.setDateOfIssue(DateUtil.getLocalDateTimeNow());
        credentialCredit.setBeneficiary(beneficiary);

        //TODO credentialCredit.setIdHistorical();


        //TODO this should be took from DB - credentialCredit.setIdDidiIssuer();
        Optional<DIDHisotoric> opActiveDid = didHistoricRepository.findByIdPersonAndIsActive(beneficiary.getId(), true);
        if (opActiveDid.isPresent()) {
            credentialCredit.setIdDidiReceptor(opActiveDid.get().getIdDidiReceptor());
            Optional<CredentialState> opStateActive = credentialStateRepository.findByStateName(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode());
            if (opStateActive.isPresent()) {
                credentialCredit.setCredentialState(opStateActive.get());
                credentialCredit.setIdDidiCredential(opActiveDid.get().getIdDidiReceptor());
            }
        } else {
            //Person do not have a DID yet -> set as pending didi
            Optional<CredentialState> opStateActive = credentialStateRepository.findByStateName(CredentialStatesCodes.PENDING_DIDI.getCode());
            if (opStateActive.isPresent()) {
                credentialCredit.setCredentialState(opStateActive.get());
            }
        }

            //This depends of the type of loan from bondarea
            credentialCredit.setCredentialDescription("type"); // TODO this column will be no longer useful
            credentialCredit.setCredentialCategory("type ");

            return credentialCredit;

    }

    //now is used only for holders, can be easily changed adding param
    public void createBenefitsCredential(Person beneficiary){
        Optional<CredentialBenefits> opBenefits = credentialBenefitsRepository.findByDniBeneficiary(beneficiary.getDocumentNumber());
        //TODO if TITULAR has a benefit with state REVOCADA ? create new ? or not ?
        //create benefit if person does not have or is holder
            if (opBenefits.isEmpty() || !opBenefits.get().getBeneficiaryType().equals(PersonTypesCodes.HOLDER.getCode()) ) {
                CredentialBenefits benefits = this.buildBenefitsCredential(beneficiary, PersonTypesCodes.HOLDER);
                credentialBenefitsRepository.save(benefits);
            } else {
                log.info("Person with dni" + beneficiary.getDocumentNumber() + "already has a credential benefits");
            }
        }


    /**
     *
     * @param beneficiary
     * @param personType
     * @return CredentialBenefits
     */
    public CredentialBenefits buildBenefitsCredential(Person beneficiary, PersonTypesCodes personType){
        CredentialBenefits benefits = new CredentialBenefits();

        //Person is holder or family
        if(personType.equals(PersonTypesCodes.HOLDER)){
            benefits.setBeneficiaryType(PersonTypesCodes.HOLDER.getCode());
            benefits.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_BENEFITS.getCode());
        }
        else{
            benefits.setBeneficiaryType(PersonTypesCodes.FAMILY.getCode());
            benefits.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_BENEFITS_FAMILY.getCode());
        }

        benefits.setDateOfIssue(DateUtil.getLocalDateTimeNow());
        benefits.setBeneficiary(beneficiary);
        benefits.setDniBeneficiary(beneficiary.getDocumentNumber());
        //TODO credentialCredit.setIdHistorical();



        //TODO this should be took from DB - credentialCredit.setIdDidiIssuer();

        Optional<DIDHisotoric> opActiveDid = didHistoricRepository.findByIdPersonAndIsActive(beneficiary.getId(), true);
        if(opActiveDid.isPresent()) {
            benefits.setIdDidiReceptor(opActiveDid.get().getIdDidiReceptor());
            Optional<CredentialState> opStateActive = credentialStateRepository.findByStateName(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode());
            if(opStateActive.isPresent()) {
                benefits.setCredentialState(opStateActive.get());
                benefits.setIdDidiCredential(opActiveDid.get().getIdDidiReceptor());
            }
        }
        else{
            //Person do not have a DID yet -> set as pending didi
            Optional<CredentialState> opStateActive = credentialStateRepository.findByStateName(CredentialStatesCodes.PENDING_DIDI.getCode());
            if(opStateActive.isPresent())
                 benefits.setCredentialState(opStateActive.get());
        }

        return benefits;
    }




    // ------------------------------------------------MOCKS -----------------------------------------

    public void saveCredentialCreditMock(){
        CredentialCredit credentialCredit = new CredentialCredit();
        //credentialCredit.setDateOfExpiry(LocalDateTime.now());

        credentialCredit.setDateOfIssue(LocalDateTime.now());

        //credentialCredit.setId(1L);//autogenerated
        //credentialCredit.setIdDidiCredential(456L);//null because must be completed by didi
        //credentialCredit.setIdDidiIssuer(123L);//null must be completed by didi
        //credentialCredit.setIdDidiReceptor(234L);//null must be completed by didi
        //credentialCredit.setIdHistorical(77L);//null must be completed by didi
        //credentialCredit.setIdRelatedCredential(534L);//tbd value


        credentialCredit.setIdGroup("1111L");
        credentialCredit.setExpiredAmount((float) 1);
        credentialCredit.setCurrentCycle("Cycle");
        credentialCredit.setCreditState("state");
        credentialCreditRepository.save(credentialCredit);
    }



    public List<CredentialDto> findAllCredentialsMock(){
        List<CredentialDto> credentials = new ArrayList<>();


        CredentialDto credential1 = new CredentialDto(1L,2L,LocalDateTime.now(),LocalDateTime.now().plusDays(1),"Jorge Rodrigues",29302594L,"Estado", "CrdentialCredit");
        credentials.add(credential1);

        CredentialDto credential2 = new CredentialDto(2L,3L,LocalDateTime.now(),LocalDateTime.now().plusDays(1),"Uriel Brama",29302594L,"Estado", "CrdentialIdentity");
        credentials.add(credential2);

        CredentialDto credential3 = new CredentialDto(3L,4L,LocalDateTime.now(),LocalDateTime.now().plusDays(2),"Pepe Grillo",293025464L,"Estado", "CredentialCredit");
        credentials.add(credential3);

        CredentialDto credential4 = new CredentialDto(4L,5L,LocalDateTime.now(),LocalDateTime.now().plusDays(6),"Armando Thompson",29302594L,"Estado", "CredentialDwelling");
        credentials.add(credential4);

        return credentials;

    }

}
