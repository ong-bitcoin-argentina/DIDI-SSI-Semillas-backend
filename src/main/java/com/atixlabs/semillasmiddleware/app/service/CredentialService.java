package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.bondarea.repository.LoanRepository;
import com.atixlabs.semillasmiddleware.app.model.DIDHistoric.DIDHisotoric;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefits;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatusCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialTypesCodes;
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

    @Autowired
    public CredentialService(CredentialCreditRepository credentialCreditRepository, CredentialRepository credentialRepository, PersonRepository personRepository, LoanRepository loanRepository, CredentialBenefitsRepository credentialBenefitsRepository, DIDHistoricRepository didHistoricRepository) {
        this.credentialCreditRepository = credentialCreditRepository;
        this.credentialRepository = credentialRepository;
        this.personRepository = personRepository;
        this.loanRepository = loanRepository;
        this.credentialBenefitsRepository = credentialBenefitsRepository;
        this.didHistoricRepository = didHistoricRepository;
    }



    /*public void buildAllCredentialsFromForm(SurveyForm surveyForm)
    {
        log.info("buildAllCredentialsFromForm: "+this.toString());
        buildPerson(surveyForm);
        buildCreditCredential(surveyForm);
        buildIdentityOwnerCredential(surveyForm);
        buildIdentityRelativeCredential(surveyForm);
        buildEntrepreneurshipCredential(surveyForm);
        buildHomeCredential(surveyForm);
        buildHealthOwnerCredential(surveyForm);
        buildHeathRelativeCredential(surveyForm);
        buildKnowledgeOwnerCredential(surveyForm);
        buildKnowledgeRelativeCredential(surveyForm);
        buildScholarLoanCredential(surveyForm);
        buildCoursesOwnerCredential(surveyForm);
        buildCoursesRelativeCredential(surveyForm);
    }
    */


    public List<Credential> findCredentials(String credentialType, String name, String dniBeneficiary, String idDidiCredential, String dateOfExpiry, String dateOfIssue, List<String> credentialState, String credentialStatus) {
        List<Credential> credentials;
        try {
         credentials = credentialRepository.findCredentialsWithFilter(credentialType, name, dniBeneficiary, idDidiCredential, dateOfExpiry, dateOfIssue, credentialState, credentialStatus);
        }
        catch (Exception e){
                log.info("There has been an error searching for credentials " + e);
                return Collections.emptyList();
            }
         return  credentials;
    }



        //This must be on personService, not here
    /*private void buildPerson(SurveyForm surveyForm){
        log.info("  buildPerson");

        PersonCategory personCategory = (PersonCategory) surveyForm.getCategoryData(PersonCategory.class);
        if(personCategory != null) {
            Person person = new Person(personCategory);

            Optional<Person> personOptional = personRepository.findByDocumentTypeAndDocumentNumber(person.getDocumentType(),person.getDocumentNumber());
            if(personOptional.isEmpty())
                personRepository.save(person);
            else
                log.info("Ya existe una persona con "+personOptional.get().getDocumentType()+" "+personOptional.get().getDocumentNumber());
        }
    }
*/

    public void createNewCreditCredentials(Loan loan) {
        //TODO beneficiarieSSSS -> the credit group will be created by separate
        Optional<CredentialCredit> opCreditExistence = credentialCreditRepository.findByIdBondareaCredit(loan.getIdBondareaLoan());
        if(!opCreditExistence.isPresent()) {
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
                //throw error -> person should have been created before...
                //this eror is important, have to be showed in front
            }
        }
        else{
            loan.setHasCredential(true);
            log.error("The credit with idBondarea " + loan.getIdBondareaLoan() + " has an existent credential ");
            //credit exist
        }
    }


    private CredentialCredit buildCreditCredential(Loan loan, Person beneficiary){
        log.info("Creating credit credential");

        CredentialCredit credentialCredit = new CredentialCredit();
        credentialCredit.setIdBondareaCredit(loan.getIdBondareaLoan());
        credentialCredit.setDniBeneficiary(loan.getDniPerson());
        // TODO we need the type from bondarea - credentialCredit.setCreditType();
        credentialCredit.setIdGroup(loan.getIdGroup());
        credentialCredit.setCurrentCycle(loan.getCycleDescription()); // si cambia, se tomara
        //TODO data for checking - credentialCredit.totalCycles;

        credentialCredit.setAmountExpiredCycles(0); //TODO a credit starts always in cero ?
        credentialCredit.setCreditState(loan.getStatusDescription());
        credentialCredit.setExpiredAmount(loan.getExpiredAmount());
        credentialCredit.setCreationDate(loan.getCreationDate());

        //Credential Parent fields
        credentialCredit.setDateOfIssue(DateUtil.getLocalDateTimeNow()); //Todo check if correct to use loan creation time
        credentialCredit.setBeneficiary(beneficiary);

        //TODO this should be took from DB - credentialCredit.setIdDidiIssuer();
        Optional<DIDHisotoric> opActiveDid = didHistoricRepository.findByIdPersonAndIsActive(beneficiary.getId(), true);
        if(opActiveDid.isPresent()) {
            credentialCredit.setIdDidiReceptor(opActiveDid.get().getIdDidiReceptor());
            credentialCredit.setCredentialState(new CredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode()));
            credentialCredit.setIdDidiCredential(opActiveDid.get().getIdDidiReceptor());
        }
        else{
            //Person do not have a DID yet -> set as pending didi
            credentialCredit.setCredentialStatus(CredentialStatusCodes.CREDENTIAL_PENDING_DIDI.getCode());
        }

        //This depends of the type field from bondarea. And CredentialTypes
        credentialCredit.setCredentialDescription("type");

        return credentialCredit;

    }

    public void createBenefitsCredential(Person beneficiary){
        //TODO validations ?
        Optional<CredentialBenefits> opBenefits = credentialBenefitsRepository.findByDniBeneficiary(beneficiary.getDocumentNumber());
        //create benefit if person does not have or is not titular
            if (!opBenefits.isPresent() || !opBenefits.get().getBeneficiaryType().equals("Titular") ) {
                CredentialBenefits benefits = this.buildBenefitsCredential(beneficiary));
                credentialBenefitsRepository.save(benefits);
            } else {
                //throw error -> person should have been created before...
            }
        }


    }

    /**
     * Build for Titular persons, process into credential credit creation (could be parametrized eventually)
     * @param beneficiary
     * @return
     */
    public CredentialBenefits buildBenefitsCredential(Person beneficiary){
        CredentialBenefits benefits = new CredentialBenefits();

        benefits.setBeneficiaryType("Titular"); //TOdo this hardcode could be improved
        benefits.setDniBeneficiary(beneficiary.getDocumentNumber());

        benefits.setDateOfIssue(DateUtil.getLocalDateTimeNow());
        benefits.setBeneficiary(beneficiary);

        benefits.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_BENEFITS.getCode());

        //TODO this should be took from DB - credentialCredit.setIdDidiIssuer();

        Optional<DIDHisotoric> opActiveDid = didHistoricRepository.findByIdPersonAndIsActive(beneficiary.getId(), true);
        if(opActiveDid.isPresent()) {
            benefits.setIdDidiReceptor(opActiveDid.get().getIdDidiReceptor());
            benefits.setCredentialState(new CredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode()));
            benefits.setIdDidiCredential(opActiveDid.get().getIdDidiReceptor());
        }
        else{
            //Person do not have a DID yet -> set as pending didi
            benefits.setCredentialStatus(CredentialStatusCodes.CREDENTIAL_PENDING_DIDI.getCode());
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
        credentialCredit.setDniBeneficiary(29302594L);
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
