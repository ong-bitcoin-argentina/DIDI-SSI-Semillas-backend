package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.bondarea.repository.LoanRepository;
import com.atixlabs.semillasmiddleware.app.exceptions.NoExpiredConfigurationExists;
import com.atixlabs.semillasmiddleware.app.exceptions.PersonDoesNotExists;
import com.atixlabs.semillasmiddleware.app.model.DIDHistoric.DIDHisotoric;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.configuration.ParameterConfiguration;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private ParameterConfigurationRepository parameterConfigurationRepository;

    @Autowired
    public CredentialService(CredentialCreditRepository credentialCreditRepository, CredentialRepository credentialRepository, PersonRepository personRepository, LoanRepository loanRepository, CredentialBenefitsRepository credentialBenefitsRepository, DIDHistoricRepository didHistoricRepository, CredentialStateRepository credentialStateRepository, ParameterConfiguration parameterConfiguration, ParameterConfigurationRepository parameterConfigurationRepository) {
        this.credentialCreditRepository = credentialCreditRepository;
        this.credentialRepository = credentialRepository;
        this.personRepository = personRepository;
        this.loanRepository = loanRepository;
        this.credentialBenefitsRepository = credentialBenefitsRepository;
        this.didHistoricRepository = didHistoricRepository;
        this.credentialStateRepository = credentialStateRepository;
        this.parameterConfigurationRepository = parameterConfigurationRepository;
    }


    public List<Credential> findCredentials(String credentialType, String name, String dniBeneficiary, String idDidiCredential, String dateOfExpiry, String dateOfIssue, List<String> credentialState) {
        List<Credential> credentials;
        try {
            credentials = credentialRepository.findCredentialsWithFilter(credentialType, name, dniBeneficiary, idDidiCredential, dateOfExpiry, dateOfIssue, credentialState);
        } catch (Exception e) {
            log.info("There has been an error searching for credentials " + e);
            return Collections.emptyList();
        }
        return credentials;
    }



    /**
     * Create a new credential credit if the id bondarea of the credit does not exist.
     * Then it creates the benefits credential to the holder
     * @param loan
     * @throws PersonDoesNotExists
     */
    public void createNewCreditCredentials(Loan loan) throws PersonDoesNotExists {
        //beneficiarieSSSS -> the credit group will be created by separate (not together)
        Optional<CredentialCredit> opCreditExistence = credentialCreditRepository.findByIdBondareaCredit(loan.getIdBondareaLoan());
        if (opCreditExistence.isEmpty()) {
            Optional<Person> opBeneficiary = personRepository.findByDocumentNumber(loan.getDniPerson());
            if (opBeneficiary.isPresent()) {
                //the documents must coincide
                CredentialCredit credit = this.buildCreditCredential(loan, opBeneficiary.get());
                loan.setHasCredential(true);

                credit = credentialCreditRepository.save(credit);
                //get the new id and save it on id historic
                credit.setIdHistorical(credit.getId());
                credentialCreditRepository.save(credit);

                loanRepository.save(loan);

                //after create credit, will create benefit holder credential
                this.createNewBenefitsCredential(opBeneficiary.get(), PersonTypesCodes.HOLDER);
            } else {
                throw new PersonDoesNotExists("Person with dni " + loan.getDniPerson() + " has not been created. The loan exists but the survey with this person has not been loaded");
                //this error is important, have to be shown in front
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
        credentialCredit.setDniBeneficiary(beneficiary.getDocumentNumber());

        //Credential Parent fields
        credentialCredit.setDateOfIssue(DateUtil.getLocalDateTimeNow());
        credentialCredit.setBeneficiary(beneficiary);


        //TODO this should be took from DB - credentialCredit.setIdDidiIssuer();
        Optional<DIDHisotoric> opActiveDid = didHistoricRepository.findByIdPersonAndIsActive(beneficiary.getId(), true);
        if (opActiveDid.isPresent()) {
            credentialCredit.setIdDidiReceptor(opActiveDid.get().getIdDidiReceptor());
            credentialCredit.setIdDidiCredential(opActiveDid.get().getIdDidiReceptor());
            Optional<CredentialState> opStateActive = credentialStateRepository.findByStateName(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode());
            if (opStateActive.isPresent()) {
                credentialCredit.setCredentialState(opStateActive.get());
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


    public void createNewBenefitsCredential(Person beneficiary, PersonTypesCodes personType) {
        List<CredentialBenefits> opBenefits = credentialBenefitsRepository.findByDniBeneficiaryAndCredentialState(beneficiary.getDocumentNumber(), new CredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode()));
        List<CredentialBenefits> benefitExistence = opBenefits.stream().filter(credentialBenefit -> credentialBenefit.getBeneficiaryType().equals(personType.getCode())).collect(Collectors.toList());
        //create benefit if person does not have one or | do not have the type wanted to create.
        if (opBenefits.size() == 0 || benefitExistence.size() == 0) {
            CredentialBenefits benefits = this.buildBenefitsCredential(beneficiary, personType);

            //get the new id and save it on id historic
            benefits = credentialBenefitsRepository.save(benefits);
            benefits.setIdHistorical(benefits.getId());
            credentialBenefitsRepository.save(benefits);
        } else {
            log.info("Person with dni" + beneficiary.getDocumentNumber() + "already has a credential benefits");
        }
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
        benefits.setBeneficiary(beneficiary);
        benefits.setDniBeneficiary(beneficiary.getDocumentNumber());


        //TODO this should be took from DB - credentialCredit.setIdDidiIssuer();

        Optional<DIDHisotoric> opActiveDid = didHistoricRepository.findByIdPersonAndIsActive(beneficiary.getId(), true);
        if (opActiveDid.isPresent()) {
            benefits.setIdDidiReceptor(opActiveDid.get().getIdDidiReceptor());
            benefits.setIdDidiCredential(opActiveDid.get().getIdDidiReceptor());
            Optional<CredentialState> opStateActive = credentialStateRepository.findByStateName(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode());
            if (opStateActive.isPresent()) {
                benefits.setCredentialState(opStateActive.get());
            }
        } else {
            //Person do not have a DID yet -> set as pending didi
            Optional<CredentialState> opStateActive = credentialStateRepository.findByStateName(CredentialStatesCodes.PENDING_DIDI.getCode());
            if (opStateActive.isPresent())
                benefits.setCredentialState(opStateActive.get());
        }

        return benefits;
    }

    /**
     * Validate if the credential needs to be updated.
     * @param loan
     * @return the credit or null
     */
    public CredentialCredit validateCredentialCreditToUpdate(Loan loan){
        Optional<CredentialCredit> opCredit = credentialCreditRepository.findByIdBondareaCredit(loan.getIdBondareaLoan());
        if (opCredit.isPresent()) {
            CredentialCredit credit = opCredit.get();
            if (loan.getExpiredAmount() != credit.getExpiredAmount() || loan.getCycleDescription() != credit.getCurrentCycle() || loan.getStatusDescription() != credit.getCreditState()/*||  loan.getTotalCuotas...*/) {
                // the loan has changed, return credit to be update
                return credit;
            }
            else{
                return null;
            }
        }
        else{
            // the credit had been set that has a credential credit, but no credential credit exist with the bondarea id
            // the next time loans are going to be check, a new credential credit would be create
            loan.setHasCredential(false);
            return null;
        }
    }


    /**
     * 2nd Step in the process, after create the new credits. This process will check the previous credential credit and his loan, to update and | or revoke.
     * If there has been a change the credentials is revoked and generated a new one and a new benefit.
     *
     * @param loan
     */
    public void updateCredentialCredit(Loan loan, CredentialCredit credit) throws NoExpiredConfigurationExists, PersonDoesNotExists {
        Long idHistoricCredit = credit.getIdHistorical();
        //TODO revoke credit -> save id historic

        Optional<Person> opBeneficiary = personRepository.findByDocumentNumber(loan.getDniPerson());
        if (opBeneficiary.isPresent()) {
            CredentialCredit updateCredit = this.buildCreditCredential(loan, opBeneficiary.get());
            updateCredit.setIdHistorical(idHistoricCredit); //assign the old historic.
            credentialCreditRepository.save(updateCredit);


            // if credit is finalized credential will be revoke
            if (loan.getStatus() == 60) { // its ok to use 60 state ?
                credit.setFinalizedTime(DateUtil.getLocalDateTimeNow().toLocalDate());
                //TODO No se revoca credito pero beneficio si, si este fuera el unico
            }
            else{
                if(loan.getIsDeleted()){
                    // TODO revoke and set to deleted ?
                }
                else {
                    // validate the expired amount
                    List<CredentialCredit> creditGroup = credentialCreditRepository.findByIdGroup(loan.getIdGroup());
                    BigDecimal amountExpired = sumExpiredAmount(creditGroup);

                    Optional<ParameterConfiguration> config = parameterConfigurationRepository.findById(1L); // ese id asi no estaria muy bueno.
                    if (config.isPresent()) {
                        BigDecimal maxAmount = new BigDecimal(Float.toString(config.get().getExpiredAmountMax()));
                        if (amountExpired.compareTo(maxAmount) > 0 ){
                            int cyclesExpired = updateCredit.getAmountExpiredCycles();
                            updateCredit.setAmountExpiredCycles(++cyclesExpired);
                            credentialCreditRepository.save(updateCredit);
                            //TODO revoke group credit and benefits
                        }
                        else{
                            //if credit has no expired amount
                            // verify credential benefits
                            this.checkCredentialBenefitsToCreate(opBeneficiary.get());
                        }
                    }
                    else{
                        log.error("There is no configuration for getting the maximum expired amount.");
                        throw new NoExpiredConfigurationExists("There is no configuration for getting the maximum expired amount. Imposible to check the credential credit");
                    }
                }
            }
        }
        else {
            log.error("Person had been created and credential credit too, but person has been deleted eventually");
            throw new PersonDoesNotExists("Person had been created and credential credit too, but person has been deleted eventually");
        }
    }


    private void checkCredentialBenefitsToCreate(Person beneficiary) {
        //find the credential benefits of the person, active and holder type
        Optional<CredentialBenefits> existenceBenefits = credentialBenefitsRepository.findByDniBeneficiaryAndCredentialStateAndBeneficiaryType(beneficiary.getDocumentNumber(), new CredentialState(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode()), PersonTypesCodes.HOLDER.getCode());
        //create benefit if person do not have an active benefits holder
        if (existenceBenefits.isEmpty()){
            this.createNewBenefitsCredential(beneficiary, PersonTypesCodes.HOLDER);
        }
    }


    private BigDecimal sumExpiredAmount(List<CredentialCredit> group){
        BigDecimal amountExpired = BigDecimal.ZERO;

        for (CredentialCredit c: group) {
            BigDecimal value = new BigDecimal(Float.toString(c.getExpiredAmount()));
            amountExpired.add(value);
        }

        return amountExpired;
    }
    

}
