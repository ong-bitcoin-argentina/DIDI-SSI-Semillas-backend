package com.atixlabs.semillasmiddleware.app.didi.controller;

import com.atixlabs.semillasmiddleware.app.didi.service.SyncDidiProcessService;
import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(SyncDidiProcessController.URL_MAPPING_CREDENTIAL)
@CrossOrigin(origins = "*")
@Slf4j
public class SyncDidiProcessController {

    public static final String URL_MAPPING_CREDENTIAL = "/syncdidi";

    private SyncDidiProcessService syncDidiProcessService;

    @Autowired
    public SyncDidiProcessController(SyncDidiProcessService syncDidiProcessService){
        this.syncDidiProcessService = syncDidiProcessService;
    }


    @GetMapping("/emmitcredentials")
    @ResponseStatus(HttpStatus.OK)
    public Map<CredentialCategoriesCodes, String> emmitCredentials(){
        return this.syncDidiProcessService.emmitAllCredentialsOnPendindDidiState();
    }

    @GetMapping("/emmitcredentialscredits")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> emmitCredentialsCredits(){
        Map<String, String> jsonMessage = new HashMap<>();

        try {
            this.syncDidiProcessService.emmitCredentialsCredit();
        } catch (CredentialException e) {
            log.error("ERROR emmiting credentials credits",e);
            jsonMessage.put("message", "ERROR "+e.getMessage());
        }

        jsonMessage.put("message", "Credentials Credit Emmited OK");
        return jsonMessage;
    }

    @GetMapping("/emmitcredentialsbenefits")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> emmitCredentialsBenefits(){
        Map<String, String> jsonMessage = new HashMap<>();

        try {
            this.syncDidiProcessService.emmitCredentialsBenefit(false);
            this.syncDidiProcessService.emmitCredentialsBenefit(true);
        } catch (CredentialException e) {
            log.error("ERROR emmiting credentials benefits",e);
            jsonMessage.put("message", "ERROR "+e.getMessage());
        }

        jsonMessage.put("message", "Credentials Benefits Emmited OK");
        return jsonMessage;
    }

    @GetMapping("/emmitcredentialsidentity")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> emmitCredentialsIdentity(){
        Map<String, String> jsonMessage = new HashMap<>();

        try {
            this.syncDidiProcessService.emmitCredentialsIdentity(false);
            this.syncDidiProcessService.emmitCredentialsIdentity(true);
        } catch (CredentialException e) {
            log.error("ERROR emmiting credentials identity",e);
            jsonMessage.put("message", "ERROR "+e.getMessage());
        }

        jsonMessage.put("message", "Credentials Emmited OK");
        return jsonMessage;
    }

    @GetMapping("/emmitcredentialsdwelling")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> emmitCredentialsDwelling(){
        Map<String, String> jsonMessage = new HashMap<>();

        try {
            this.syncDidiProcessService.emmitCredentialsDwelling();
        } catch (CredentialException e) {
            log.error("ERROR emmiting credentials Dwelling",e);
            jsonMessage.put("message", "ERROR "+e.getMessage());
        }

        jsonMessage.put("message", "Credentials Dwelling OK");
        return jsonMessage;
    }

    @GetMapping("/emmitcredentialsentrepreneurship")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> emmitCredentialsEntrepreneurship(){
        Map<String, String> jsonMessage = new HashMap<>();

        try {
            this.syncDidiProcessService.emmitCredentialsEntrepreneurship();
        } catch (CredentialException e) {
            log.error("ERROR emmiting credentials Entrepreneurship",e);
            jsonMessage.put("message", "ERROR "+e.getMessage());
        }

        jsonMessage.put("message", "Credentials Entrepreneurship OK");
        return jsonMessage;
    }



    @GetMapping("/process/new/appdidiusers")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> processNewsAppDidiUsers(){
        Map<String, String> jsonMessage = new HashMap<>();

        try {
            this.syncDidiProcessService.processNewsAppDidiUsers();
        } catch (Exception e) {
            log.error("ERROR process appdidiusers",e);
            jsonMessage.put("message", "ERROR "+e.getMessage());
        }

        jsonMessage.put("message", "update didi user processs ok OK");
        return jsonMessage;
    }
}
