package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.service;

import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.constant.RequestState;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.dto.IdentityValidationRequestDto;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.exceptions.InexistentIdentityValidationRequestException;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.model.IdentityValidationRequest;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.repository.IdentityValidationRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
public class IdentityValidationRequestService {

    @Autowired
    public IdentityValidationRequestService(IdentityValidationRequestRepository identityValidationRequestRepository,
                                            ShareStateChangeService shareStateChangeService){
        this.identityValidationRequestRepository = identityValidationRequestRepository;
        this.shareStateChangeService = shareStateChangeService;
    }

    @Value("${app.pageSize}")
    private String size;

    private IdentityValidationRequestRepository identityValidationRequestRepository;
    private ShareStateChangeService shareStateChangeService;

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

        log.info("Create Identity validation request: \n "+ idr.toString());
        return identityValidationRequestRepository.save(idr);
    }

    public Page<IdentityValidationRequest> findAll(Integer page){
        Pageable pageRequest = PageRequest.of(page, Integer.valueOf(size), Sort.by("date").ascending());
        return identityValidationRequestRepository.findAll(pageRequest);
    }

    public Optional<IdentityValidationRequest> findById(Long idValidationRequest){
        return identityValidationRequestRepository.findById(idValidationRequest);
    }

    public void changeRequestState(Long idValidationRequest,
                                   RequestState state,
                                   Optional<String> revocationReason)throws InexistentIdentityValidationRequestException {

        log.info("Changing identity validation request state, request id["+idValidationRequest+"], state["+state.toString()+"], revocation reason["+revocationReason.get()+"]");
        IdentityValidationRequest identityValidationRequest = this.findById(idValidationRequest)
                .orElseThrow(InexistentIdentityValidationRequestException::new);

        log.info("Request found");
        identityValidationRequest.setRequestState(state);
        revocationReason.ifPresent(identityValidationRequest::setRevocationReason);
        log.info("Final request state: \n "+ identityValidationRequest.toString());

        identityValidationRequestRepository.save(identityValidationRequest);
        shareStateChangeService.shareStateChange(identityValidationRequest);

    }

}

