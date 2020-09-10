package com.atixlabs.semillasmiddleware.app.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Optional;
import java.time.Instant;


@Builder
@Getter
public class ActionFilterDto {
    @Builder.Default private Optional<String> username = Optional.empty();
    @Builder.Default private Optional<Integer> level = Optional.empty();
    @Builder.Default private Optional<Integer> actionType = Optional.empty();
    @Builder.Default private Optional<String> message = Optional.empty();
    @Builder.Default private Optional<Instant> dateFrom = Optional.empty();
    @Builder.Default private Optional<Instant> dateTo = Optional.empty();

}