package com.atixlabs.semillasmiddleware.app.model.identityValidationRequest.constant;

import com.atixlabs.semillasmiddleware.app.model.action.ActionTypeEnum;

import java.util.Arrays;
import java.util.Optional;

public enum RequestState {

    STATE_IN_PROGRESS(0,"IN PROGRESS"),
    STATE_SUCCESS(1,"SUCCESS"),
    STATE_FAILURE(2,"FAILURE");


    private Integer id;
    private String description;

    RequestState(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public static Optional<RequestState> valueOf(int value) {
        return Arrays.stream(values())
                .filter(StateEnum -> StateEnum.id == value)
                .findFirst();
    }
}
