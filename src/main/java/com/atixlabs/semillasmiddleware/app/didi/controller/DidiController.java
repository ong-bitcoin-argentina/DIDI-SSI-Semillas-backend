package com.atixlabs.semillasmiddleware.app.didi.controller;

import com.atixlabs.semillasmiddleware.app.didi.dto.DidiAppUser;
import com.atixlabs.semillasmiddleware.app.didi.dto.DidiCreateCredentialResponse;
import com.atixlabs.semillasmiddleware.app.didi.service.DidiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DidiController.URL_MAPPING_CREDENTIAL)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@Slf4j
public class DidiController {

    public static final String URL_MAPPING_CREDENTIAL = "/credentials";

    private DidiService didiService;

    @Autowired
    public DidiController(DidiService didiService) {
        this.didiService = didiService;
    }

    @GetMapping("/didi/login")
    @ResponseStatus(HttpStatus.OK)
    public String authDidi() {
        return didiService.getAuthToken();
    }

    @PostMapping("/didi")
    @ResponseStatus(HttpStatus.OK)
    public DidiCreateCredentialResponse updateDidiCredentialData(@RequestBody DidiAppUser didiAppUser){

        return didiService.createCredentialDidi(didiAppUser);
    }

}
