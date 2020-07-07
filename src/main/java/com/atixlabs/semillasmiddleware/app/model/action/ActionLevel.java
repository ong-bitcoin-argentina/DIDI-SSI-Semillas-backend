package com.atixlabs.semillasmiddleware.app.model.action;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ActionLevel {

    INFO(0,"INFO"),
    WARN(1,"WARN"),
    ERROR(2,"ERROR");


    private Integer id;
    private String description;

    ActionLevel(Integer id, String description) {
        this.id = id;
        this.description = description;
    }



}
