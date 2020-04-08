package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.repository.CredentialCreditRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class CredentialService {

    @Autowired
    private CredentialCreditRepository credentialCreditRepository;

    public void addCredentialCredit(){
        CredentialCredit credentialCredit = new CredentialCredit();

        credentialCredit.setId(1L);

        credentialCredit.setIdDidiIssuer(123L);
        credentialCredit.setIdDidiReceptor(234L);
        credentialCredit.setIdDidiCredential(456L);
        credentialCredit.setIdHistorical(77L);
        credentialCredit.setDateOfExpiry(LocalDateTime.now());
        credentialCredit.setIdRelatedCredential(534L);

        credentialCredit.setIdCredit(1L);
        credentialCredit.setCreditName("credit name");
        credentialCredit.setIdGroup(1111L);
        credentialCredit.setGroupName("GroupName");
        credentialCredit.setRol("rol");
        credentialCredit.setAmount(1d);
        credentialCredit.setCurrentCycle("Cycle");
        credentialCredit.setCreditState("state");
        credentialCredit.setDniBeneficiary(29302594L);

        log.info("addCredentialCredit");
        log.info(credentialCredit.toString());
        credentialCreditRepository.save(credentialCredit);
    }

    public void addCredentialCreditFromExcel(CredentialCredit credentialCredit){
        //CredentialCredit credentialCredit = new CredentialCredit();

        log.info("addCredentialCreditFromExcel");

        credentialCredit.setIdDidiIssuer(1L);
        credentialCredit.setIdDidiReceptor(234L);
        credentialCredit.setIdDidiCredential(456L);
        credentialCredit.setIdHistorical(77L);
        credentialCredit.setDateOfExpiry(LocalDateTime.now());
        credentialCredit.setIdRelatedCredential(534L);

        credentialCredit.setIdCredit(222L);
        credentialCredit.setCreditName("credit name");
        credentialCredit.setIdGroup(1111L);
        credentialCredit.setGroupName("GroupName");
        credentialCredit.setRol("rol");
        credentialCredit.setAmount(1d);
        credentialCredit.setCurrentCycle("Cycle");
        credentialCredit.setCreditState("state");
        //credentialCredit.setDniBeneficiary(29302594L);//ya viene

        log.info(credentialCredit.toString());
        credentialCreditRepository.save(credentialCredit);
    }


}
