package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.bondarea.service.BondareaService;
import com.atixlabs.semillasmiddleware.app.didi.service.SyncDidiProcessService;
import com.atixlabs.semillasmiddleware.app.dto.ProcessResultDto;
import com.atixlabs.semillasmiddleware.app.exceptions.PersonDoesNotExistsException;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
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

    public static String OK = "OK";
    public static String ERROR = "error";

    public static String SYNCRO_LOANS = "syncro loans";
    public static String GENERATE_CREDIT_BENEFITS_CREDENTIAL = "generate credits and benefits credentials";
    public static String UPDATED_CREDENTIALS_ON_CHANGE_ID_DIDI = "update credentials on id didi change";
    public static String UPDATED_CREDENTIALS_ON_CHANGE_SANCOR_POLICY = "update credentials on Sancor Policy change";
    public static String EMMIT_ALL_ON_PENDING_DIDI = "emmit credentials on pending didi state";

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

        HashMap<String, String> result = new HashMap<String, String>();

        result.putAll(this.excecuteSyncroLoans());
        result.putAll(this.executeProcessGenerateCreditAndBenefitsCredentialsByLoans());
        result.putAll(this.executeUpdateCredentialOnIdDidiChangeProcess());
        result.putAll(this.executeProcessSancorPoliciesUpdated());
        result.putAll(this.executeEmmitAllCredentialsOnPendindDidiState());

        log.info("Executing ALL Process Finalized");

        return result;

    }

    public Map<String, String> excecuteSyncroLoans(){
        HashMap<String, String> result = new HashMap<String, String>();
        log.info("Execution of Bondarea Syncro Loans started...");
        try{
            this.bondareaService.synchronizeLoans();

            result.put(SYNCRO_LOANS,OK);
            log.info("Execution of Bondarea Syncro Loans finalized OK");

        } catch (InvalidProcessException e) {
            result.put(SYNCRO_LOANS,ERROR);
            log.error("Execution of Bondarea Syncro Loans finalized with errors ",e);
        }

        return result;
    }

    public Map<String, String> executeProcessGenerateCreditAndBenefitsCredentialsByLoans(){
        log.info("Execution of Generate Credit And Benefits Credentials By Loans started...");
        HashMap<String, String> result = new HashMap<String, String>();
        try{
            this.credentialService.generateCreditAndBenefitsCredentialsByLoans();
            result.put(GENERATE_CREDIT_BENEFITS_CREDENTIAL,OK);
            log.info("Execution of Generate Credit And Benefits Credentials By Loans finalized OK");

        } catch (InvalidProcessException e) {
            result.put(GENERATE_CREDIT_BENEFITS_CREDENTIAL,ERROR);
            log.error("Execution of Generate Credit And Benefits Credentials By Loans finalized with errors ",e);
        }

        return result;
    }

    public Map<String, String> executeUpdateCredentialOnIdDidiChangeProcess(){
        log.info("Execution of Update Credential On IdDidi Change Process started...");
        HashMap<String, String> result = new HashMap<String, String>();

            if(this.syncDidiProcessService.processNewsAppDidiUsers()) {
                result.put(UPDATED_CREDENTIALS_ON_CHANGE_ID_DIDI, OK);
                log.info("Execution of Update Credential On IdDidi Change Process finalized OK");
            }else{
            result.put(UPDATED_CREDENTIALS_ON_CHANGE_ID_DIDI,ERROR);
            log.error("Execution of Update Credential On IdDidi Change Process finalized with errors ");
        }

        return result;
    }

    public Map<String, String> executeProcessSancorPoliciesUpdated(){
        log.info("Execution of Update Credential On Sancor Policy Change Process started...");
        HashMap<String, String> result = new HashMap<String, String>();

        ProcessResultDto processResultDto = this.syncSancorProcessService.processSancorPoliciesUpdated();

        String message = String.format("of %d to process, %d ok %d with errors", processResultDto.getToProcess(), processResultDto.getProcessOk(), processResultDto.getProcessWithErrors());

        result.put(UPDATED_CREDENTIALS_ON_CHANGE_SANCOR_POLICY, message);
         log.info("Execution of Update Credential On Sancor Policy  Change Process finalized {}", message);

        return result;
    }

    public Map<String, String> executeEmmitAllCredentialsOnPendindDidiState(){
        log.info("Execution of Emmit All Credentials On PendindDidiState Process started...");
        HashMap<String, String> result = new HashMap<String, String>();

        Map<CredentialCategoriesCodes, String> resultEmmit = this.syncDidiProcessService.emmitAllCredentialsOnPendindDidiState();
        if(resultEmmit.isEmpty()){
            result.put(EMMIT_ALL_ON_PENDING_DIDI, OK);
            log.info("Execution of Execution of Emmit All Credentials On PendindDidiState finalized OK");
        }else{
            result.put(EMMIT_ALL_ON_PENDING_DIDI,ERROR);
            log.error("Execution of Execution of Emmit All Credentials On PendindDidiState finalized with errors ");
        }

        return result;
    }
}
