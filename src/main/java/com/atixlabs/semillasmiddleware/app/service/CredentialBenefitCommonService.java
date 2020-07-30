package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.didi.service.DidiService;
import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;

import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefits;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.PersonTypesCodes;

import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
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
    public void createCredentialsBenefitsForNewLoan(Loan loan) throws CredentialException {

        this.getLog().info("creating Benefits Credential for Loan {}", loan.getIdBondareaLoan());

        Optional<Person> opHolder = personService.findByDocumentNumber(loan.getDniPerson());

        if (opHolder.isPresent()) {
            Person holder = opHolder.get();

            Optional<List<Person>> opFamiliy = personService.findFamilyForHolder(holder);

            if (!holder.isInDefault()) { // If holder is not in default

                Optional<T> opCredentialBenefitsHolder = this.getCredentialBenefits(loan.getDniPerson(), loan.getDniPerson(), PersonTypesCodes.HOLDER);

                T credentialBenefitsHolder = null;

                if (opCredentialBenefitsHolder.isPresent()) {

                    credentialBenefitsHolder = opCredentialBenefitsHolder.get();

                    if (this.isCredentialRevoked(credentialBenefitsHolder)) {

                        //Holder
                        T newCredentialBenefitsHolder = this.buildNewBenefitsCredential(holder, holder, PersonTypesCodes.HOLDER);
                        newCredentialBenefitsHolder.setIdHistorical(credentialBenefitsHolder.getIdHistorical());
                        this.saveCredentialBenefit(newCredentialBenefitsHolder);

                    } else { //credential is active or pending didi
                        this.getLog().info("Credential Benefit for holder {} of loan {} is in state {}, credential not created", holder.getDocumentNumber(), loan.getIdBondareaLoan(), credentialBenefitsHolder.getCredentialState().getStateName());
                    }


                } else { //If credential not exists, create credential in state Pending Didi
                    //Holder
                    credentialBenefitsHolder = this.buildNewBenefitsCredential(holder, holder, PersonTypesCodes.HOLDER);
                    this.saveCredentialBenefit(credentialBenefitsHolder);
                }

                //Family
                if ((opFamiliy.isPresent()) && (!opFamiliy.get().isEmpty())) {
                    List<Person> family = opFamiliy.get();
                    for (Person beneficiary : family) {
                        this.handleCredentialBenefitsForBeneficiary(holder, beneficiary, loan);
                    }
                } else {
                    this.getLog().info("The holder {} has no family ", holder.getDocumentNumber());
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

        Optional<T> opCredentialBenefitsBeneficiary = this.getCredentialBenefits(holder.getDocumentNumber(), beneficiary.getDocumentNumber(), PersonTypesCodes.FAMILY);

        Optional<CredentialState> opStateRevoke = credentialStateService.getCredentialRevokeState();

        if (!opCredentialBenefitsBeneficiary.isPresent()) { //si no existe Creo credencial de beneficio en estado Pendiente de Didi

            T credentialBenefits = this.buildNewBenefitsCredential(holder, beneficiary, PersonTypesCodes.FAMILY);
            this.saveCredentialBenefit(credentialBenefits);

        } else {

            T credentialBenefitsBeneficiary = opCredentialBenefitsBeneficiary.get();

            if (credentialBenefitsBeneficiary.getCredentialState().equals(opStateRevoke.get())) {

                T newCredentialBenefitsBeneficiary = this.buildNewBenefitsCredential(holder, holder, PersonTypesCodes.FAMILY);
                newCredentialBenefitsBeneficiary.setIdHistorical(credentialBenefitsBeneficiary.getIdHistorical());
                this.saveCredentialBenefit(newCredentialBenefitsBeneficiary);

            } else { //credential is active or pending didi

                this.getLog().info(String.format("Credential Benefit for beneficiary %d and holder %d of loan %s is in state %s, credential not created", beneficiary.getDocumentNumber(), holder.getDocumentNumber(), loan.getIdBondareaLoan(), credentialBenefitsBeneficiary.getCredentialState().getStateName()));

            }
        }
    }

    abstract Optional<T> getCredentialBenefits(Long holderDni, Long beneficiaryDni, PersonTypesCodes personTypesCodes);

    abstract T buildNewBenefitsCredential(Person holder, Person beneficiary, PersonTypesCodes personType) throws CredentialException;

    abstract T saveCredentialBenefit(T credential);

}
