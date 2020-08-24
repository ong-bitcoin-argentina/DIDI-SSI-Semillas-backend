package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.service;

import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.constant.RequestState;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.dto.IdentityValidationRequestDto;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.model.IdentityValidationRequest;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.repository.IdentityValidationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class IdentityValidationRequestService {

    @Autowired
    public IdentityValidationRequestService(IdentityValidationRequestRepository identityValidationRequestRepository){
        this.identityValidationRequestRepository = identityValidationRequestRepository;
    }

    private IdentityValidationRequestRepository identityValidationRequestRepository;

    public IdentityValidationRequest create(IdentityValidationRequestDto identityValidationRequestDto){
        IdentityValidationRequest idr =
                new IdentityValidationRequest(identityValidationRequestDto.getDni(),
                        identityValidationRequestDto.getDid(),
                        identityValidationRequestDto.getEmail(),
                        identityValidationRequestDto.getPhone(),
                        identityValidationRequestDto.getName(),
                        identityValidationRequestDto.getLastName(),
                        RequestState.IN_PROGRESS,
                        LocalDate.now(),
                        identityValidationRequestDto.getRevocationReason());

        return identityValidationRequestRepository.save(idr);
    }

}

