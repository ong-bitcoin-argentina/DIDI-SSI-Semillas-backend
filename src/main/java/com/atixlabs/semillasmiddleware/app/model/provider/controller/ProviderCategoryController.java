package com.atixlabs.semillasmiddleware.app.model.provider.controller;

import com.atixlabs.semillasmiddleware.app.model.provider.dto.ProviderCategoryDto;
import com.atixlabs.semillasmiddleware.app.model.provider.model.ProviderCategory;
import com.atixlabs.semillasmiddleware.app.model.provider.service.ProviderCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(ProviderCategoryController.URL_MAPPING)
@CrossOrigin(origins = {"http://localhost:8080", "${didi.server.url}"}, methods= {RequestMethod.GET,RequestMethod.POST})
public class ProviderCategoryController {
    public static final String URL_MAPPING = "/providerCategories";

    @Autowired
    public ProviderCategoryController(ProviderCategoryService providerCategoryService){
        this.providerCategoryService= providerCategoryService;
    }

    private ProviderCategoryService providerCategoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProviderCategoryDto> findAllProviders(){
        List<ProviderCategory> providerCategories = providerCategoryService.findAll();
        List<ProviderCategoryDto> providerCategoryDtos = new ArrayList<>();
        providerCategories.forEach(providerCategory -> providerCategoryDtos.add(providerCategory.toDto()));
        return providerCategoryDtos;

    }
}
