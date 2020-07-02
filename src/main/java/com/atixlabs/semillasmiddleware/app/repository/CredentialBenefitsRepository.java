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

    //List<CredentialBenefits> findByBeneficiaryDniAndCredentialState(Long dni, CredentialState state);

    //Optional<CredentialBenefits> findByBeneficiaryDniAndCredentialStateAndBeneficiaryType(Long dni, CredentialState state, String beneficiaryType);

    Optional<CredentialBenefits> findByBeneficiaryDniAndCredentialStateInAndBeneficiaryType(Long dni, List<CredentialState> states, String beneficiaryType);

    List<CredentialBenefits> findByBeneficiaryDni(Long dni);

    List<CredentialBenefits> findByCreditHolderDniAndCredentialStateIn(Long dni, List<CredentialState> states);

    List<CredentialBenefits> findByBeneficiaryDniAndCredentialStateIn(Long dni, List<CredentialState> states);

    Optional<CredentialBenefits> findByCreditHolderDniAndBeneficiaryDniAndCredentialStateIn(Long dniHolder, Long dniBeneficiary, List<CredentialState> states);

    //List<CredentialBenefits> findByBeneficiaryDniAndCredentialStateIn(Long dni, List<CredentialState> states);

    List<CredentialBenefits> findByCreditHolderDniAndCredentialStateInAndBeneficiaryType(Long dni, List<CredentialState> states, String beneficiaryType);

    Optional<CredentialBenefits> findTopByCreditHolderDniAndBeneficiaryDniAndBeneficiaryTypeOrderByIdDesc(Long holderDni, Long beneficiaryDni,String beneficiaryType);

}
