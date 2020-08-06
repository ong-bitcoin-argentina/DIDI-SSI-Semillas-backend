package com.atixlabs.semillasmiddleware.app.sancor.service;

import com.atixlabs.semillasmiddleware.app.didi.service.DidiService;
import com.atixlabs.semillasmiddleware.app.didi.service.SyncDidiProcessService;
import com.atixlabs.semillasmiddleware.app.dto.ProcessResultDto;
import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefitSancor;
import com.atixlabs.semillasmiddleware.app.model.credentialState.constants.RevocationReasonsCodes;
import com.atixlabs.semillasmiddleware.app.sancor.model.SancorPolicy;
import com.atixlabs.semillasmiddleware.app.service.CredentialBenefitSancorService;
import com.atixlabs.semillasmiddleware.app.service.CredentialService;
import com.atixlabs.semillasmiddleware.app.service.CredentialStateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SyncSancorProcessService {

    private SancorPolicyService sancorPolicyService;

    private CredentialBenefitSancorService credentialBenefitSancorService;

    private CredentialStateService credentialStateService;

    private DidiService didiService;

    private CredentialService credentialService;

    @Autowired
    public SyncSancorProcessService(SancorPolicyService sancorPolicyService, CredentialBenefitSancorService credentialBenefitSancorService,CredentialStateService credentialStateService, DidiService didiService, CredentialService credentialService){
        this.sancorPolicyService = sancorPolicyService;
        this.credentialBenefitSancorService = credentialBenefitSancorService;
        this.credentialStateService = credentialStateService;
        this.didiService = didiService;
        this.credentialService = credentialService;
    }

    public ProcessResultDto processSancorPoliciesUpdated(){

        ProcessResultDto processResultDto = new ProcessResultDto();

        List<SancorPolicy> sancorPoliciesThatNeedReview = sancorPolicyService.getSancorPoliciesThatNeedReview();

        log.info(" {} sancor policies need review",sancorPoliciesThatNeedReview.size());

        processResultDto.setToProcess(Long.valueOf(sancorPoliciesThatNeedReview.size()));

        for (SancorPolicy sancorPolicy : sancorPoliciesThatNeedReview){
            try {
                Optional<CredentialBenefitSancor> opCredentialBenefitSancor = credentialBenefitSancorService.getCredentialBenefitsHolder(sancorPolicy.getCertificateClientDni());
                if (opCredentialBenefitSancor.isPresent()) {

                    if (credentialBenefitSancorService.isCredentialActive(opCredentialBenefitSancor.get())) {
                        this.credentialService.revokeComplete(opCredentialBenefitSancor.get(), RevocationReasonsCodes.UPDATE_INTERNAL);
                        CredentialBenefitSancor newCredentialBenefitSancor =  this.credentialBenefitSancorService.buildHolderBenefitsCredential(opCredentialBenefitSancor.get(), Optional.of(sancorPolicy));
                        this.credentialBenefitSancorService.save(newCredentialBenefitSancor);
                    }else{
                        if(credentialBenefitSancorService.isCredentialPendingDidi(opCredentialBenefitSancor.get())){
                            opCredentialBenefitSancor.get().addPolicyData(sancorPolicy);
                            this.credentialBenefitSancorService.save(opCredentialBenefitSancor.get());
                        }
                    }

                }

                sancorPolicy.setNeedReview(false);
                this.sancorPolicyService.save(sancorPolicy);
                log.info("sancor policy for dni {} updated ",sancorPolicy.getCertificateClientDni());
                processResultDto.addProcessOk();

            } catch (CredentialException e) {
                log.error("Error process sancor policies that need review dni {}",sancorPolicy.getCertificateClientDni(),e);
                processResultDto.addProcessWithErrors();
            }
        }
        return processResultDto;
    }

}
