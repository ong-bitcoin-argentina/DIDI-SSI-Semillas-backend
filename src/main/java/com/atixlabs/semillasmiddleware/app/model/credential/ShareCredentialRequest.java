package com.atixlabs.semillasmiddleware.app.model.credential;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
public class ShareCredentialRequest {
    @NotNull
    private String did;
    @NotNull
    private Long dni;
    @NotNull
    private Long providerId;
    @NotNull
    private String phone;
    @NotNull
    private String email;
}
