package com.atixlabs.semillasmiddleware.app.model.credential;

import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
@Builder
public class CredentialFilterDto {
    private Optional<String> did;
    private Optional<String> category;
    private Optional<Long> beneficiaryDni;
}
