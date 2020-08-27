package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Getter
@Setter
public class StatusChangeDto {
    @NotNull
    private String requestState;

    private Optional<String> rejectReason = Optional.empty();

    private Optional<String> rejectionObservations = Optional.empty();

    public StatusChangeDto(String requestState, Optional<String> rejectReason, Optional<String> rejectionObservations ){
        this.requestState = requestState;
        this.rejectReason = rejectReason;
        this.rejectionObservations = rejectionObservations;
    }
}
