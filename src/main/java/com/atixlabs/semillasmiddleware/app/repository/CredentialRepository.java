package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes.CREDENTIAL_REVOKE;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {

    List<Credential>  findAllByCredentialStateIs(String credentialState); //TODO no esta filtrando, parece que no toma el campo crendentialState
}
