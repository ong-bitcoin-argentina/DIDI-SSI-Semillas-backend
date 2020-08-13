package com.atixlabs.semillasmiddleware.app.model.provider.service;

import com.atixlabs.semillasmiddleware.app.model.provider.exception.InexistentCategoryException;
import com.atixlabs.semillasmiddleware.app.model.provider.model.ProviderCategory;
import com.atixlabs.semillasmiddleware.app.model.provider.repository.ProviderCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderCategoryService {

    @Autowired
    public ProviderCategoryService(ProviderCategoryRepository providerCategoryRepository){
        this.providerCategoryRepository = providerCategoryRepository;
    }

    private ProviderCategoryRepository providerCategoryRepository;

    public ProviderCategory findById(Long categoryId){
        return providerCategoryRepository.findById(categoryId).orElseThrow(InexistentCategoryException::new);
    }

    public List<ProviderCategory> findAll(){
        return providerCategoryRepository.findAll();
    }
}
