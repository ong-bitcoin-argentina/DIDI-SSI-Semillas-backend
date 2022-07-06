package com.atixlabs.semillasmiddleware.app.didi.repository;

import com.atixlabs.semillasmiddleware.app.didi.model.DidiAppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface DidiAppUserRepository extends JpaRepository<DidiAppUser, Long> {

    Optional<DidiAppUser> findByDniAndActiveTrue(Long dni);

    ArrayList<DidiAppUser> findByActiveAndSyncStatusIn(boolean active, ArrayList<String> didiSyncStatus);
}
