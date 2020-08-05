package com.atixlabs.semillasmiddleware.app.model.provider.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class ProviderCreateRequest {
    @NotNull
    private Long categoryId;
    @NotNull
    private String name;
    @NotNull
    private String phone;
    @NotNull
    private String email;
    @NotNull
    private Integer benefit;
    @NotNull
    private String speciality;

}
