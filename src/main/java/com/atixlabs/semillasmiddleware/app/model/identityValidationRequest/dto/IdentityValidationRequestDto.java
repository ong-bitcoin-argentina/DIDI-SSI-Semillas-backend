package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IdentityValidationRequestDto {
    private Long dni;
    private String did;
    private String email;
    private String phone;
    private String name;
    private String lastName;
    private String revocationReason;
}
