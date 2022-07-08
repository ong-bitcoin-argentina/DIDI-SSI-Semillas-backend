package com.atixlabs.semillasmiddleware.app.model.provider.controller;

import com.atixlabs.semillasmiddleware.app.model.provider.dto.ProviderDto;
import com.atixlabs.semillasmiddleware.app.model.provider.dto.ProviderFilterDto;
import com.atixlabs.semillasmiddleware.app.model.provider.dto.ProviderUpdateRequest;
import com.atixlabs.semillasmiddleware.app.model.provider.exception.InexistentCategoryException;
import com.atixlabs.semillasmiddleware.app.model.provider.dto.ProviderCreateRequest;
import com.atixlabs.semillasmiddleware.app.model.provider.exception.InexistentProviderException;
import com.atixlabs.semillasmiddleware.app.model.provider.model.Provider;
import com.atixlabs.semillasmiddleware.app.model.provider.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping(ProviderController.URL_MAPPING)
@CrossOrigin(origins = {"http://localhost:8080", "${didi.server.url}"}, methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.PATCH})
public class ProviderController {
    public static final String URL_MAPPING = "/providers";

    @Autowired
    public ProviderController(ProviderService providerService){
        this.providerService = providerService;
    }

    private ProviderService providerService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<String> createProvider(@RequestBody @Valid ProviderCreateRequest providerCreateRequest){
        try {
            providerService.create(providerCreateRequest);
        }catch (InexistentCategoryException ice){
            return ResponseEntity.badRequest().body("ProviderCategory id is incorrect");
        }
        return ResponseEntity.accepted().body("created.");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/filtered")
    @ResponseStatus(HttpStatus.OK)
    public Page<Provider> findAllProvidersFiltered(@RequestParam("page") @Min(0) int page,
                                           @RequestParam Optional<Boolean> activesOnly,
                                           @RequestParam Optional<String> criteriaQuery,
                                           @RequestParam Optional<Long> categoryId){

        ProviderFilterDto providerFilterDto = ProviderFilterDto.builder()
                .activesOnly(activesOnly)
                .criteriaQuery(criteriaQuery)
                .categoryId(categoryId)
                .build();
        return providerService.findAll(page, providerFilterDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProviderDto> findAllProviders(){
        return providerService.findAll().stream().map(Provider::toDto).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findProvider(@PathVariable @Min(1) Long id){

        try {
            ProviderDto provider = providerService.findById(id).toDto();
            return ResponseEntity.ok().body(provider);
        }catch (InexistentProviderException ipe){
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/disable/{id}")
    public ResponseEntity<String> disableProvider(@PathVariable @Min(1) Long providerId){
        try {
            providerService.disable(providerId);
        }catch (InexistentProviderException ipe){
            return ResponseEntity.badRequest().body("There is no provider with id: "+providerId);
        }

        return ResponseEntity.ok().body("ok");
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateProvider(@PathVariable @Min(1) Long id,
                                            @RequestBody ProviderUpdateRequest providerUpdateRequest){
        try {
            providerService.update(id, providerUpdateRequest);
        }catch (InexistentProviderException ipe){
            return ResponseEntity.badRequest().body("There is no provider with id: "+id);
        }catch (InexistentCategoryException ice){
            return ResponseEntity.badRequest().body("There is no category with id: "+providerUpdateRequest.getCategoryId().orElse(null));
        }

        return ResponseEntity.ok().body("updated");
    }


}
