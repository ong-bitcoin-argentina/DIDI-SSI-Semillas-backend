package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.configuration.ParameterConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParameterConfigurationRepository extends JpaRepository<ParameterConfiguration, Long> {

    Optional<ParameterConfiguration> findByConfigurationName(String configName);

}
