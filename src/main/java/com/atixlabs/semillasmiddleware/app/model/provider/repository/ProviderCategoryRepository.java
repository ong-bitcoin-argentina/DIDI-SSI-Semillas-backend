package com.atixlabs.semillasmiddleware.app.model.provider.repository;

import com.atixlabs.semillasmiddleware.app.model.provider.model.ProviderCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderCategoryRepository extends JpaRepository<ProviderCategory, Long> {
    Optional<ProviderCategory> findByName(String name);
}
