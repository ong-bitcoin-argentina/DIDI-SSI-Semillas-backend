package com.atixlabs.semillasmiddleware.app.model.provider.service;

import com.atixlabs.semillasmiddleware.app.model.provider.exception.InexistentCategoryException;
import com.atixlabs.semillasmiddleware.app.model.provider.exception.InexistentProviderException;
import com.atixlabs.semillasmiddleware.app.model.provider.model.ProviderCategory;
import com.atixlabs.semillasmiddleware.app.model.provider.model.Provider;
import com.atixlabs.semillasmiddleware.app.model.provider.dto.ProviderCreateRequest;
import com.atixlabs.semillasmiddleware.app.model.provider.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderService {

    @Autowired
    public ProviderService(ProviderRepository providerRepository,
                           ProviderCategoryService providerCategoryService){
        this.providerRepository = providerRepository;
        this.providerCategoryService = providerCategoryService;
    }

    private ProviderRepository providerRepository;
    private ProviderCategoryService providerCategoryService;

    public Provider create(ProviderCreateRequest providerCreateRequest) {
        Provider provider = new Provider();
        Optional<ProviderCategory> category = providerCategoryService.findById(providerCreateRequest.getCategoryId());
        if(!category.isPresent()) throw new InexistentCategoryException();

        provider.setProviderCategory(category.get());
        provider.setEmail(providerCreateRequest.getEmail());
        provider.setName(providerCreateRequest.getName());
        provider.setPhone(providerCreateRequest.getPhone());
        provider.setBenefit(providerCreateRequest.getBenefit());
        provider.setSpeciality(providerCreateRequest.getSpeciality());

        return providerRepository.save(provider);
    }

    public Page<Provider> findAll(boolean activesOnly, Pageable pageRequest){
        if (activesOnly) return providerRepository.findAllByActive(pageRequest, true);
        return providerRepository.findAll(pageRequest);
    }

    public void disable(Long providerId){
        Provider provider = providerRepository.findById(providerId).orElseThrow( () -> new InexistentProviderException());
        provider.setActive(false);
        providerRepository.save(provider);
    }
}
