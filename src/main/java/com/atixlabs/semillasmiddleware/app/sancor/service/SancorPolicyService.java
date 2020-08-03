package com.atixlabs.semillasmiddleware.app.sancor.service;

import com.atixlabs.semillasmiddleware.app.sancor.model.SancorPolicy;
import com.atixlabs.semillasmiddleware.app.sancor.repository.SancorPolicyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class SancorPolicyService {

    private SancorPolicyRepository sancorPolicyRepository;

    @Autowired
    public SancorPolicyService(SancorPolicyRepository sancorPolicyRepository){
        this.sancorPolicyRepository = sancorPolicyRepository;
    }

    public Optional<SancorPolicy> findByCertificateClientDni(Long certificateClientDni){
        return this.sancorPolicyRepository.findByCertificateClientDni(certificateClientDni);
    }

    public SancorPolicy save(SancorPolicy sancorPolicy){
        return this.sancorPolicyRepository.save(sancorPolicy);
    }
}
