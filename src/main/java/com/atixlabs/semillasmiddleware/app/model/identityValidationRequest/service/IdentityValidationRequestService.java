package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.service;

import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.constant.RejectReason;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.constant.RequestState;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.dto.IdentityValidationRequestDto;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.dto.StatusChangeDto;
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
                        LocalDate.now());

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

    public void changeRequestState(Long idValidationRequest, StatusChangeDto statusChangeDto)throws InexistentIdentityValidationRequestException {
        Optional<String> rejectionObservations = statusChangeDto.getRejectionObservations();
        log.info("Changing identity validation request state, request id["+idValidationRequest+"], state["+statusChangeDto.getRequestState()+"], revocation reason["+rejectionObservations.orElse("")+"]");

        RequestState requestState = RequestState.valueOf(statusChangeDto.getRequestState());
        Optional<RejectReason> rejectReason = statusChangeDto.getRejectReason().map(RejectReason::valueOf);

        IdentityValidationRequest identityValidationRequest = this.findById(idValidationRequest)
                .orElseThrow(InexistentIdentityValidationRequestException::new);

        log.info("Request found");
        identityValidationRequest.setReviewDate(LocalDate.now());
        identityValidationRequest.setRequestState(requestState);
        rejectReason.ifPresent(identityValidationRequest::setRejectReason);
        rejectionObservations.ifPresent(identityValidationRequest::setRejectionObservations);
        log.info("Final request state: \n "+ identityValidationRequest.toString());

        identityValidationRequestRepository.save(identityValidationRequest);
        shareStateChangeService.shareStateChange(identityValidationRequest);

    }

}

