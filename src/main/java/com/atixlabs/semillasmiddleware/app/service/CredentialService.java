package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialIdentity;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatusCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialTypesCodes;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.repository.*;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.Category;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.PersonCategory;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.Categories;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.PersonType;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.SurveyForm;
import com.atixlabs.semillasmiddleware.app.dto.CredentialDto;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.hql.internal.ast.ParseErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CredentialService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private CredentialRepository credentialRepository;
    @Autowired
    private CredentialCreditRepository credentialCreditRepository;
    @Autowired
    private CredentialIdentityRepository credentialIdentityRepository;
    @Autowired
    private CredentialStateRepository credentialStateRepository;


    @Autowired
    public CredentialService(CredentialCreditRepository credentialCreditRepository, CredentialRepository credentialRepository) {
        this.credentialCreditRepository = credentialCreditRepository;
        this.credentialRepository = credentialRepository;
    }


    public void buildAllCredentialsFromForm(SurveyForm surveyForm, ProcessExcelFileResult processExcelFileResult) {
        log.info("buildAllCredentialsFromForm: " + this.toString());
        buildIdentityCredentialFromForm(surveyForm, processExcelFileResult);
        buildEntrepreneurshipCredentialFromForm(surveyForm, processExcelFileResult);
        buildHomeCredentialFromForm(surveyForm, processExcelFileResult);
    }

    /**
     * The following are non-public methods, isolating functionality.
     * to make public methods easier to read.
     *
     * @param surveyForm
     */
    private void buildIdentityCredentialFromForm(SurveyForm surveyForm, ProcessExcelFileResult processExcelFileResult) {
        log.info("  buildIdentityCredential");

        //1-get all people data from form, creditHolder will be a beneficiary as well.
        ArrayList<Category> personsArrayList = surveyForm.getCompletedCategoriesByClass(PersonCategory.class);

        //2-verify each person is new or not but his data has not changed.
        boolean allValidationOk = true;
        for (Category beneficiaryCategory : personsArrayList) {
            PersonCategory beneficiaryPersonCategory = (PersonCategory) beneficiaryCategory;
            Person beneficiary = getPersonFromPersonCategory(beneficiaryPersonCategory);
            if (!isPersonNewOrUnchanged(beneficiary, processExcelFileResult))
                allValidationOk = false;
            if (!isCredentialNew(beneficiary.getDocumentNumber(), processExcelFileResult))
                allValidationOk = false;
        }

        if (allValidationOk) {
            //3-Now that every person is new or unchanged: get creditHolder Data
            PersonCategory creditHolderPersonCategory = (PersonCategory) surveyForm.getCategoryByName(Categories.BENEFICIARY_CATEGORY_NAME.getCode(), null);
            Person creditHolder = getPersonFromPersonCategory(creditHolderPersonCategory);
            creditHolder = savePersonIfNew(creditHolder);

            //4-Now working with each beneficiary
            for (Category beneficiaryCategory : personsArrayList) {
                PersonCategory beneficiaryPersonCategory = (PersonCategory) beneficiaryCategory;
                Person beneficiary = getPersonFromPersonCategory(beneficiaryPersonCategory);

                //the person is new or unchanged
                beneficiary = savePersonIfNew(beneficiary);

                //the credential is new we already checked
                credentialIdentityRepository.save(buildIdentityCredential(beneficiary, beneficiaryPersonCategory.getPersonType(), creditHolder));
            }
        }
    }

    private Person getPersonFromPersonCategory(PersonCategory personCategory){
        Person person = new Person();
        person.setDocumentNumber(personCategory.getIdNumber());
        person.setFirstName(personCategory.getName());
        person.setLastName(personCategory.getSurname());
        person.setBirthDate(personCategory.getBirthDate());
        return person;
    }

    private boolean isPersonNewOrUnchanged(Person person, ProcessExcelFileResult processExcelFileResult){
        Optional<Person> personOptional = personRepository.findByDocumentNumber(person.getDocumentNumber());
        if (personOptional.isEmpty())
            return true;
        else {
            if (verifyPersonData(person, personOptional.get()))
                return true;
            else
             processExcelFileResult.addRowError(
                     "warning-negocio",
                     person.getFirstName()+" "+person.getLastName() +" con DNI:"+ person.getDocumentNumber() +" tiene cambios y no será modificada"
             );
        }
        return false;
    }

    private boolean verifyPersonData(Person person1, Person person2){
        return person1.getDocumentNumber().equals(person2.getDocumentNumber()) &&
                person1.getFirstName().equals(person2.getFirstName()) &&
                person1.getLastName().equals(person2.getLastName()) &&
                person1.getBirthDate().isEqual(person2.getBirthDate());
    }


    private boolean isCredentialNew(Long beneficiaryDni, ProcessExcelFileResult processExcelFileResult){
        CredentialState credentialStateActive = null;
        Optional<CredentialState> credentialStateOptional = credentialStateRepository.findByStateName(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode());
        if (credentialStateOptional.isPresent())
            credentialStateActive = credentialStateOptional.get();

        Optional<CredentialIdentity> CredentialIdentityOptional = credentialIdentityRepository.findByBeneficiaryDniAndCredentialState(beneficiaryDni, credentialStateActive);
        if (CredentialIdentityOptional.isEmpty())
            return true;
        else
            processExcelFileResult.addRowError(
                    "Warning CREDENCIAL DUPLICADA",
                    "Ya existe una credencial ACTIVA para el DNI " + beneficiaryDni+" debe revocarlas manualmente"
            );
        return false;
    }

    private Person savePersonIfNew(Person person){
        Optional<Person> personOptional = personRepository.findByDocumentNumber(person.getDocumentNumber());
        if (personOptional.isEmpty())
            return personRepository.save(person);
        else
            return personOptional.get();
    }

    private CredentialIdentity buildIdentityCredential(Person beneficiary, PersonType beneficiaryPersonType, Person creditHolder) {
        CredentialIdentity credentialIdentity = new CredentialIdentity();
        credentialIdentity.setDateOfIssue(LocalDateTime.now());
        credentialIdentity.setCreditHolder(creditHolder);
        credentialIdentity.setCreditHolderDni(creditHolder.getDocumentNumber());
        credentialIdentity.setCreditHolderName(creditHolder.getFirstName()+" "+creditHolder.getLastName());
        credentialIdentity.setBeneficiary(beneficiary);
        credentialIdentity.setBeneficiaryDni(beneficiary.getDocumentNumber());
        credentialIdentity.setBeneficiaryName(beneficiary.getFirstName()+" "+beneficiary.getLastName());

        Optional<CredentialState> credentialStateOptional = credentialStateRepository.findByStateName(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode());
        credentialStateOptional.ifPresent(credentialIdentity::setCredentialState);

        switch (beneficiaryPersonType){
            case BENEFICIARY:
                credentialIdentity.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_IDENTITY.getCode());
                break;
            case SPOUSE:
            case CHILD:
            case OTHER_KINSMAN:
                credentialIdentity.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_IDENTITY_FAMILIAR.getCode());
                break;
        }

        credentialIdentity.setCredentialCategory(CredentialCategoriesCodes.IDENTITY.getCode());
        return credentialIdentity;
    }


    private void buildEntrepreneurshipCredentialFromForm(SurveyForm surveyForm, ProcessExcelFileResult processExcelFileResult) {


    }

    private void buildHomeCredentialFromForm(SurveyForm surveyForm, ProcessExcelFileResult processExcelFileResult) {
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
