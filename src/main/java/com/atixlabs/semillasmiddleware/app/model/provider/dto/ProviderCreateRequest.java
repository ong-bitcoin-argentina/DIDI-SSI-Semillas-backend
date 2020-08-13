package com.atixlabs.semillasmiddleware.app.model.provider.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
public class ProviderCreateRequest {
    @NotNull
    private Long categoryId;
    @NotNull
    private String name;
    @NotNull
    private String phone;
    @NotNull
    private String email;

    private String whatsappNumber;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer benefit;
    @NotNull
    private String speciality;

}
