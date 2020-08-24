package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.controller;

import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.dto.IdentityValidationRequestDto;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.model.IdentityValidationRequest;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.service.IdentityValidationRequestService;
import com.atixlabs.semillasmiddleware.app.model.provider.controller.ProviderController;
import com.atixlabs.semillasmiddleware.app.model.provider.dto.ProviderFilterDto;
import com.atixlabs.semillasmiddleware.app.model.provider.model.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Optional;

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

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<IdentityValidationRequest> findAllProvidersFiltered(@RequestParam("page") @Min(0) int page){
        return identityValidationRequestService.findAll(page);
    }
}
