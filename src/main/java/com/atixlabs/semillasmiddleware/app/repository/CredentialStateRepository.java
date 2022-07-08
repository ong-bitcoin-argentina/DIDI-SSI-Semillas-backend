package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.CredentialState.CredentialState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CredentialStateRepository extends JpaRepository<CredentialState, Long>{

    Optional<CredentialState> findByStateName(String state);

    List<CredentialState> findByStateNameIn(List<String> statesCodesArrayList);

}
