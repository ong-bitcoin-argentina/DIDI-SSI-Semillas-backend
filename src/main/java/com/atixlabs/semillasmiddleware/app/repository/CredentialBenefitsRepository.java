package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialBenefitsRepository extends JpaRepository<CredentialBenefits, Long> {

    Optional<CredentialBenefits> findByDniBeneficiary(Long dni);
}
