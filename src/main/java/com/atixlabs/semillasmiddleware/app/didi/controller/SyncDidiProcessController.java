package com.atixlabs.semillasmiddleware.app.didi.controller;

import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.didi.service.SyncDidiProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(SyncDidiProcessController.URL_MAPPING_CREDENTIAL)
@CrossOrigin(origins = {"http://localhost:8080", "${didi.server.url}"}, methods= {RequestMethod.GET,RequestMethod.POST})
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
            jsonMessage.put(Constants.MSG, Constants.ERROR+e.getMessage());
        }

        jsonMessage.put(Constants.MSG, "Credentials Credit Emmited OK");
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
            jsonMessage.put(Constants.MSG, Constants.ERROR+e.getMessage());
        }

        jsonMessage.put(Constants.MSG, "Credentials Benefits Emmited OK");
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
            jsonMessage.put(Constants.MSG, Constants.ERROR+e.getMessage());
        }

        jsonMessage.put(Constants.MSG, "Credentials Emmited OK");
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
            jsonMessage.put(Constants.MSG, Constants.ERROR+e.getMessage());
        }

        jsonMessage.put(Constants.MSG, "Credentials Dwelling OK");
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
            jsonMessage.put(Constants.MSG, Constants.ERROR+e.getMessage());
        }

        jsonMessage.put(Constants.MSG, "Credentials Entrepreneurship OK");
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
            jsonMessage.put(Constants.MSG, Constants.ERROR+e.getMessage());
        }

        jsonMessage.put(Constants.MSG, "update didi user processs ok OK");
        return jsonMessage;
    }

    private static class Constants{
        public static final String MSG = "mensaje";
        public static final String ERROR = "ERROR ";
    }
}
