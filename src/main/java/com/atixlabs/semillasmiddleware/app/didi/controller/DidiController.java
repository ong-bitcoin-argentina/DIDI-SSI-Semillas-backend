package com.atixlabs.semillasmiddleware.app.didi.controller;

import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.bondarea.service.LoanService;
import com.atixlabs.semillasmiddleware.app.didi.dto.DidiAuthResponse;
import com.atixlabs.semillasmiddleware.app.didi.service.DidiService;
import com.atixlabs.semillasmiddleware.app.dto.CredentialDto;
import com.atixlabs.semillasmiddleware.app.exceptions.NoExpiredConfigurationExists;
import com.atixlabs.semillasmiddleware.app.exceptions.PersonDoesNotExists;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialTypesCodes;
import com.atixlabs.semillasmiddleware.app.service.CredentialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(DidiController.URL_MAPPING_CREDENTIAL)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@Slf4j
public class DidiController {

    public static final String URL_MAPPING_CREDENTIAL = "/credentials";

    private DidiService didiService;

    @Autowired
    public DidiController(CredentialService credentialService, LoanService loanService, DidiService didiService) {
        this.didiService = didiService;
    }

    @GetMapping("/didi")
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<Credential> synchronizeDidi() {
        return didiService.createCredentialDidi();
    }

    @GetMapping("/didi/login")
    @ResponseStatus(HttpStatus.OK)
    public DidiAuthResponse authDidi() {
        return didiService.getAuthTokenSync();
    }

}
