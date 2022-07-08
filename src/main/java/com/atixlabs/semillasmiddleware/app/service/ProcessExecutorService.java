package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.bondarea.service.BondareaService;
import com.atixlabs.semillasmiddleware.app.didi.service.SyncDidiProcessService;
import com.atixlabs.semillasmiddleware.app.dto.ProcessResultDto;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.processControl.exception.InvalidProcessException;
import com.atixlabs.semillasmiddleware.app.sancor.service.SyncSancorProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ProcessExecutorService {

    private BondareaService bondareaService;
    private CredentialService credentialService;
    private SyncDidiProcessService syncDidiProcessService;
    private SyncSancorProcessService syncSancorProcessService;

    @Autowired
    public ProcessExecutorService(BondareaService bondareaService, CredentialService credentialService, SyncDidiProcessService syncDidiProcessService,SyncSancorProcessService syncSancorProcessService){
        this.bondareaService = bondareaService;
        this.credentialService = credentialService;
        this.syncDidiProcessService = syncDidiProcessService;
        this.syncSancorProcessService = syncSancorProcessService;
    }

    public synchronized Map<String,String> execute(){
        log.info("Executing ALL Process....");

        HashMap<String, String> result = new HashMap<>();

        result.putAll(this.excecuteSyncroLoans());
        result.putAll(this.executeProcessGenerateCreditAndBenefitsCredentialsByLoans());
        result.putAll(this.executeUpdateCredentialOnIdDidiChangeProcess());
        result.putAll(this.executeProcessSancorPoliciesUpdated());
        result.putAll(this.executeEmmitAllCredentialsOnPendindDidiState());

        log.info("Executing ALL Process Finalized");

        return result;

    }

    public Map<String, String> excecuteSyncroLoans(){
        HashMap<String, String> result = new HashMap<>();
        log.info("Execution of Bondarea Syncro Loans started...");
        try{
            this.bondareaService.synchronizeLoans();

            result.put(Constants.SYNCRO_LOANS, Constants.OK);
            log.info("Execution of Bondarea Syncro Loans finalized OK");

        } catch (InvalidProcessException e) {
            result.put(Constants.SYNCRO_LOANS, Constants.ERROR);
            log.error("Execution of Bondarea Syncro Loans finalized with errors ",e);
        }

        return result;
    }

    public Map<String, String> executeProcessGenerateCreditAndBenefitsCredentialsByLoans(){
        log.info("Execution of Generate Credit And Benefits Credentials By Loans started...");
        HashMap<String, String> result = new HashMap<>();
        try{
            this.credentialService.generateCreditAndBenefitsCredentialsByLoans();
            result.put(Constants.GENERATE_CREDIT_BENEFITS_CREDENTIAL, Constants.OK);
            log.info("Execution of Generate Credit And Benefits Credentials By Loans finalized OK");

        } catch (InvalidProcessException e) {
            result.put(Constants.GENERATE_CREDIT_BENEFITS_CREDENTIAL, Constants.ERROR);
            log.error("Execution of Generate Credit And Benefits Credentials By Loans finalized with errors ",e);
        }

        return result;
    }

    public Map<String, String> executeUpdateCredentialOnIdDidiChangeProcess(){
        log.info("Execution of Update Credential On IdDidi Change Process started...");
        HashMap<String, String> result = new HashMap<>();

            if(Boolean.TRUE.equals(this.syncDidiProcessService.processNewsAppDidiUsers())) {
                result.put(Constants.UPDATED_CREDENTIALS_ON_CHANGE_ID_DIDI, Constants.OK);
                log.info("Execution of Update Credential On IdDidi Change Process finalized OK");
            }else{
            result.put(Constants.UPDATED_CREDENTIALS_ON_CHANGE_ID_DIDI, Constants.ERROR);
            log.error("Execution of Update Credential On IdDidi Change Process finalized with errors ");
        }

        return result;
    }

    public Map<String, String> executeProcessSancorPoliciesUpdated(){
        log.info("Execution of Update Credential On Sancor Policy Change Process started...");
        HashMap<String, String> result = new HashMap<>();

        ProcessResultDto processResultDto = this.syncSancorProcessService.processSancorPoliciesUpdated();

        String message = String.format("of %d to process, %d ok %d with errors", processResultDto.getToProcess(), processResultDto.getProcessOk(), processResultDto.getProcessWithErrors());

        result.put(Constants.UPDATED_CREDENTIALS_ON_CHANGE_SANCOR_POLICY, message);
         log.info("Execution of Update Credential On Sancor Policy  Change Process finalized {}", message);

        return result;
    }

    public Map<String, String> executeEmmitAllCredentialsOnPendindDidiState(){
        log.info("Execution of Emmit All Credentials On PendindDidiState Process started...");
        HashMap<String, String> result = new HashMap<>();

        Map<CredentialCategoriesCodes, String> resultEmmit = this.syncDidiProcessService.emmitAllCredentialsOnPendindDidiState();
        if(resultEmmit.isEmpty()){
            result.put(Constants.EMMIT_ALL_ON_PENDING_DIDI, Constants.OK);
            log.info("Execution of Execution of Emmit All Credentials On PendindDidiState finalized OK");
        }else{
            result.put(Constants.EMMIT_ALL_ON_PENDING_DIDI, Constants.ERROR);
            log.error("Execution of Execution of Emmit All Credentials On PendindDidiState finalized with errors ");
        }

        return result;
    }

    private class Constants{
        public static final String OK = "OK";
        public static final String ERROR = "error";

        public static final String SYNCRO_LOANS = "syncro loans";
        public static final String GENERATE_CREDIT_BENEFITS_CREDENTIAL = "generate credits and benefits credentials";
        public static final String UPDATED_CREDENTIALS_ON_CHANGE_ID_DIDI = "update credentials on id didi change";
        public static final String UPDATED_CREDENTIALS_ON_CHANGE_SANCOR_POLICY = "update credentials on Sancor Policy change";
        public static final String EMMIT_ALL_ON_PENDING_DIDI = "emmit credentials on pending didi state";
    }
}
