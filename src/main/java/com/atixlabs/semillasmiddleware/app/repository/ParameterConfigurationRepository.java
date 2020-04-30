package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CredentialCreditRepository extends JpaRepository<CredentialCredit, Long> {

    Optional<CredentialCredit> findByIdBondareaCreditAndCredentialState(String idBondarea, CredentialState state);

    List<CredentialCredit> findByIdGroup(String idGroup);
}
