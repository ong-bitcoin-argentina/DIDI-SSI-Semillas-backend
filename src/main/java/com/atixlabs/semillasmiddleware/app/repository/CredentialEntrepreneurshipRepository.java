package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.credential.CredentialEntrepreneurship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialEntrepreneurshipRepository extends JpaRepository<CredentialEntrepreneurship, Long> {

}
