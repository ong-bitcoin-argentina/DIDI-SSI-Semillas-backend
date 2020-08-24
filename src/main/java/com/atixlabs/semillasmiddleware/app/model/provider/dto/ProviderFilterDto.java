package com.atixlabs.semillasmiddleware.app.model.provider.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Builder
@Getter
public class ProviderFilterDto {
    @Builder.Default private Optional<String> criteriaQuery = Optional.empty();

    @Builder.Default private Optional<Boolean> activesOnly = Optional.empty();

    @Builder.Default private Optional<Long> categoryId = Optional.empty();

}

