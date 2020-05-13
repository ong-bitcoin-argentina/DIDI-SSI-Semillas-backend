package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> , CredentialRepositoryCustom{


    ArrayList<Credential> findByCredentialStateAndBeneficiaryDniIn(CredentialState credentialState, ArrayList<Long> dniList);

    ArrayList<Credential> findByCredentialCategoryAndCredentialState(String code, CredentialState credentialStatePending);

    Optional<Credential> findByBeneficiaryDniAndCredentialCategoryAndCredentialStateIn(Long beneficiaryDni, String credentialCategoryCode, ArrayList<CredentialState> credentialStateActivePending);
}
