package com.atixlabs.semillasmiddleware.app.didi.service;

import com.atixlabs.semillasmiddleware.app.didi.model.DidiAppUser;
import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.service.CredentialCreditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SyncDidiProcessService {

    private CredentialCreditService credentialCreditService;

    private DidiAppUserService didiAppUserService;

    private DidiService didiService;

    @Autowired
    public SyncDidiProcessService(CredentialCreditService credentialCreditService, DidiAppUserService didiAppUserService, DidiService didiService){
        this.credentialCreditService = credentialCreditService;
        this.didiAppUserService = didiAppUserService;
        this.didiService = didiService;
    }

    public void emmitCredentialCredits() throws CredentialException {

        List<CredentialCredit> credentialCreditsToEmmit = this.credentialCreditService.getCredentialCreditsOnPendindDidiState();

        if(credentialCreditsToEmmit==null || credentialCreditsToEmmit.isEmpty()){
            log.info("No credit credentials to emmit were found");
        }else{

            for(CredentialCredit credentialCredit : credentialCreditsToEmmit){
                this.emmitCredentialCredit(credentialCredit);
            }

        }
    }


    /**
     * get current Did for holder and emmit credential
     * @param credentialCredit
     */
    public void emmitCredentialCredit(CredentialCredit credentialCredit){

        DidiAppUser didiAppUser = this.didiAppUserService.getDidiAppUserByDni(credentialCredit.getCreditHolderDni());

        if(didiAppUser!=null) {
            credentialCredit.setIdDidiReceptor(didiAppUser.getDid());
            credentialCreditService.save(credentialCredit);

            didiService.createAndEmmitCertificateDidi(credentialCredit);

        }else{
            log.info("Id Didi for Holder {} not exist, Credential Credit for loan {} not emmited", credentialCredit.getCreditHolderDni(), credentialCredit.getIdBondareaCredit());
        }

    }


}
