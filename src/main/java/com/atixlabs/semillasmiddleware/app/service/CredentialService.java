package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.credential.*;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatusCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialTypesCodes;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.repository.*;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.Category;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.DwellingCategory;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.EntrepreneurshipCategory;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.PersonCategory;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.Categories;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.SurveyForm;
import com.atixlabs.semillasmiddleware.app.dto.CredentialDto;
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
    private PersonRepository personRepository;
    @Autowired
    private CredentialRepository credentialRepository;
    @Autowired
    private CredentialCreditRepository credentialCreditRepository;
    @Autowired
    private CredentialIdentityRepository credentialIdentityRepository;
    @Autowired
    private CredentialEntrepreneurshipRepository credentialEntrepreneurshipRepository;
    @Autowired
    CredentialDwellingRepository credentialDwellingRepository;
    @Autowired
    private CredentialStateRepository credentialStateRepository;


    @Autowired
    public CredentialService(CredentialCreditRepository credentialCreditRepository, CredentialRepository credentialRepository) {
        this.credentialCreditRepository = credentialCreditRepository;
        this.credentialRepository = credentialRepository;
    }


    public void buildAllCredentialsFromForm(SurveyForm surveyForm, ProcessExcelFileResult processExcelFileResult) {
        log.info("buildAllCredentialsFromForm: " + this.toString());
        if (validateAllCredentialsFromForm(surveyForm, processExcelFileResult))
            saveAllCredentialsFromForm(surveyForm);
    }

    /**
     * The following are non-public methods, isolating functionality.
     * to make public methods easier to read.
     *
     * @param surveyForm
     */
    private boolean validateAllCredentialsFromForm(SurveyForm surveyForm, ProcessExcelFileResult processExcelFileResult) {
        log.info("  validateIdentityCredentialFromForm");

        //1-get all people data from form, creditHolder will be a beneficiary as well.
        ArrayList<Category> categoryArrayList = surveyForm.getAllCompletedCategories();

        //2-get creditHolder Data
        PersonCategory creditHolderPersonCategory = (PersonCategory) surveyForm.getCategoryByUniqueName(Categories.BENEFICIARY_CATEGORY_NAME.getCode(), null);
        Person creditHolder = getPersonFromPersonCategory(creditHolderPersonCategory);

        //2-verify each person is new, or his data has not changed.
        boolean allCredentialsNewOrInactive = true;
        for (Category category : categoryArrayList) {
            switch (category.getCategoryName()){
                case BENEFICIARY_CATEGORY_NAME:
                case SPOUSE_CATEGORY_NAME:
                case CHILD_CATEGORY_NAME:
                case KINSMAN_CATEGORY_NAME:
                    PersonCategory beneficiaryPersonCategory = (PersonCategory) category;
                    Person beneficiary = getPersonFromPersonCategory(beneficiaryPersonCategory);
                    if (isCredentialAlreadyExistent(beneficiary.getDocumentNumber(), CredentialCategoriesCodes.IDENTITY.getCode(), processExcelFileResult))
                        allCredentialsNewOrInactive = false;
                    break;
                case ENTREPRENEURSHIP_CATEGORY_NAME:
                    if (isCredentialAlreadyExistent(creditHolder.getDocumentNumber(), CredentialCategoriesCodes.ENTREPRENEURSHIP.getCode(), processExcelFileResult))
                        allCredentialsNewOrInactive = false;
                    break;
                case DWELLING_CATEGORY_NAME:
                    if (isCredentialAlreadyExistent(creditHolder.getDocumentNumber(), CredentialCategoriesCodes.DWELLING.getCode(), processExcelFileResult))
                        allCredentialsNewOrInactive = false;
                    break;
            }
        }
        return allCredentialsNewOrInactive;
    }

    private boolean isCredentialAlreadyExistent(Long beneficiaryDni, String credentialCategoryCode, ProcessExcelFileResult processExcelFileResult){
        CredentialState credentialStateActive = null;
        Optional<CredentialState> credentialStateOptional = credentialStateRepository.findByStateName(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode());
        if (credentialStateOptional.isPresent())
            credentialStateActive = credentialStateOptional.get();

        Optional<Credential> credentialOptional = credentialRepository.findByBeneficiaryDniAndAndCredentialCategoryAndCredentialState(
                beneficiaryDni,
                credentialCategoryCode,
                credentialStateActive
        );
        if (credentialOptional.isEmpty())
            return false;
        else
            processExcelFileResult.addRowError(
                    "Warning CREDENCIAL DUPLICADA",
                    "Ya existe una credencial de tipo "+credentialCategoryCode+
                             " en estado ACTIVO"+
                             " para el DNI "+beneficiaryDni+" para continuar debe revocarlas manualmente"
            );
        return true;
    }

    private void saveAllCredentialsFromForm(SurveyForm surveyForm){
        //1-get creditHolder Data
        PersonCategory creditHolderPersonCategory = (PersonCategory) surveyForm.getCategoryByUniqueName(Categories.BENEFICIARY_CATEGORY_NAME.getCode(), null);
        Person creditHolder = getPersonFromPersonCategory(creditHolderPersonCategory);

        //1-get all data from form
        ArrayList<Category> categoryArrayList = surveyForm.getAllCompletedCategories();

        //4-Now working with each beneficiary
        for (Category category : categoryArrayList) {
            saveCredential(category, creditHolder);
        }
    }
    private void saveCredential(Category category, Person creditHolder){
        log.info("  saveCredential: "+category.getCategoryName());
        switch (category.getCategoryName()){
            case BENEFICIARY_CATEGORY_NAME:
            case CHILD_CATEGORY_NAME:
            case SPOUSE_CATEGORY_NAME:
            case KINSMAN_CATEGORY_NAME:
                credentialIdentityRepository.save(buildIdentityCredential(category, creditHolder));
                break;
            case ENTREPRENEURSHIP_CATEGORY_NAME:
                credentialEntrepreneurshipRepository.save(buildEntrepreneurshipCredential(category, creditHolder));
                break;
            case DWELLING_CATEGORY_NAME:
                credentialDwellingRepository.save(buildDwellingCredential(category, creditHolder));
                break;
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

    private boolean arePersonEquals(Person person1, Person person2){
        return  person1.getDocumentNumber().equals(person2.getDocumentNumber()) &&
                person1.getFirstName().equals(person2.getFirstName()) &&
                person1.getLastName().equals(person2.getLastName()) &&
                person1.getBirthDate().isEqual(person2.getBirthDate());
    }

    private Person savePersonIfNew(Person person){
        Optional<Person> personOptional = personRepository.findByDocumentNumber(person.getDocumentNumber());
        if (personOptional.isEmpty())
            return personRepository.save(person);
        if (!arePersonEquals(person, personOptional.get())){
            person.setId(personOptional.get().getId());
            return personRepository.save(person);
        }
        return personOptional.get();
    }

    private void buildCredential(Person creditHolder, Credential credential){
        creditHolder = savePersonIfNew(creditHolder);

        credential.setDateOfIssue(LocalDateTime.now());
        credential.setCreditHolder(creditHolder);
        credential.setCreditHolderDni(creditHolder.getDocumentNumber());
        credential.setCreditHolderName(creditHolder.getFirstName()+" "+creditHolder.getLastName());

        //the beneficiary is the same as the credit holder for all credentials but identity
        //buildIdentityCredential overwrites this value with the different members.
        credential.setBeneficiary(creditHolder);
        credential.setBeneficiaryDni(creditHolder.getDocumentNumber());
        credential.setBeneficiaryName(creditHolder.getFirstName()+" "+creditHolder.getLastName());

        credential.setCredentialStatus(CredentialStatusCodes.CREDENTIAL_PENDING_BONDAREA.getCode());
        Optional<CredentialState> credentialStateOptional = credentialStateRepository.findByStateName(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode());
        credentialStateOptional.ifPresent(credential::setCredentialState);
    }

    private CredentialIdentity buildIdentityCredential(Category category, Person creditHolder) {
        PersonCategory beneficiaryPersonCategory = (PersonCategory) category;
        Person beneficiary = getPersonFromPersonCategory(beneficiaryPersonCategory);
        beneficiary = savePersonIfNew(beneficiary);

        CredentialIdentity credentialIdentity = new CredentialIdentity();
        buildCredential(creditHolder, credentialIdentity);

        credentialIdentity.setBeneficiary(beneficiary);
        credentialIdentity.setBeneficiaryDni(beneficiary.getDocumentNumber());
        credentialIdentity.setBeneficiaryName(beneficiary.getFirstName()+" "+beneficiary.getLastName());

        credentialIdentity.setCredentialCategory(CredentialCategoriesCodes.IDENTITY.getCode());

        switch (beneficiaryPersonCategory.getPersonType()){
            case BENEFICIARY:
                credentialIdentity.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_IDENTITY.getCode());
                break;
            case SPOUSE:
            case CHILD:
            case OTHER_KINSMAN:
                credentialIdentity.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_IDENTITY_FAMILIAR.getCode());
                break;
        }

        return credentialIdentity;
    }

    private CredentialEntrepreneurship buildEntrepreneurshipCredential(Category category, Person creditHolder){
        EntrepreneurshipCategory entrepreneurshipCategory = (EntrepreneurshipCategory) category;

        CredentialEntrepreneurship credentialEntrepreneurship = new CredentialEntrepreneurship();
        buildCredential(creditHolder, credentialEntrepreneurship);
        credentialEntrepreneurship.setEntrepreneurshipType(entrepreneurshipCategory.getType());
        credentialEntrepreneurship.setStartActivity(entrepreneurshipCategory.getActivityStartDate());
        credentialEntrepreneurship.setMainActivity(entrepreneurshipCategory.getMainActivity());
        credentialEntrepreneurship.setEntrepreneurshipName(entrepreneurshipCategory.getName());
        credentialEntrepreneurship.setEntrepreneurshipAddress(entrepreneurshipCategory.getAddress());
        credentialEntrepreneurship.setEndActivity(entrepreneurshipCategory.getActivityEndingDate());

        credentialEntrepreneurship.setCredentialCategory(CredentialCategoriesCodes.ENTREPRENEURSHIP.getCode());
        credentialEntrepreneurship.setCredentialDescription(CredentialCategoriesCodes.ENTREPRENEURSHIP.getCode());

        return credentialEntrepreneurship;
    }

    private CredentialDwelling buildDwellingCredential(Category category, Person creditHolder) {
        DwellingCategory entrepreneurshipCategory = (DwellingCategory) category;

        CredentialDwelling credentialDwelling = new CredentialDwelling();
        buildCredential(creditHolder, credentialDwelling);

        credentialDwelling.setDwellingType(entrepreneurshipCategory.getDwellingType());
        credentialDwelling.setDwellingAddress(entrepreneurshipCategory.getDistrict());
        credentialDwelling.setPossessionType(entrepreneurshipCategory.getHoldingType());

        credentialDwelling.setCredentialCategory(CredentialCategoriesCodes.DWELLING.getCode());
        credentialDwelling.setCredentialDescription(CredentialCategoriesCodes.DWELLING.getCode());

        return credentialDwelling;
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
        credentialCredit.setCreditName("credit name");
        credentialCredit.setIdGroup(1111L);
        credentialCredit.setGroupName("GroupName");
        credentialCredit.setRol("rol");
        credentialCredit.setAmount(1d);
        credentialCredit.setCurrentCycle("Cycle");
        credentialCredit.setCreditState("state");
        credentialCredit.setCreditHolderDni(29302594L);
        //credentialCredit.setBeneficiaryDni(29302594L);
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
