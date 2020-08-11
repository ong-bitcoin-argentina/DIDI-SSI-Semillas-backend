package com.atixlabs.semillasmiddleware.app.model.provider.repository;

import com.atixlabs.semillasmiddleware.app.model.provider.model.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderRepository extends JpaRepository<Provider, Long> {

    Page<Provider> findAllByActive(Pageable pageable, boolean active);

    @Override
    Page<Provider> findAll(Pageable pageable);

    Optional<Provider> findByEmail(String email);
}
