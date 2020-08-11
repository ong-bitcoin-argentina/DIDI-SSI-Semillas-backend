package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.credential.CredentialDwelling;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CredentialDwellingRepository extends JpaRepository<CredentialDwelling, Long> {

    List<CredentialDwelling> findByCredentialState(CredentialState credentialState);

    List<CredentialDwelling> findByCreditHolderDniAndCredentialState(Long holderDni, CredentialState credentialStates);

}
