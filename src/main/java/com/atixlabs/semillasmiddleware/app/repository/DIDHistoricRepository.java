package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.didiHistoric.DidiHistoric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DIDHistoricRepository extends JpaRepository<DidiHistoric, Long> {

    Optional<DidiHistoric> findByIdPersonAndIsActive(Long idPerson, boolean active);


}
