package com.atixlabs.semillasmiddleware.app.model.provider.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
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

    @Builder.Default
    private Optional<String> phone = Optional.empty();
    @NotNull
    private String email;

    @Builder.Default
    private Optional<String> whatsappNumber = Optional.empty();

    @Builder.Default
    private Optional<Integer> benefit = Optional.empty();

    @NotNull
    @Builder.Default
    private String description = "";

    @NotNull
    private String speciality;

}
