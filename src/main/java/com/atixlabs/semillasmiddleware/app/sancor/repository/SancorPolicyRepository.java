package com.atixlabs.semillasmiddleware.app.sancor.repository;

import com.atixlabs.semillasmiddleware.app.didi.model.DidiAppUser;
import com.atixlabs.semillasmiddleware.app.sancor.model.SancorPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SancorPolicyRepository extends JpaRepository<SancorPolicy, Long> {

}
