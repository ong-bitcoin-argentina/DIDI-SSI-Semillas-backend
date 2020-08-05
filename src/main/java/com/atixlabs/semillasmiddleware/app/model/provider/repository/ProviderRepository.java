package com.atixlabs.semillasmiddleware.app.model.provider.repository;

import com.atixlabs.semillasmiddleware.app.model.provider.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProviderRepository extends JpaRepository<Provider, Long> {
    List<Provider> findAllByActive(boolean active);
}
