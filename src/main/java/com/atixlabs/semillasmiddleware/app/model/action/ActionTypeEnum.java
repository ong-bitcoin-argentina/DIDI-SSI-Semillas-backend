package com.atixlabs.semillasmiddleware.app.model.action;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ActionTypeEnum {

    //TODO
    NEW_SURVEY(0,"ENCUESTA"),
    BONDAREA_SYNC(1,"BONDAREA"),
    DIDI(2,"DIDI"),
    CREDENTIAL(3,"CREDENCIAL"),
    DIDI_CREDENTIAL_REQUEST(4,"SOLICITUD CREDENCIAL");


    private Integer id;
    private String description;

    ActionTypeEnum(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public static Optional<ActionTypeEnum> valueOf(int value) {
        return Arrays.stream(values())
                .filter(ActionTypeEnum -> ActionTypeEnum.id == value)
                .findFirst();
    }
}
