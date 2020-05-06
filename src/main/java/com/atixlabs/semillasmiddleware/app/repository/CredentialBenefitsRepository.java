package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefits;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CredentialBenefitsRepository extends JpaRepository<CredentialBenefits, Long> {

    List<CredentialBenefits> findByBeneficiaryDniAndCredentialState(Long dni, CredentialState state);

    Optional<CredentialBenefits> findByBeneficiaryDniAndCredentialStateAndBeneficiaryType(Long dni, CredentialState state, String beneficiaryType);

    Optional<CredentialBenefits> findByBeneficiaryDniAndCredentialStateInAndBeneficiaryType(Long dni, List<CredentialState> states, String beneficiaryType);

    List<CredentialBenefits> findByBeneficiaryDni(Long dni);
}
