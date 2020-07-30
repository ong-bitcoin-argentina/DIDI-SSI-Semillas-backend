package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.didi.service.DidiService;
import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.configuration.ParameterConfiguration;
import com.atixlabs.semillasmiddleware.app.model.configuration.constants.ConfigurationCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefitSancor;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialTypesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.PersonTypesCodes;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.repository.CredentialBenefitSancorRepository;
import com.atixlabs.semillasmiddleware.app.repository.CredentialRepository;
import com.atixlabs.semillasmiddleware.app.repository.ParameterConfigurationRepository;
import com.atixlabs.semillasmiddleware.app.repository.RevocationReasonRepository;
import com.atixlabs.semillasmiddleware.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CredentialBenefitSancorService extends CredentialBenefitCommonService<CredentialBenefitSancor> {

    private CredentialBenefitSancorRepository credentialBenefitSancorRepository;


    private ParameterConfigurationRepository parameterConfigurationRepository;

    @Autowired
    public CredentialBenefitSancorService(PersonService personService, ParameterConfigurationRepository parameterConfigurationRepository, CredentialStateService credentialStateService, CredentialBenefitSancorRepository credentialBenefitSancorRepository, DidiService didiService, RevocationReasonRepository revocationReasonRepository, CredentialRepository credentialRepository){
        super(personService,credentialStateService, didiService, revocationReasonRepository, credentialRepository);
        this.credentialBenefitSancorRepository = credentialBenefitSancorRepository;
        this.parameterConfigurationRepository = parameterConfigurationRepository;

    }


    @Override
    public Optional<CredentialBenefitSancor> getCredentialBenefits(Long holderDni, Long beneficiaryDni, PersonTypesCodes personTypesCodes) {
        return credentialBenefitSancorRepository.findTopByCreditHolderDniAndBeneficiaryDniAndBeneficiaryTypeOrderByIdDesc(holderDni, beneficiaryDni, personTypesCodes.getCode());
    }


    /**
     * Create Benefit Sancor credential without Id Didi and pending didi
     *
     * @param beneficiary
     * @param personType
     * @return
     */
    @Override
    public CredentialBenefitSancor buildNewBenefitsCredential(Person holder, Person beneficiary, PersonTypesCodes personType) throws CredentialException {
        CredentialBenefitSancor credentialBenefitSancor = new CredentialBenefitSancor();

        Optional<ParameterConfiguration> config = parameterConfigurationRepository.findByConfigurationName(ConfigurationCodes.ID_DIDI_ISSUER.getCode());

        if (config.isPresent()) {
            credentialBenefitSancor.setIdDidiIssuer(config.get().getValue());
        } else {
            //
            throw new CredentialException("Id Didi Issuer Not exists, cant build credential");
        }

        CredentialState statePendingDidi = credentialStateService.getCredentialPendingDidiState();
        credentialBenefitSancor.setCredentialState(statePendingDidi);

        credentialBenefitSancor.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_BENEFITS_SANCOR.getCode());
        //Person is holder or family
        /*if (personType.equals(PersonTypesCodes.HOLDER)) {
            credentialBenefits.setBeneficiaryType(PersonTypesCodes.HOLDER.getCode());
            credentialBenefits.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_BENEFITS.getCode());
        } else {
            credentialBenefits.setBeneficiaryType(PersonTypesCodes.FAMILY.getCode());
            credentialBenefits.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_BENEFITS_FAMILY.getCode());
        }*/

        credentialBenefitSancor.setDateOfIssue(DateUtil.getLocalDateTimeNow());
        credentialBenefitSancor.setCredentialCategory(CredentialCategoriesCodes.BENEFIT_SANCOR.getCode());

        credentialBenefitSancor.setCreditHolder(holder);

        credentialBenefitSancor.setBeneficiary(beneficiary);

        return credentialBenefitSancor;
    }

    @Override
    public CredentialBenefitSancor saveCredentialBenefit(CredentialBenefitSancor credential) {
        credential = credentialBenefitSancorRepository.save(credential);
        if (credential.getIdHistorical() == null) {
            credential.setIdHistorical(credential.getId());
            credential = credentialBenefitSancorRepository.save(credential);

        }
        return credential;
    }

    @Override
    protected Logger getLog() {
        return log;
    }
}
