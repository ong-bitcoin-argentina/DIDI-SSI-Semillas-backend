package com.atixlabs.semillasmiddleware.app.dto;

import com.atixlabs.semillasmiddleware.app.model.action.ActionLog;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ActionDto {

    private Long id;

   // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Instant executionDateTime;

    private  String user;

    private String level;

    private String actionType;

    private String message;

    public ActionDto(){}

    public ActionDto(ActionLog actionLog){
        this.id = actionLog.getId();
        this.executionDateTime = actionLog.getExecutionDateTime();
        this.user = actionLog.getUserName();
        this.level = actionLog.getLevel().getDescription();
        this.actionType = actionLog.getActionType().getDescription();
        this.message = actionLog.getMessage();
    }

}
