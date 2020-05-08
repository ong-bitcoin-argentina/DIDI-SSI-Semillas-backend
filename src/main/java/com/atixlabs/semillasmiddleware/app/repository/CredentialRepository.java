package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialIdentity;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> , CredentialRepositoryCustom{


    Optional<Credential> findByBeneficiaryDniAndAndCredentialCategoryAndCredentialStateIn(Long beneficiaryDni, String credentialCategoryCode, List<CredentialState> credentialStateActivePending);
}
