package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialEntrepreneurship;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CredentialEntrepreneurshipRepository extends JpaRepository<CredentialEntrepreneurship, Long> {

    List<CredentialEntrepreneurship> findByCredentialState(CredentialState credentialState);

    List<Credential> findByCreditHolderDniAndEntrepreneurshipNameAndCredentialStateIn(Long creditHolderDni, String entrepreneurshipName, List<CredentialState> credentialStateActivePending);

    List<CredentialEntrepreneurship> findByCreditHolderDniAndCredentialState(Long holderDni, CredentialState credentialStates);

}
