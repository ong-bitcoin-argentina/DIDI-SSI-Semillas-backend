package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.didi.service.DidiService;
import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.configuration.ParameterConfiguration;
import com.atixlabs.semillasmiddleware.app.model.configuration.constants.ConfigurationCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefits;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialTypesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.PersonTypesCodes;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.model.credentialState.constants.RevocationReasonsCodes;
import com.atixlabs.semillasmiddleware.app.repository.CredentialBenefitsRepository;
import com.atixlabs.semillasmiddleware.app.repository.CredentialRepository;
import com.atixlabs.semillasmiddleware.app.repository.ParameterConfigurationRepository;
import com.atixlabs.semillasmiddleware.app.repository.RevocationReasonRepository;
import com.atixlabs.semillasmiddleware.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CredentialBenefitService extends CredentialCommonService {

    private PersonService personService;

   // private CredentialStateService credentialStateService;

    private CredentialBenefitsRepository credentialBenefitsRepository;

    private ParameterConfigurationRepository parameterConfigurationRepository;

    public CredentialBenefitService(PersonService personService, CredentialStateService credentialStateService, CredentialBenefitsRepository credentialBenefitsRepository, ParameterConfigurationRepository parameterConfigurationRepository, DidiService didiService, RevocationReasonRepository revocationReasonRepository, CredentialRepository credentialRepository){
       super(credentialStateService, didiService, revocationReasonRepository, credentialRepository);
        this.personService = personService;
        //this.credentialStateService = credentialStateService;
        this.credentialBenefitsRepository = credentialBenefitsRepository;
        this.parameterConfigurationRepository = parameterConfigurationRepository;
    }


    /**
     * If holder exists and loan is active and not in default
     * <p>
     * * Create Benefit Credential for holder,  in state Pending Didi
     * * Create Benefits credentials for each kin, in Pending de Didi state
     * * else
     * * If holder exists as person, and loan is active and not in default
     * * If holder is not in default
     * *** Benefit Holder
     * *** If credential not exists, create credential in state Pending Didi
     * *** If exists and is active, do nothing
     * *** If exists and it is revoked, create a new one in Pending Didi status (is for finalize/cancelled loans)
     * *** Benefit Family
     * *** If credential not exists, create credential in state Pending Didi
     * *** If exists and is active, do nothing
     * *** If exists and it is revoked, create a new one in Pending Didi status (is for finalize/cancelled loans)
     * * If holder is in default
     * ** set credit for process in the next cycle
     * <p>
     * If holder not exists set credit for process in the next cycle
     *
     * @param loan
     */
    public void createCredentialsBenefitsForNewLoan(Loan loan) throws CredentialException {

        log.info(String.format("creating Benefits Credential for Loan %s", loan.getIdBondareaLoan()));

        Optional<Person> opHolder = personService.findByDocumentNumber(loan.getDniPerson());

        if (opHolder.isPresent()) {
            Person holder = opHolder.get();

            Optional<List<Person>> opFamiliy = personService.findFamilyForHolder(holder);

            if (!holder.isInDefault()) { // If holder is not in default

                Optional<CredentialBenefits> opCredentialBenefitsHolder = this.getCredentialBenefits(loan.getDniPerson(), loan.getDniPerson(), PersonTypesCodes.HOLDER );

                CredentialBenefits credentialBenefitsHolder = null;

                if (opCredentialBenefitsHolder.isPresent()) {

                    Optional<CredentialState> opStateRevoke = credentialStateService.getCredentialRevokeState();

                    credentialBenefitsHolder = opCredentialBenefitsHolder.get();

                    if (credentialBenefitsHolder.getCredentialState().equals(opStateRevoke.get())) {

                        //Holder
                        CredentialBenefits newCredentialBenefitsHolder = this.buildNewBenefitsCredential(holder, holder, PersonTypesCodes.HOLDER);
                        newCredentialBenefitsHolder.setIdHistorical(credentialBenefitsHolder.getIdHistorical());
                        this.saveCredentialBenefits(newCredentialBenefitsHolder);

                    } else { //credential is active or pending didi
                        log.info(String.format("Credential Benefit for holder %d of loan %s is in state %s, credential not created", holder.getDocumentNumber(), loan.getIdBondareaLoan(), credentialBenefitsHolder.getCredentialState().getStateName()));
                    }


                } else { //If credential not exists, create credential in state Pending Didi
                    //Holder
                    credentialBenefitsHolder = this.buildNewBenefitsCredential(holder, holder, PersonTypesCodes.HOLDER);
                    this.saveCredentialBenefits(credentialBenefitsHolder);
                }

                //Family
                if ((opFamiliy.isPresent()) && (!opFamiliy.get().isEmpty())) {
                    List<Person> family = opFamiliy.get();
                    for (Person beneficiary : family) {
                        this.handleCredentialBenefitsForBeneficiary(holder, beneficiary, loan);
                    }
                } else {
                    log.info(String.format("The holder %d has no family ", holder.getDocumentNumber()));
                }

            } else { //holder is in default
                String message = String.format("Credential Benefit for holder %d of loan %s is not created, holder is in default", holder.getDocumentNumber(), loan.getIdBondareaLoan());
                log.error(message);
                throw new CredentialException(message);
            }

        } else { //Holder not exists
            String message = String.format("Can't create Benefit Credential, Holder dni %d not exists", loan.getDateFirstInstalment());
            log.error(message);
            throw new CredentialException(message);
        }
    }


    /**
     * If holder is not on default
     *      Holder
     *          If credential exists
     *              If Credential is active and emmited, no nothing
     *      *                     Si la Credencial esta Pendiende de Didi, no hago nada
     *      *                     Si la Credencial esta revocada, creo una nueva credencial en estado pendiente de Didi
     *      *                 Familiar
     *      *                     Si la Crendencial desta Activa y Emitida, no hago nada
     *      *                     Si la Credencial esta Pendiende de Didi, no hago nada
     *      *                     Si la Credencial esta revocada, creo una nueva credencial en estado pendiente de Didi
     * @param loan
     * @param holder
     */
    public void updateCredentialBenefitForActiveLoan(Loan loan, Person holder) throws CredentialException {

        if(!holder.isInDefault()){

            Optional<CredentialBenefits> opCredentialBenefitsHolder = this.getCredentialBenefits(loan.getDniPerson(), loan.getDniPerson(), PersonTypesCodes.HOLDER );

            if(opCredentialBenefitsHolder.isPresent()){

                CredentialBenefits credentialBenefits = opCredentialBenefitsHolder.get();

                if(this.isCredentialRevoked(credentialBenefits)){
                    this.createCredentialsBenefitsForNewLoan(loan);
                }

            }

        }
    }

    /**
     * If holder is not in default
     * *             Benefit Holder
     * *                 If credential not exists, create credential in state Pending Didi
     * *                 If exists and is active, do nothing
     * *                 If exists and it is revoked, create a new one in Pending Didi status (is for finalize/cancelled loans)
     * *             Beneficio Familiar
     * *                 si no existe Creo credencial de beneficio en estado Pendiente de Didi
     * *                 si existe y está vigente no hago nada
     * *                 si existe y está revocada creó una nueva  la credencial como Pendiente de Didi
     *
     * @param holder
     * @param beneficiary
     * @throws CredentialException
     */
    private void handleCredentialBenefitsForBeneficiary(Person holder, Person beneficiary, Loan loan) throws CredentialException {

        Optional<CredentialBenefits> opCredentialBenefitsBeneficiary = this.getCredentialBenefits(holder.getDocumentNumber(), beneficiary.getDocumentNumber(), PersonTypesCodes.FAMILY);
        Optional<CredentialState> opStateRevoke = credentialStateService.getCredentialRevokeState();

        if (!opCredentialBenefitsBeneficiary.isPresent()) { //si no existe Creo credencial de beneficio en estado Pendiente de Didi

            CredentialBenefits credentialBenefits = this.buildNewBenefitsCredential(holder, beneficiary, PersonTypesCodes.FAMILY);
            this.saveCredentialBenefits(credentialBenefits);

        } else {

            CredentialBenefits credentialBenefitsBeneficiary = opCredentialBenefitsBeneficiary.get();

            if (credentialBenefitsBeneficiary.getCredentialState().equals(opStateRevoke.get())) {

                CredentialBenefits newCredentialBenefitsBeneficiary = this.buildNewBenefitsCredential(holder, holder, PersonTypesCodes.FAMILY);
                newCredentialBenefitsBeneficiary.setIdHistorical(credentialBenefitsBeneficiary.getIdHistorical());
                this.saveCredentialBenefits(newCredentialBenefitsBeneficiary);

            } else { //credential is active or pending didi

                log.info(String.format("Credential Benefit for beneficiary %d and holder %d of loan %s is in state %s, credential not created", beneficiary.getDocumentNumber(), holder.getDocumentNumber(), loan.getIdBondareaLoan(), credentialBenefitsBeneficiary.getCredentialState().getStateName()));

            }
        }
    }

    /**
     * *         Benefits Credential
     * *             Holder
     * *                 If exists, is active and emmited, do revoke,
     * *                 If exists and is Pending Didi, revoke localy
     * *                 If exists and is revoked, do nothing
     * *                 If not exists, do nothing
     *
     * @throws CredentialException
     */
    public void revokeHolderCredentialsBenefitsForLoan(Person holder) throws CredentialException {

        this.revokeCredentialsBenefitsForLoanInDefault(holder, holder,PersonTypesCodes.HOLDER);

    }

    /**
     * *         Benefits Credential
     * *             Familiy
     * *                 If exists, is active and emmited, do revoke,
     * *                 If exists and is Pending Didi, revoke localy
     * *                 If exists and is revoked, do nothing
     * *                 If not exists, do nothing
     *
     * @throws CredentialException
     */
    public void revokeFamilyCredentialsBenefitsForLoan(Person holder) throws CredentialException {

        Optional<List<Person>> opFamily = personService.findFamilyForHolder(holder);

        if(opFamily.isPresent() && (!opFamily.get().isEmpty())) {

            List<Person> family = opFamily.get();

            log.info(String.format("Found %d people as family for holder %d", family.size(), holder.getDocumentNumber()));

            for (Person beneficiary : family) {

                this.revokeCredentialsBenefitsForLoanInDefault(holder,beneficiary,PersonTypesCodes.FAMILY);

            }

        }else{
            log.info("Holder %d has no family, Beneficiaries Credential Benefits not created");
        }
    }

    /**
     * *         Benefits Credential
     * *                 If exists, is active and emmited, do revoke,
     * *                 If exists and is Pending Didi, revoke localy
     * *                 If exists and is revoked, do nothing
     * *                 If not exists, do nothing
     *
     * @throws CredentialException
     */
    public void revokeCredentialsBenefitsForLoanInDefault(Person holder, Person beneficiary, PersonTypesCodes personTypesCodes) throws CredentialException {

        log.info(String.format("Revoking Credential Benefits for Beneficiary %d and Holder %d credential type %s",beneficiary.getDocumentNumber(), holder.getDocumentNumber(), personTypesCodes.getCode()));

        Optional<CredentialBenefits>  opCredentialBenefits =  this.getCredentialBenefits(holder.getDocumentNumber(), beneficiary.getDocumentNumber(), personTypesCodes);

        if(opCredentialBenefits.isPresent()){

            CredentialBenefits credentialBenefits = opCredentialBenefits.get();

            if(!this.isCredentialRevoked(credentialBenefits)){

                this.revokeComplete(credentialBenefits, RevocationReasonsCodes.DEFAULT.getCode() );

            }else{
                log.info(String.format("Benefits Credential for holder %d its already Revoked", holder.getDocumentNumber()));
            }

        }else{
            log.info(String.format("Credential Benefits for Beneficiary %d and Holder %d credential type %s not exists", beneficiary.getDocumentNumber(),holder.getDocumentNumber(),personTypesCodes.getCode()));
        }

    }





    public Optional<CredentialBenefits> getCredentialBenefits(Long holderDni, Long beneficiaryDni, PersonTypesCodes personTypesCodes) {
        Optional<CredentialBenefits> opCredentialBenefitsHolder = credentialBenefitsRepository.findTopByCreditHolderDniAndBeneficiaryDniAndBeneficiaryTypeOrderByIdDesc(holderDni, beneficiaryDni, personTypesCodes.getCode());
        return opCredentialBenefitsHolder;
    }

    /**
     * Create Benefit credentila without Id Didi and pending didi
     *
     * @param beneficiary
     * @param personType
     * @return
     */
    public CredentialBenefits buildNewBenefitsCredential(Person holder, Person beneficiary, PersonTypesCodes personType) throws CredentialException {
        CredentialBenefits credentialBenefits = new CredentialBenefits();

        Optional<ParameterConfiguration> config = parameterConfigurationRepository.findByConfigurationName(ConfigurationCodes.ID_DIDI_ISSUER.getCode());

        if (config.isPresent()) {
            credentialBenefits.setIdDidiIssuer(config.get().getValue());
        } else {
            //
            throw new CredentialException("Id Didi Issuer Not exists, cant build credential");
        }

        Optional<CredentialState> opStatePendingDidi = credentialStateService.getCredentialPendingDidiState();
        credentialBenefits.setCredentialState(opStatePendingDidi.get());

        //Person is holder or family
        if (personType.equals(PersonTypesCodes.HOLDER)) {
            credentialBenefits.setBeneficiaryType(PersonTypesCodes.HOLDER.getCode());
            credentialBenefits.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_BENEFITS.getCode());
        } else {
            credentialBenefits.setBeneficiaryType(PersonTypesCodes.FAMILY.getCode());
            credentialBenefits.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_BENEFITS_FAMILY.getCode());
        }

        credentialBenefits.setDateOfIssue(DateUtil.getLocalDateTimeNow());
        credentialBenefits.setCredentialCategory(CredentialCategoriesCodes.BENEFIT.getCode());

        credentialBenefits.setCreditHolder(holder);

        credentialBenefits.setBeneficiary(beneficiary);

        return credentialBenefits;
    }


    public CredentialBenefits saveCredentialBenefits(CredentialBenefits credentialBenefits) {
        credentialBenefits = credentialBenefitsRepository.save(credentialBenefits);
        if (credentialBenefits.getIdHistorical() == null) {
            credentialBenefits.setIdHistorical(credentialBenefits.getId());
            credentialBenefits = credentialBenefitsRepository.save(credentialBenefits);

        }
        return credentialBenefits;
    }

    @Override
    protected Logger getLog() {
        return log;
    }
}
