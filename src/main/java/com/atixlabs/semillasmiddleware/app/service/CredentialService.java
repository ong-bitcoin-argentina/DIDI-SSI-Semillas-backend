package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.bondarea.repository.LoanRepository;
import com.atixlabs.semillasmiddleware.app.model.DIDHistoric.DIDHisotoric;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.credential.*;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialTypesCodes;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.repository.*;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.Category;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.DwellingCategory;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.EntrepreneurshipCategory;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefits;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.repository.CredentialCreditRepository;
import com.atixlabs.semillasmiddleware.app.repository.PersonRepository;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.AnswerCategoryFactory;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.PersonCategory;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.Categories;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.SurveyForm;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.*;
import com.atixlabs.semillasmiddleware.app.dto.CredentialDto;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
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
    private CredentialIdentityRepository credentialIdentityRepository;
    private CredentialEntrepreneurshipRepository credentialEntrepreneurshipRepository;
    private CredentialDwellingRepository credentialDwellingRepository;
    private PersonRepository personRepository;
    private LoanRepository loanRepository;
    private CredentialBenefitsRepository credentialBenefitsRepository;
    private DIDHistoricRepository didHistoricRepository;
    private CredentialStateRepository credentialStateRepository;
    private AnswerCategoryFactory answerCategoryFactory;

    @Autowired
    public CredentialService(
            CredentialCreditRepository credentialCreditRepository,
            CredentialRepository credentialRepository,
            PersonRepository personRepository,
            LoanRepository loanRepository,
            CredentialBenefitsRepository credentialBenefitsRepository,
            DIDHistoricRepository didHistoricRepository,
            CredentialStateRepository credentialStateRepository,
            AnswerCategoryFactory answerCategoryFactory,
            CredentialIdentityRepository credentialIdentityRepository,
            CredentialEntrepreneurshipRepository credentialEntrepreneurshipRepository,
            CredentialDwellingRepository credentialDwellingRepository
    ) {
        this.credentialCreditRepository = credentialCreditRepository;
        this.credentialRepository = credentialRepository;
        this.personRepository = personRepository;
        this.loanRepository = loanRepository;
        this.credentialBenefitsRepository = credentialBenefitsRepository;
        this.didHistoricRepository = didHistoricRepository;
        this.credentialStateRepository = credentialStateRepository;
        this.answerCategoryFactory = answerCategoryFactory;
        this.credentialIdentityRepository = credentialIdentityRepository;
        this.credentialEntrepreneurshipRepository =  credentialEntrepreneurshipRepository;
        this.credentialDwellingRepository = credentialDwellingRepository;
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
            switch (category.getCategoryName()) {
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

    private boolean isCredentialAlreadyExistent(Long beneficiaryDni, String credentialCategoryCode, ProcessExcelFileResult processExcelFileResult) {
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
                    "Ya existe una credencial de tipo " + credentialCategoryCode +
                            " en estado ACTIVO" +
                            " para el DNI " + beneficiaryDni + " para continuar debe revocarlas manualmente"
            );
        return true;
    }

    private void saveAllCredentialsFromForm(SurveyForm surveyForm) {
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

    private void saveCredential(Category category, Person creditHolder) {
        log.info("  saveCredential: " + category.getCategoryName());
        switch (category.getCategoryName()) {
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

    public List<Credential> findCredentials(String credentialType, String name, String dniBeneficiary, String
            idDidiCredential, String dateOfExpiry, String dateOfIssue, List<String> credentialState) {
        List<Credential> credentials;
        credentials = credentialRepository.findCredentialsWithFilter(credentialType, name, dniBeneficiary, idDidiCredential, dateOfExpiry, dateOfIssue, credentialState);

        return credentials;
    }

    private Person getPersonFromPersonCategory(PersonCategory personCategory) {
        Person person = new Person();
        person.setDocumentNumber(personCategory.getIdNumber());
        person.setFirstName(personCategory.getName());
        person.setLastName(personCategory.getSurname());
        person.setBirthDate(personCategory.getBirthDate());
        return person;
    }

    private boolean arePersonEquals(Person person1, Person person2) {
        return person1.getDocumentNumber().equals(person2.getDocumentNumber()) &&
                person1.getFirstName().equals(person2.getFirstName()) &&
                person1.getLastName().equals(person2.getLastName()) &&
                person1.getBirthDate().isEqual(person2.getBirthDate());
    }

    private Person savePersonIfNew(Person person) {
        Optional<Person> personOptional = personRepository.findByDocumentNumber(person.getDocumentNumber());
        if (personOptional.isEmpty())
            return personRepository.save(person);
        if (!arePersonEquals(person, personOptional.get())) {
            person.setId(personOptional.get().getId());
            return personRepository.save(person);
        }
        return personOptional.get();
    }

    private void buildCredential(Person creditHolder, Credential credential) {
        creditHolder = savePersonIfNew(creditHolder);

        credential.setDateOfIssue(LocalDateTime.now());
        credential.setCreditHolder(creditHolder);
        credential.setCreditHolderDni(creditHolder.getDocumentNumber());
        credential.setCreditHolderName(creditHolder.getFirstName() + " " + creditHolder.getLastName());

        //the beneficiary is the same as the credit holder for all credentials but identity
        //buildIdentityCredential overwrites this value with the different members.
        credential.setBeneficiary(creditHolder);
        credential.setBeneficiaryDni(creditHolder.getDocumentNumber());
        credential.setBeneficiaryName(creditHolder.getFirstName() + " " + creditHolder.getLastName());

        //credential.setCredentialStatus(CredentialStatusCodes.CREDENTIAL_PENDING_BONDAREA.getCode());
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
        credentialIdentity.setBeneficiaryName(beneficiary.getFirstName() + " " + beneficiary.getLastName());

        credentialIdentity.setCredentialCategory(CredentialCategoriesCodes.IDENTITY.getCode());

        switch (beneficiaryPersonCategory.getPersonType()) {
            case BENEFICIARY:
                credentialIdentity.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_IDENTITY.getCode());
                break;
            case SPOUSE:
            case CHILD:
            case OTHER_KINSMAN:
                credentialIdentity.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_IDENTITY_FAMILY.getCode());
                break;
        }

        return credentialIdentity;
    }

    private CredentialEntrepreneurship buildEntrepreneurshipCredential(Category category, Person creditHolder) {
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

    public List<Credential> findCredentials(String credentialType, String name, String dniBeneficiary, String
            idDidiCredential, String dateOfExpiry, String dateOfIssue, List<String> credentialState, String
                                                    credentialStatus) {
        List<Credential> credentials;
        try {
            credentials = credentialRepository.findCredentialsWithFilter(
                    credentialType,
                    name,
                    dniBeneficiary,
                    idDidiCredential,
                    dateOfExpiry,
                    dateOfIssue,
                    credentialState
            );
        } catch (Exception e) {
            log.info("There has been an error searching for credentials " + e);
            return Collections.emptyList();
        }
        return credentials;
    }


    public void saveCredentialCreditMock() {
        CredentialCredit credentialCredit = new CredentialCredit();
        credentialCredit.setIdGroup("1111BBB");
        credentialCredit.setAmount(1d);
        credentialCredit.setCurrentCycle("Cycle");
        credentialCredit.setCreditState("state");
        credentialCredit.setCreditHolderDni(29302594L);
        credentialCreditRepository.save(credentialCredit);
    }

    public void createNewCreditCredentials(Loan loan) throws Exception {
        //beneficiarieSSSS -> the credit group will be created by separate (not together)
        Optional<CredentialCredit> opCreditExistence = credentialCreditRepository.findByIdBondareaCredit(loan.getIdBondareaLoan());
        if (opCreditExistence.isEmpty()) {
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
        } else {
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

        //Added Modification CreditHolderDni and CreditHolderId (Dario):
        credentialCredit.setBeneficiary(beneficiary);
        credentialCredit.setBeneficiaryDni(beneficiary.getDocumentNumber());
        credentialCredit.setBeneficiaryName(beneficiary.getFirstName()+" "+beneficiary.getLastName());

        credentialCredit.setCreditHolderDni(beneficiary.getDocumentNumber());
        credentialCredit.setCreditHolder(beneficiary);
        credentialCredit.setCreditHolderName(beneficiary.getFirstName()+" "+beneficiary.getLastName());
        //End creditHolder changes(Dario)







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
    public void createBenefitsCredential(Person beneficiary) {
        Optional<CredentialBenefits> opBenefits = credentialBenefitsRepository.findByBeneficiaryDni(beneficiary.getDocumentNumber());
        //TODO if TITULAR has a benefit with state REVOCADA ? create new ? or not ?
        //create benefit if person does not have or is holder
        if (opBenefits.isEmpty() || !opBenefits.get().getBeneficiaryType().equals(PersonTypesCodes.HOLDER.getCode())) {
            CredentialBenefits benefits = this.buildBenefitsCredential(beneficiary, PersonTypesCodes.HOLDER);
            credentialBenefitsRepository.save(benefits);
        } else {
            log.info("Person with dni" + beneficiary.getDocumentNumber() + "already has a credential benefits");
        }
    }

    public List<CredentialDto> findAllCredentialsMock() {
        List<CredentialDto> credentials = new ArrayList<>();
        return credentials;
    }

    /**
     * @param beneficiary
     * @param personType
     * @return CredentialBenefits
     */
    public CredentialBenefits buildBenefitsCredential(Person beneficiary, PersonTypesCodes personType) {
        CredentialBenefits benefits = new CredentialBenefits();

        //Person is holder or family
        if (personType.equals(PersonTypesCodes.HOLDER)) {
            benefits.setBeneficiaryType(PersonTypesCodes.HOLDER.getCode());
            benefits.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_BENEFITS.getCode());
        } else {
            benefits.setBeneficiaryType(PersonTypesCodes.FAMILY.getCode());
            benefits.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_BENEFITS_FAMILY.getCode());
        }

        benefits.setDateOfIssue(DateUtil.getLocalDateTimeNow());

        //Added Modification CreditHolderDni and CreditHolderId (Dario):
        benefits.setBeneficiary(beneficiary);
        benefits.setBeneficiaryDni(beneficiary.getDocumentNumber());
        benefits.setBeneficiaryName(beneficiary.getFirstName()+" "+beneficiary.getLastName());

        benefits.setCreditHolderDni(beneficiary.getDocumentNumber());
        benefits.setCreditHolder(beneficiary);
        benefits.setCreditHolderName(beneficiary.getFirstName()+" "+beneficiary.getLastName());
        //End creditHolder changes(Dario)

        //TODO credentialCredit.setIdHistorical();
        //TODO this should be took from DB - credentialCredit.setIdDidiIssuer();

        Optional<DIDHisotoric> opActiveDid = didHistoricRepository.findByIdPersonAndIsActive(beneficiary.getId(), true);
        if (opActiveDid.isPresent()) {
            benefits.setIdDidiReceptor(opActiveDid.get().getIdDidiReceptor());
            Optional<CredentialState> opStateActive = credentialStateRepository.findByStateName(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode());
            if (opStateActive.isPresent()) {
                benefits.setCredentialState(opStateActive.get());
                benefits.setIdDidiCredential(opActiveDid.get().getIdDidiReceptor());
            }
        } else {
            //Person do not have a DID yet -> set as pending didi
            Optional<CredentialState> opStateActive = credentialStateRepository.findByStateName(CredentialStatesCodes.PENDING_DIDI.getCode());
            if (opStateActive.isPresent())
                benefits.setCredentialState(opStateActive.get());
        }

        return benefits;
    }

}