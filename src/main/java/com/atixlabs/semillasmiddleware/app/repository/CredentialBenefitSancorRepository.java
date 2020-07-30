package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefitSancor;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialBenefitSancorRepository extends JpaRepository<CredentialBenefitSancor, Long> {

    Optional<CredentialBenefitSancor> findTopByCreditHolderDniAndBeneficiaryDniAndBeneficiaryTypeOrderByIdDesc(Long holderDni, Long beneficiaryDni, String beneficiaryType);

}
