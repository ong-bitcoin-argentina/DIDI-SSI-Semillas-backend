package com.atixlabs.semillasmiddleware.app.didi.controller;

import com.atixlabs.semillasmiddleware.app.didi.dto.DidiAppUserDto;
import com.atixlabs.semillasmiddleware.app.didi.service.DidiAppUserService;
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

    private DidiAppUserService didiAppUserService;
    private DidiService didiService;

    @Autowired
    public DidiController(DidiAppUserService didiAppUserService, DidiService didiService) {
        this.didiAppUserService = didiAppUserService;
        this.didiService = didiService;

    }


    @PostMapping("/didi")
    @ResponseStatus(HttpStatus.OK)
    public String registerNewDidiAppUser(@RequestBody DidiAppUserDto didiAppUserDto){
        return didiAppUserService.registerNewAppUser(didiAppUserDto);
    }

    //ONLY FOR TESTING
    @GetMapping("/didi/login")
    @ResponseStatus(HttpStatus.OK)
    public String authDidi() {
        return didiService.getAuthToken();
    }

    @GetMapping("/didi/sync")
    @ResponseStatus(HttpStatus.OK)
    public String didiCredentialSync() {

        return didiService.didiCredentialSync();
    }




}
