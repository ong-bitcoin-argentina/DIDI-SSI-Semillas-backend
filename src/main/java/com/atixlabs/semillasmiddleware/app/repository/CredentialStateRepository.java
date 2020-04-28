package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialStateRepository extends JpaRepository<CredentialState, Long>{

    CredentialState findByStateName(String state);


}
