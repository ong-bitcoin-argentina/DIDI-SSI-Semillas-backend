package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.dto;

import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Getter
public class StatusChangeDto {
    @NotNull
    @Min(1)
    private Integer idRequestState;

    private Optional<String> revocationReason = Optional.empty();
}
