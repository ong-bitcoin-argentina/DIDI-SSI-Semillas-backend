package com.atixlabs.semillasmiddleware.app.model.provider.service;

import com.atixlabs.semillasmiddleware.app.model.provider.dto.ProviderFilterDto;
import com.atixlabs.semillasmiddleware.app.model.provider.exception.InexistentCategoryException;
import com.atixlabs.semillasmiddleware.app.model.provider.exception.InexistentProviderException;
import com.atixlabs.semillasmiddleware.app.model.provider.model.ProviderCategory;
import com.atixlabs.semillasmiddleware.app.model.provider.model.Provider;
import com.atixlabs.semillasmiddleware.app.model.provider.dto.ProviderCreateRequest;
import com.atixlabs.semillasmiddleware.app.model.provider.repository.ProviderRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.*;
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

    @Value("${app.pageSize}")
    private String size;

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

    private Specification<Provider> getProviderSpecification (ProviderFilterDto providerFilterDto){
        return (Specification<Provider>) (root, query, cb) -> {
            List<Predicate> predicates = Lists.newLinkedList();

            if (providerFilterDto.getActivesOnly().isPresent()){
                predicates.add(cb.equal(root.get("active"), providerFilterDto.getActivesOnly().get()));
            }
            if (providerFilterDto.getCategoryId().isPresent()){

                predicates.add(cb.equal(root.get("providerCategory").get("id"), providerFilterDto.getCategoryId().get()));
            }
            if (providerFilterDto.getCriteriaQuery().isPresent()){
                String criteria = providerFilterDto.getCriteriaQuery().get().toUpperCase();
                predicates.add(
                        cb.or(
                                cb.like(cb.upper(root.get("name")), criteria),
                                cb.like(cb.upper(root.get("email")),criteria)));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };

    }

    public Page<Provider> findAll(Integer page, ProviderFilterDto providerFilterDto){
        Pageable pageRequest = PageRequest.of(page, Integer.valueOf(size), Sort.by("name").ascending());
        return providerRepository.findAll(getProviderSpecification(providerFilterDto), pageRequest);
    }

    public void disable(Long providerId){
        Provider provider = providerRepository.findById(providerId).orElseThrow( () -> new InexistentProviderException());
        provider.setActive(false);
        providerRepository.save(provider);
    }
}
