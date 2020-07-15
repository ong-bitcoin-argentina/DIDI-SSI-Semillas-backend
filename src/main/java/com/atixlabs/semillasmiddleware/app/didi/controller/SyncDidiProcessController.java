package com.atixlabs.semillasmiddleware.app.didi.controller;

import com.atixlabs.semillasmiddleware.app.didi.model.constant.DidiAppUserOperationResult;
import com.atixlabs.semillasmiddleware.app.didi.service.SyncDidiProcessService;
import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(SyncDidiProcessController.URL_MAPPING_CREDENTIAL)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@Slf4j
public class SyncDidiProcessController {

    public static final String URL_MAPPING_CREDENTIAL = "/syncdidi";

    private SyncDidiProcessService syncDidiProcessService;

    @Autowired
    public SyncDidiProcessController(SyncDidiProcessService syncDidiProcessService){
        this.syncDidiProcessService = syncDidiProcessService;
    }

    @GetMapping("/emmitcredentialscredits")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> emmitCredentialsCredits(){
        Map<String, String> jsonMessage = new HashMap<>();

        try {
            this.syncDidiProcessService.emmitCredentialCredits();
        } catch (CredentialException e) {
            log.error("ERROR emmiting credentials credits",e);
            jsonMessage.put("message", "ERROR "+e.getMessage());
        }

        jsonMessage.put("message", "Credentials Credit Emmited OK");
        return jsonMessage;
    }
}
