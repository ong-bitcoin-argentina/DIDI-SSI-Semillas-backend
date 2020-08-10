package com.atixlabs.semillasmiddleware.app.model.provider.service;

import com.atixlabs.semillasmiddleware.app.model.provider.model.ProviderCategory;
import com.atixlabs.semillasmiddleware.app.model.provider.repository.ProviderCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderCategoryService {

    @Autowired
    public ProviderCategoryService(ProviderCategoryRepository providerCategoryRepository){
        this.providerCategoryRepository = providerCategoryRepository;
    }

    private ProviderCategoryRepository providerCategoryRepository;

    public Optional<ProviderCategory> findById(Long categoryId){
        return providerCategoryRepository.findById(categoryId);
    }

    public List<ProviderCategory> findAll(){
        return providerCategoryRepository.findAll();
    }
}
