package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.DIDHistoric.DIDHisotoric;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DIDHistoricRepository extends JpaRepository<DIDHisotoric, Long> {

    Optional<DIDHisotoric> findByIdPersonAndIsActive(Long idPersona, boolean active);


}
