package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.didi.service.DidiService;
import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefits;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.PersonTypesCodes;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.model.credentialState.constants.RevocationReasonsCodes;
import com.atixlabs.semillasmiddleware.app.repository.CredentialRepository;
import com.atixlabs.semillasmiddleware.app.repository.RevocationReasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public abstract class CredentialBenefitCommonService<T extends Credential> extends CredentialCommonService {

    protected PersonService personService;

    @Autowired
    public CredentialBenefitCommonService(PersonService personService, CredentialStateService credentialStateService, DidiService didiService, RevocationReasonRepository revocationReasonRepository, CredentialRepository credentialRepository) {
        super(credentialStateService, didiService, revocationReasonRepository, credentialRepository);

        this.personService = personService;

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
    public void createCredentialsBenefitsHolderForNewLoan(Loan loan) throws CredentialException {

        this.getLog().info("creating Benefits Credential for Loan {}", loan.getIdBondareaLoan());

        Optional<Person> opHolder = personService.findByDocumentNumber(loan.getDniPerson());

        if (opHolder.isPresent()) {
            Person holder = opHolder.get();

            if (!holder.isInDefault()) { // If holder is not in default

                Optional<T> opCredentialBenefitsHolder = this.getCredentialBenefitsHolder(loan.getDniPerson());

                T credentialBenefitsHolder = null;

                if (opCredentialBenefitsHolder.isPresent()) {

                    credentialBenefitsHolder = opCredentialBenefitsHolder.get();

                    if (this.isCredentialRevoked(credentialBenefitsHolder)) {

                        //Holder
                        T newCredentialBenefitsHolder = this.buildNewHolderBenefitsCredential(holder);
                        newCredentialBenefitsHolder.setIdHistorical(credentialBenefitsHolder.getIdHistorical());
                        this.saveCredentialBenefit(newCredentialBenefitsHolder);

                    } else { //credential is active or pending didi
                        this.getLog().info("Credential Benefit for holder {} of loan {} is in state {}, credential not created", holder.getDocumentNumber(), loan.getIdBondareaLoan(), credentialBenefitsHolder.getCredentialState().getStateName());
                    }


                } else { //If credential not exists, create credential in state Pending Didi
                    //Holder
                    credentialBenefitsHolder = this.buildNewHolderBenefitsCredential(holder);
                    this.saveCredentialBenefit(credentialBenefitsHolder);
                }


            } else { //holder is in default
                String message = String.format("Credential Benefit for holder %d of loan %s is not created, holder is in default", holder.getDocumentNumber(), loan.getIdBondareaLoan());
                this.getLog().error(message);
                throw new CredentialException(message);
            }

        } else { //Holder not exists
            String message = String.format("Can't create Benefit Credential, Holder dni %d not exists", loan.getDateFirstInstalment());
            this.getLog().error(message);
            throw new CredentialException(message);
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

        Optional<T> opCredentialBenefits = this.getHolderCredentialBenefit(holder);

        this.revokeCredentialsBenefitsForLoanInDefault(holder, holder, opCredentialBenefits);

    }

    abstract Optional<T> getHolderCredentialBenefit(Person holder);

    /**
     * *         Benefits Credential
     * *                 If exists, is active and emmited, do revoke,
     * *                 If exists and is Pending Didi, revoke localy
     * *                 If exists and is revoked, do nothing
     * *                 If not exists, do nothing
     *
     * @throws CredentialException
     */
    public void revokeCredentialsBenefitsForLoanInDefault(Person holder, Person beneficiary, Optional<T> opCredentialBenefits) throws CredentialException {

        this.getLog().info("Revoking Credential Benefits for Beneficiary {} and Holder {} ", beneficiary.getDocumentNumber(), holder.getDocumentNumber());

        if (opCredentialBenefits.isPresent()) {

            T credentialBenefits = opCredentialBenefits.get();

            if (!this.isCredentialRevoked(credentialBenefits)) {

                this.revokeComplete(credentialBenefits, RevocationReasonsCodes.DEFAULT.getCode());

            } else {
                this.getLog().info("Benefits Credential for holder {} its already Revoked", holder.getDocumentNumber());
            }

        } else {
            this.getLog().info("Credential Benefits for Beneficiary {} and Holder {} credential not exists", beneficiary.getDocumentNumber(), holder.getDocumentNumber());
        }

    }


    /**
     * If holder is not on default
     * Holder
     * If credential exists
     * If Credential is active and emmited, do nothing
     * If Credential is Pending DIDI, no nothing
     * If Credential is revoked Create new Credential
     *
     * @param loan
     * @param holder
     */
    public void updateCredentialBenefitForActiveLoan(Loan loan, Person holder) throws CredentialException {

        if (!holder.isInDefault()) {

            Optional<T> opCredentialBenefitsHolder = this.getCredentialBenefitsHolder(loan.getDniPerson());

            if (opCredentialBenefitsHolder.isPresent()) {

                T credentialBenefits = opCredentialBenefitsHolder.get();

                //State of Credential benefit family depends of credential benefit holder, must be the same state, only control holder state
                if (this.isCredentialRevoked(credentialBenefits)) {
                    this.createCredentialsBenefitsHolderForNewLoan(loan);
                }

            }

        }
    }

    abstract Optional<T> getCredentialBenefitsHolder(Long holderDni);

   // abstract Optional<T> getCredentialBenefitsFamiliy(Long holderDni, Long beneficiary);

    abstract T buildNewHolderBenefitsCredential(Person holder) throws CredentialException;

   // abstract T buildNewFamiliyBenefitsCredential(Person holder, Person beneficiary) throws CredentialException;

    abstract T saveCredentialBenefit(T credential);

}
