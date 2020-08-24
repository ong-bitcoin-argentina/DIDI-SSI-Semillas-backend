package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.controller;

import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.dto.IdentityValidationRequestDto;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.service.IdentityValidationRequestService;
import com.atixlabs.semillasmiddleware.app.model.provider.controller.ProviderController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(IdentityValidationRequestController.URL_MAPPING)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class IdentityValidationRequestController {

    public static final String URL_MAPPING = "/identityValidationRequests";

    @Autowired
    public IdentityValidationRequestController(IdentityValidationRequestService identityValidationRequestService){
        this.identityValidationRequestService = identityValidationRequestService;
    }

    private IdentityValidationRequestService identityValidationRequestService;

    @PostMapping
    public ResponseEntity<String> createRequest(@RequestBody @Valid IdentityValidationRequestDto identityValidationRequestDto){
        identityValidationRequestService.create(identityValidationRequestDto);
        return ResponseEntity.accepted().body("created.");
    }
}
