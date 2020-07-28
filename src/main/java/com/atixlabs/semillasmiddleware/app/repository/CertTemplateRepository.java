package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.didi.model.CertTemplate;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertTemplateRepository extends JpaRepository<CertTemplate, Long> {

    Optional<CertTemplate> findByCredentialCategoriesCodes(CredentialCategoriesCodes credentialCategoriesCodes);
}
