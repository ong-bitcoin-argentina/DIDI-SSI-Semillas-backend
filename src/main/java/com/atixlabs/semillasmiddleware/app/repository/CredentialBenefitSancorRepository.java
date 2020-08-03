package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefitSancor;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CredentialBenefitSancorRepository extends JpaRepository<CredentialBenefitSancor, Long> {

    Optional<CredentialBenefitSancor> findTopByCreditHolderDniAndBeneficiaryDniOrderByIdDesc(Long holderDni, Long beneficiaryDni);

    List<CredentialBenefitSancor> findByCredentialState(CredentialState credentialState);
}
