package com.atixlabs.semillasmiddleware.app.sancor.service;

import com.atixlabs.semillasmiddleware.app.sancor.repository.SancorPolicyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SancorPolicyService {

    private SancorPolicyRepository sancorPolicyRepository;

    @Autowired
    public SancorPolicyService(SancorPolicyRepository sancorPolicyRepository){
        this.sancorPolicyRepository = sancorPolicyRepository;
    }

}
