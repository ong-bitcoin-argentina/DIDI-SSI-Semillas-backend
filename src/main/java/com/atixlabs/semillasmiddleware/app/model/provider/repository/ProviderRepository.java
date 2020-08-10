package com.atixlabs.semillasmiddleware.app.model.provider.repository;

import com.atixlabs.semillasmiddleware.app.model.provider.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProviderRepository extends JpaRepository<Provider, Long> {
    List<Provider> findAllByActive(boolean active);

    Optional<Provider> findByEmail(String email);
}
