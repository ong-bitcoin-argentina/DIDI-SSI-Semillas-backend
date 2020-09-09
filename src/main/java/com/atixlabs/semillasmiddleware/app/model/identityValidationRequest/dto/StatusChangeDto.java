package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.dto;

import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.constant.RejectReason;
import com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.constant.RequestState;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Getter
@Setter
public class StatusChangeDto {
    @NotNull
    private RequestState requestState;

    private Optional<RejectReason> rejectReason = Optional.empty();

    private Optional<String> rejectionObservations = Optional.empty();

    public StatusChangeDto(RequestState requestState, Optional<RejectReason> rejectReason, Optional<String> rejectionObservations ){
        this.requestState = requestState;
        this.rejectReason = rejectReason;
        this.rejectionObservations = rejectionObservations;
    }
}
