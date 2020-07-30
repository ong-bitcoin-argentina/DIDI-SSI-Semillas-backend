package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.didi.service.DidiService;
import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.configuration.ParameterConfiguration;
import com.atixlabs.semillasmiddleware.app.model.configuration.constants.ConfigurationCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefitSancor;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefits;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
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
public class CredentialBenefitService extends CredentialBenefitCommonService<CredentialBenefits> {

    //private PersonService personService;

   // private CredentialStateService credentialStateService;

    private CredentialBenefitsRepository credentialBenefitsRepository;

    private ParameterConfigurationRepository parameterConfigurationRepository;

    public CredentialBenefitService(PersonService personService, CredentialStateService credentialStateService, CredentialBenefitsRepository credentialBenefitsRepository, ParameterConfigurationRepository parameterConfigurationRepository, DidiService didiService, RevocationReasonRepository revocationReasonRepository, CredentialRepository credentialRepository){
       super(personService, credentialStateService, didiService, revocationReasonRepository, credentialRepository);
        //this.personService = personService;
        //this.credentialStateService = credentialStateService;
        this.credentialBenefitsRepository = credentialBenefitsRepository;
        this.parameterConfigurationRepository = parameterConfigurationRepository;
    }



 //TODO eliminar
    /**
     * If holder is not on default
     *      Holder
     *          If credential exists
     *              If Credential is active and emmited, do nothing
     *              If Credential is Pending DIDI, no nothing
     *              If Credential is revoked Create new Credential
     *      Kinsman
     *      If credential exists
     *      *              If Credential is active and emmited, do nothing
     *      *              If Credential is Pending DIDI, no nothing
     *      *              If Credential is revoked Create new Credential

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

                Optional<CredentialBenefits> credentialBenefit = this.getFamiliyCredentialBenefit(holder, beneficiary);
                this.revokeCredentialsBenefitsForLoanInDefault(holder,beneficiary,credentialBenefit);

            }

        }else{
            log.info("Holder %d has no family, Beneficiaries Credential Benefits not created");
        }
    }

    @Override
    public Optional<CredentialBenefits> getHolderCredentialBenefit(Person holder){
        return this.getCredentialBenefits(holder.getDocumentNumber(), holder.getDocumentNumber(), PersonTypesCodes.HOLDER);
    }

    public Optional<CredentialBenefits> getFamiliyCredentialBenefit(Person holder, Person beneficiary){
        return this.getCredentialBenefits(holder.getDocumentNumber(), beneficiary.getDocumentNumber(), PersonTypesCodes.FAMILY);
    }


    @Override
    public Optional<CredentialBenefits> getCredentialBenefitsHolder(Long holderDni){
        return this.getCredentialBenefits(holderDni , holderDni, PersonTypesCodes.HOLDER);
    }

    @Override
    public Optional<CredentialBenefits> getCredentialBenefitsFamiliy(Long holderDni, Long beneficiary){
        return this.getCredentialBenefits(holderDni , beneficiary, PersonTypesCodes.FAMILY);
    }


    public Optional<CredentialBenefits> getCredentialBenefits(Long holderDni, Long beneficiaryDni, PersonTypesCodes personTypesCodes) {
        Optional<CredentialBenefits> opCredentialBenefitsHolder = credentialBenefitsRepository.findTopByCreditHolderDniAndBeneficiaryDniAndBeneficiaryTypeOrderByIdDesc(holderDni, beneficiaryDni, personTypesCodes.getCode());
        return opCredentialBenefitsHolder;
    }


    @Override
    public CredentialBenefits buildNewHolderBenefitsCredential(Person holder) throws CredentialException{
         return this.buildNewBenefitsCredential(holder, holder, PersonTypesCodes.HOLDER);
    }

    @Override
    public CredentialBenefits buildNewFamiliyBenefitsCredential(Person holder, Person beneficiary) throws CredentialException{
        return this.buildNewBenefitsCredential(holder, beneficiary, PersonTypesCodes.HOLDER);
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

        CredentialState statePendingDidi = credentialStateService.getCredentialPendingDidiState();
        credentialBenefits.setCredentialState(statePendingDidi);

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


    @Override
    public CredentialBenefits saveCredentialBenefit(CredentialBenefits credentialBenefits) {
        credentialBenefits = credentialBenefitsRepository.save(credentialBenefits);
        if (credentialBenefits.getIdHistorical() == null) {
            credentialBenefits.setIdHistorical(credentialBenefits.getId());
            credentialBenefits = credentialBenefitsRepository.save(credentialBenefits);

        }
        return credentialBenefits;
    }

    public List<CredentialBenefits> getCredentialBenefitsOnPendindDidiState() throws CredentialException {
        CredentialState pendingDidiState = credentialStateService.getCredentialPendingDidiState();

        return  credentialBenefitsRepository.findByCredentialState(pendingDidiState);
    }

    public CredentialBenefits save(CredentialBenefits credentialBenefits){
        return credentialBenefitsRepository.save(credentialBenefits);
    }

    @Override
    protected Logger getLog() {
        return log;
    }
}
