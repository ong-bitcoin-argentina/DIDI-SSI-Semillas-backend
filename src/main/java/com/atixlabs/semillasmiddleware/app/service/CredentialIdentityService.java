package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.didi.model.DidiAppUser;
import com.atixlabs.semillasmiddleware.app.didi.service.DidiService;
import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefits;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialIdentity;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialRelationHolderType;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.repository.CredentialIdentityRepository;
import com.atixlabs.semillasmiddleware.app.repository.CredentialRepository;
import com.atixlabs.semillasmiddleware.app.repository.RevocationReasonRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    /*
    public Optional<CredentialIdentity> getCredentialIdentityActiveForDni(Long dni) throws CredentialException {
        Optional<CredentialState> activeDidiState = credentialStateService.getCredentialActiveState();

        return credentialIdentityRepository.findTopByCreditHolderDniAndCredentialStateOrderByDateOfIssueDesc(dni, activeDidiState.get());
    }*/

    /**
     * Abuelo Beneficiario en la encuesta, Flor Familiar
     * Se crean las Credenciales de identidad
     * 1- Titular  Id didi (Abuelo),  Dni Titular (Abuelo), DNI Beneficiario (Abuelo), Caracter (Titular)
     * 2-Familiar Id didi (Abuelo),  Dni Titular (Abuelo), DNI Beneficiario (Flor),  Caracter (Titular)
     *
     * Flor genera si id didi
     * Se crean las credenciales
     * 5-Familiar  Id didi (Flor),  Dni Titular (Abuelo), DNI Beneficiario (Flor),  Caracter (Familiar)
     * @param dni
     * @return
     */
    public List<CredentialIdentity> getAllCredentialIdentityActivesForDni(Long dni) throws CredentialException {
        List<CredentialIdentity> asHolder = this.getCredentialIdentityActivesForDniAndTypeHolder(dni);
        List<CredentialIdentity> asFamily = this.getCredentialIdentityActivesForDniAndTypeFamiliy(dni);

        List<CredentialIdentity> credentials = new ArrayList<CredentialIdentity>();

        if(asHolder != null)
            credentials.addAll(asHolder);

        if(asFamily != null)
            credentials.addAll(asFamily);

        return credentials;
    }

    public List<CredentialIdentity> getCredentialIdentityActivesForDniAndTypeFamiliy(Long dni) throws CredentialException {
        Optional<CredentialState> activeDidiState = credentialStateService.getCredentialActiveState();

        return credentialIdentityRepository.findByBeneficiaryDniAndCredentialStateAndRelationWithCreditHolder(dni, activeDidiState.get(), CredentialRelationHolderType.KINSMAN.getCode());
    }

    public List<CredentialIdentity> getCredentialIdentityActivesForDniAndTypeHolder(Long dni) throws CredentialException {
        Optional<CredentialState> activeDidiState = credentialStateService.getCredentialActiveState();

        return credentialIdentityRepository.findByCreditHolderDniAndCredentialStateAndRelationWithCreditHolder(dni, activeDidiState.get(), CredentialRelationHolderType.HOLDER.getCode());
    }


    public CredentialIdentity save(CredentialIdentity credentialIdentity){
        return credentialIdentityRepository.save(credentialIdentity);
    }

    public CredentialIdentity buildNewOnPendidgDidi(CredentialIdentity credentialIdentity, DidiAppUser newDidiAppUser) throws CredentialException {
        CredentialIdentity newCredentialIdentity =  new CredentialIdentity(credentialIdentity);
        newCredentialIdentity.setIdDidiReceptor(newDidiAppUser.getDid());

        this.resetStateOnPendingDidi(newCredentialIdentity);

        return newCredentialIdentity;
    }


    @Override
    protected Logger getLog() {
        return log;
    }
}
