package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.didi.service.DidiService;
import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefits;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialIdentity;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.repository.CredentialIdentityRepository;
import com.atixlabs.semillasmiddleware.app.repository.CredentialRepository;
import com.atixlabs.semillasmiddleware.app.repository.RevocationReasonRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CredentialIdentityService extends CredentialCommonService {

    private CredentialIdentityRepository credentialIdentityRepository;


    public CredentialIdentityService( CredentialStateService credentialStateService, DidiService didiService, RevocationReasonRepository revocationReasonRepository, CredentialRepository credentialRepository, CredentialIdentityRepository credentialIdentityRepository){
        super(credentialStateService, didiService, revocationReasonRepository, credentialRepository);
        this.credentialIdentityRepository = credentialIdentityRepository;
    }

    public List<CredentialIdentity> getCredentialIdentityOnPendindDidiState() throws CredentialException {
        CredentialState pendingDidiState = credentialStateService.getCredentialPendingDidiState();

        return  credentialIdentityRepository.findByCredentialState(pendingDidiState);
    }

    public CredentialIdentity save(CredentialIdentity credentialIdentity){
        return credentialIdentityRepository.save(credentialIdentity);
    }

    @Override
    protected Logger getLog() {
        return log;
    }
}
