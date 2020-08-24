package com.atixlabs.semillasmiddleware.app.model.action;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ActionLevelEnum {

    INFO(0,"INFO"),
    WARN(1,"WARN"),
    ERROR(2,"ERROR");


    private Integer id;
    private String description;

    ActionLevelEnum(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public static Optional<ActionLevelEnum> valueOf(int value) {
        return Arrays.stream(values())
                .filter(ActionLevelEnum -> ActionLevelEnum.id == value)
                .findFirst();
    }


}
