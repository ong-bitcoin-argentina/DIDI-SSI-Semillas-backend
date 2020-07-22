package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.didi.service.DidiService;
import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialDwelling;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.repository.CredentialDwellingRepository;
import com.atixlabs.semillasmiddleware.app.repository.CredentialRepository;
import com.atixlabs.semillasmiddleware.app.repository.RevocationReasonRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CredentialDwellingService extends CredentialCommonService {

    private CredentialDwellingRepository credentialDwellingRepository;

    public CredentialDwellingService(CredentialStateService credentialStateService, DidiService didiService, RevocationReasonRepository revocationReasonRepository, CredentialRepository credentialRepository, CredentialDwellingRepository credentialDwellingRepository){
        super(credentialStateService, didiService, revocationReasonRepository, credentialRepository);
        this.credentialDwellingRepository = credentialDwellingRepository;
    }

    public List<CredentialDwelling> getCredentialDwellingOnPendindDidiState() throws CredentialException {
        CredentialState pendingDidiState = credentialStateService.getCredentialPendingDidiState();

        return  credentialDwellingRepository.findByCredentialState(pendingDidiState);
    }

    public CredentialDwelling save(CredentialDwelling credentialDwelling){
        return credentialDwellingRepository.save(credentialDwelling);
    }

    @Override
    protected Logger getLog() {
        return log;
    }
}
