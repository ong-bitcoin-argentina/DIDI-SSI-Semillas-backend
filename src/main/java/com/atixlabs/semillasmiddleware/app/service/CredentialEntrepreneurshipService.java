package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.didi.model.DidiAppUser;
import com.atixlabs.semillasmiddleware.app.didi.service.DidiService;
import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialDwelling;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialEntrepreneurship;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.repository.CredentialDwellingRepository;
import com.atixlabs.semillasmiddleware.app.repository.CredentialEntrepreneurshipRepository;
import com.atixlabs.semillasmiddleware.app.repository.CredentialRepository;
import com.atixlabs.semillasmiddleware.app.repository.RevocationReasonRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class CredentialEntrepreneurshipService extends CredentialCommonService {

    private CredentialEntrepreneurshipRepository credentialEntrepreneurshipRepository;

    public CredentialEntrepreneurshipService(CredentialStateService credentialStateService, DidiService didiService, RevocationReasonRepository revocationReasonRepository, CredentialRepository credentialRepository, CredentialEntrepreneurshipRepository credentialEntrepreneurshipRepository){
        super(credentialStateService, didiService, revocationReasonRepository, credentialRepository);
        this.credentialEntrepreneurshipRepository = credentialEntrepreneurshipRepository;
    }

    public List<CredentialEntrepreneurship> getCredentialEntrepreneurshipOnPendindDidiState() throws CredentialException {
        CredentialState pendingDidiState = credentialStateService.getCredentialPendingDidiState();

        return  credentialEntrepreneurshipRepository.findByCredentialState(pendingDidiState);
    }

    public CredentialEntrepreneurship save(CredentialEntrepreneurship credentialEntrepreneurship){
        return credentialEntrepreneurshipRepository.save(credentialEntrepreneurship);
    }

    public List<CredentialEntrepreneurship> getCredentialEntrepreneurshipActiveForDni(Long dni) throws CredentialException {
        Optional<CredentialState> activeDidiState = credentialStateService.getCredentialActiveState();

        return credentialEntrepreneurshipRepository.findByCreditHolderDniAndCredentialState(dni, activeDidiState.get());
    }

    public CredentialEntrepreneurship buildNewOnPendidgDidi(CredentialEntrepreneurship credentialEntrepreneurship, DidiAppUser newDidiAppUser) throws CredentialException {
        CredentialEntrepreneurship newCredentialEntrepreneurship =  new CredentialEntrepreneurship(credentialEntrepreneurship);
        newCredentialEntrepreneurship.setIdDidiReceptor(newDidiAppUser.getDid());

        this.resetStateOnPendingDidi(newCredentialEntrepreneurship);

        return newCredentialEntrepreneurship;
    }

    @Override
    protected Logger getLog() {
        return log;
    }
}