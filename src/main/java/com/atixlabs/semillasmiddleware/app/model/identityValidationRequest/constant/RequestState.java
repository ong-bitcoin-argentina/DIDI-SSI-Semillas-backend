package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.constant;

import com.atixlabs.semillasmiddleware.app.model.action.ActionTypeEnum;

import java.util.Arrays;
import java.util.Optional;

public enum RequestState {

    IN_PROGRESS(0),
    SUCCESS(1),
    FAILURE(2);


    private Integer id;

    RequestState(Integer id) {
        this.id = id;
    }

    public static Optional<RequestState> valueOf(int value) {
        return Arrays.stream(values())
                .filter(StateEnum -> StateEnum.id == value)
                .findFirst();
    }
}
