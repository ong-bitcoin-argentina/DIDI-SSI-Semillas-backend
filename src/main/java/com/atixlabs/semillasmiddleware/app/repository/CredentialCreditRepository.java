package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialCreditRepository extends JpaRepository<CredentialCredit, Long> {

    Optional<CredentialCredit> findByIdBondareaCredit(String idBondarea);
}
