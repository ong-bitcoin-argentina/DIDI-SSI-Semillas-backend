package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.dto;

import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Getter
public class StatusChangeDto {
    @NotNull
    private String requestState;

    private Optional<String> revocationReason = Optional.empty();
}
