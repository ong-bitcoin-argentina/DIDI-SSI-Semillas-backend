package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class IdentityValidationRequestDto {
    @NotNull
    private Long dni;
    @NotNull
    private String did;
    private String email;
    private String phone;
    @NotNull
    private String name;
    @NotNull
    private String lastName;
    private String rejectionObservations;
}
