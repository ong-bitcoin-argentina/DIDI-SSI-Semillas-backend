package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.repository;

import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.model.IdentityValidationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IdentityValidationRequestRepository extends JpaRepository<IdentityValidationRequest, Long>, JpaSpecificationExecutor<IdentityValidationRequest> {
}
