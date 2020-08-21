package com.atixlabs.semillasmiddleware.app.model.provider.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Setter
@Getter
@Builder
public class ProviderCreateRequest {
    @NotNull
    private Long categoryId;
    @NotNull
    private String name;

    private Optional<String> phone;
    @NotNull
    private String email;

    private Optional<String> whatsappNumber;

    private Optional<Integer> benefit;

    @NotNull
    @Builder.Default
    private String description = "";

    @NotNull
    private String speciality;

}
