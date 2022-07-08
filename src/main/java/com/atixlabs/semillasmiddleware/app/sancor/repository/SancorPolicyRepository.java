package com.atixlabs.semillasmiddleware.app.sancor.repository;

import com.atixlabs.semillasmiddleware.app.sancor.model.SancorPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SancorPolicyRepository extends JpaRepository<SancorPolicy, Long> {

    Optional<SancorPolicy> findByCertificateClientDni(Long certificateClientDni);

    List<SancorPolicy> findByNeedReview(boolean needReview);
}
