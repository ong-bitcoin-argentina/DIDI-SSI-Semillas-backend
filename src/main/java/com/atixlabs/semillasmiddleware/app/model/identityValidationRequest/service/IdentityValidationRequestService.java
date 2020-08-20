package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.service;

import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.dto.IdentityValidationRequestDto;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.model.IdentityValidationRequest;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.repository.IdentityValidationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentityValidationRequestService {

    @Autowired
    public IdentityValidationRequestService(IdentityValidationRequestRepository identityValidationRequestRepository){
        this.identityValidationRequestRepository = identityValidationRequestRepository;
    }

    private IdentityValidationRequestRepository identityValidationRequestRepository;

    public IdentityValidationRequest save(IdentityValidationRequestDto identityValidationRequestDto){
        IdentityValidationRequest idr =
                new IdentityValidationRequest(identityValidationRequestDto.getDni(),
                        identityValidationRequestDto.getDid(),
                        identityValidationRequestDto.getEmail(),
                        identityValidationRequestDto.getPhone(),
                        identityValidationRequestDto.getName(),
                        identityValidationRequestDto.getLastName());

        return identityValidationRequestRepository.save(idr);
    }

}

