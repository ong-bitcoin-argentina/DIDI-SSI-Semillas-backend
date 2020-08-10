package com.atixlabs.semillasmiddleware.app.model.provider.controller;

import com.atixlabs.semillasmiddleware.app.model.provider.dto.ProviderDto;
import com.atixlabs.semillasmiddleware.app.model.provider.exception.InexistentCategoryException;
import com.atixlabs.semillasmiddleware.app.model.provider.dto.ProviderCreateRequest;
import com.atixlabs.semillasmiddleware.app.model.provider.model.Provider;
import com.atixlabs.semillasmiddleware.app.model.provider.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(ProviderController.URL_MAPPING)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class ProviderController {
    public static final String URL_MAPPING = "/providers";

    @Autowired
    public ProviderController(ProviderService providerService){
        this.providerService = providerService;
    }

    private ProviderService providerService;

    @PostMapping
    public ResponseEntity<String> createProvider(@RequestBody @Valid ProviderCreateRequest providerCreateRequest){
        try {
            providerService.create(providerCreateRequest);
        }catch (InexistentCategoryException ice){
            return ResponseEntity.badRequest().body("ProviderCategory id is incorrect");
        }
        return ResponseEntity.accepted().body("created.");
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProviderDto> findAllProvidersByActive(@RequestParam boolean activesOnly){
        List<Provider> providers = providerService.findAll(activesOnly);
        List<ProviderDto> providerDtos = new ArrayList<>();
        providers.forEach(provider -> providerDtos.add(provider.toDto()));
        return providerDtos;

    }
}
