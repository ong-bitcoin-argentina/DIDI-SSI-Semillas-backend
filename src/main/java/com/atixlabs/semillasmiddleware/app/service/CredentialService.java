package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.repository.CredentialCreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CredentialService {

    @Autowired
    private CredentialCreditRepository credentialCreditRepository;

    public void addCredentialCredit(){
        CredentialCredit credentialCredit = new CredentialCredit();

        credentialCredit.setIdDidiIssueer(123L);
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
        credentialCredit.setDniBeneficiary(29302594L);

        credentialCreditRepository.save(credentialCredit);
    }
}
