package com.atixlabs.semillasmiddleware.app.controller;

import com.atixlabs.semillasmiddleware.app.dto.CredentialDto;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.repository.CredentialCreditRepository;
import com.atixlabs.semillasmiddleware.app.repository.CredentialRepository;
import com.atixlabs.semillasmiddleware.app.service.CredentialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(CredentialController.URL_MAPPING_CREDENTIAL)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@Slf4j
public class CredentialController {

    public static final String URL_MAPPING_CREDENTIAL = "/credential";

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private CredentialCreditRepository credentialCreditRepository;

    @Autowired
    private CredentialRepository credentialRepository;

    @RequestMapping(value = "/createCredit", method = RequestMethod.GET)
    public void createCredit() {
        log.info(" createCredit ");
        credentialService.addCredentialCredit();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CredentialDto> findAllCredentials() {
       List<Credential> credentials = credentialRepository.findAll();
       List<CredentialDto> credentialsDto = credentials.stream().map(aCredential -> new CredentialDto(aCredential)).collect(Collectors.toList());
       log.info("FIND CREDENTIALS -- " + credentialsDto.toString());
       return credentialsDto;
    }

}
