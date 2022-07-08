package com.atixlabs.semillasmiddleware.app.model.credential;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Getter
public class ShareCredentialRequest {
    @NotNull
    private String did;
    @NotNull
    private Long dni;
    private Optional<Long> providerId = Optional.empty();
    @NotNull
    private String phone;
    @NotNull
    private String email;
    @NotNull
    private String viewerJWT;

    private Optional<String> customProviderEmail = Optional.empty();
}
