package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialIdentity;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatusCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialTypesCodes;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.repository.CredentialCreditRepository;
import com.atixlabs.semillasmiddleware.app.repository.CredentialIdentityRepository;
import com.atixlabs.semillasmiddleware.app.repository.PersonRepository;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.AnswerCategoryFactory;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.Category;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.PersonCategory;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.SurveyForm;
import com.atixlabs.semillasmiddleware.app.dto.CredentialDto;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.repository.CredentialRepository;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CredentialService {

    @Autowired
    private CredentialRepository credentialRepository;
    @Autowired
    private CredentialCreditRepository credentialCreditRepository;
    @Autowired
    private PersonRepository personRepository;


    @Autowired
    private CredentialIdentityRepository credentialIdentityRepository;

    @Autowired
    private AnswerCategoryFactory answerCategoryFactory;

    @Autowired
    public CredentialService(CredentialCreditRepository credentialCreditRepository, CredentialRepository credentialRepository) {
        this.credentialCreditRepository = credentialCreditRepository;
        this.credentialRepository = credentialRepository;
    }


    public void buildAllCredentialsFromForm(SurveyForm surveyForm, ProcessExcelFileResult processExcelFileResult) {
        log.info("buildAllCredentialsFromForm: " + this.toString());
        buildIdentityCredential(surveyForm, processExcelFileResult);
        buildEntrepreneurshipCredential(surveyForm, processExcelFileResult);
        buildHomeCredential(surveyForm, processExcelFileResult);
    }

    /**
     * The following are non-public methods, isolating functionality.
     * to make public methods easier to read.
     *
     * @param surveyForm
     */
    private void buildIdentityCredential(SurveyForm surveyForm, ProcessExcelFileResult processExcelFileResult) {
        log.info("  buildIdentityCredential");

        ArrayList<Category> personsArrayList = surveyForm.getCompletedElementsOfCategoryFromForm(PersonCategory.class);

        for (Category category : personsArrayList) {
            PersonCategory personCategory = (PersonCategory) category;

            //ENCUENTRO LA PERSONA:
            Optional<Person> beneficiaryOptional = personRepository.findByDocumentNumber(personCategory.getIdNumber());
            if (beneficiaryOptional.isEmpty()){
                personRepository.save(fillPersonData(personCategory));//Saving person if not exists
                beneficiaryOptional = personRepository.findByDocumentNumber(personCategory.getIdNumber());//Returning new person id
            }

            Optional<CredentialIdentity> CredentialIdentityOptional = credentialIdentityRepository.findByBeneficiary(beneficiaryOptional.get());

            if (CredentialIdentityOptional.isEmpty())
                credentialIdentityRepository.save(fillIdentityCredentialData(personCategory, beneficiaryOptional.get()));
            else
                processExcelFileResult.addRowError(
                        "Warning "+personCategory.getCategoryOriginalName(),
                        "Ya existe una credencial para el DNI " + personCategory.getIdNumber()
                );
        }
    }

    private Person fillPersonData(PersonCategory personCategory){
        Person beneficiaryNew = new Person();
        beneficiaryNew.setDocumentNumber(personCategory.getIdNumber());
        beneficiaryNew.setFirstName(personCategory.getName());
        beneficiaryNew.setLastName(personCategory.getSurname());
        beneficiaryNew.setBirthDate(personCategory.getBirthDate());
        return beneficiaryNew;
    }

    private CredentialIdentity fillIdentityCredentialData(PersonCategory personCategory, Person beneficiary) {
        CredentialIdentity credentialIdentity = new CredentialIdentity();
        credentialIdentity.setCredentialCategory(CredentialCategoriesCodes.IDENTITY.getCode());

        switch (personCategory.getPersonType()){
            case BENEFICIARY:
                credentialIdentity.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_IDENTITY.getCode());
                break;
            case SPOUSE:
            case CHILD:
            case OTHER_KINSMAN:
                credentialIdentity.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_IDENTITY_FAMILIAR.getCode());
                break;
        }
        credentialIdentity.setBeneficiary(beneficiary);
        credentialIdentity.setCredentialStatus(CredentialStatusCodes.CREDENTIAL_PENDING_BONDAREA.getCode());
        CredentialState credentialState = new CredentialState();
        credentialState.setId(1L);//todo: revisar hacer un find x code para encontrar ID.
        credentialIdentity.setCredentialState(credentialState);
        return credentialIdentity;
    }


    private void buildEntrepreneurshipCredential(SurveyForm surveyForm, ProcessExcelFileResult processExcelFileResult) {


    }

    private void buildHomeCredential(SurveyForm surveyForm, ProcessExcelFileResult processExcelFileResult) {
    }


    public List<Credential> findCredentials(String credentialType, String name, String dniBeneficiary, String idDidiCredential, String dateOfExpiry, String dateOfIssue, List<String> credentialState, String credentialStatus) {
        List<Credential> credentials;
        try {
            credentials = credentialRepository.findCredentialsWithFilter(credentialType, name, dniBeneficiary, idDidiCredential, dateOfExpiry, dateOfIssue, credentialState, credentialStatus);
        } catch (Exception e) {
            log.info("There has been an error searching for credentials " + e);
            return Collections.emptyList();
        }
        return credentials;
    }


    public void saveCredentialCreditMock() {
        CredentialCredit credentialCredit = new CredentialCredit();
        //credentialCredit.setDateOfExpiry(LocalDateTime.now());

        //credentialCredit.setDateOfIssue(LocalDateTime.now());

        //credentialCredit.setId(1L);//autogenerated
        //credentialCredit.setIdDidiCredential(456L);//null because must be completed by didi
        //credentialCredit.setIdDidiIssuer(123L);//null must be completed by didi
        //credentialCredit.setIdDidiReceptor(234L);//null must be completed by didi
        //credentialCredit.setIdHistorical(77L);//null must be completed by didi
        //credentialCredit.setIdRelatedCredential(534L);//tbd value


        credentialCredit.setCreditName("credit name");
        credentialCredit.setIdGroup(1111L);
        credentialCredit.setGroupName("GroupName");
        credentialCredit.setRol("rol");
        credentialCredit.setAmount(1d);
        credentialCredit.setCurrentCycle("Cycle");
        credentialCredit.setCreditState("state");
        //credentialCredit.setDniBeneficiary(29302594L);
        credentialCreditRepository.save(credentialCredit);
    }


    public List<CredentialDto> findAllCredentialsMock() {
        List<CredentialDto> credentials = new ArrayList<>();


        CredentialDto credential1 = new CredentialDto(1L, 2L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), "Jorge Rodrigues", 29302594L, "Estado", "CrdentialCredit");
        credentials.add(credential1);

        CredentialDto credential2 = new CredentialDto(2L, 3L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), "Uriel Brama", 29302594L, "Estado", "CrdentialIdentity");
        credentials.add(credential2);

        CredentialDto credential3 = new CredentialDto(3L, 4L, LocalDateTime.now(), LocalDateTime.now().plusDays(2), "Pepe Grillo", 293025464L, "Estado", "CredentialCredit");
        credentials.add(credential3);

        CredentialDto credential4 = new CredentialDto(4L, 5L, LocalDateTime.now(), LocalDateTime.now().plusDays(6), "Armando Thompson", 29302594L, "Estado", "CredentialDwelling");
        credentials.add(credential4);

        return credentials;

    }

}
