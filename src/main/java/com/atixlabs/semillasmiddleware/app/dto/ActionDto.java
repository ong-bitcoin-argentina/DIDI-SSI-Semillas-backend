package com.atixlabs.semillasmiddleware.app.dto;

import com.atixlabs.semillasmiddleware.app.model.action.ActionLog;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
public class ActionDto {

    private Long id;

   // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Instant texecutionDateTime;

    private  String user;

    private String level;

    private String actionType;

    private String message;

    public ActionDto(){};

    public ActionDto(ActionLog actionLog){
        this.id = actionLog.getId();
        this.texecutionDateTime = actionLog.getExecutionDateTime();
        this.user = actionLog.getUserName();
        this.level = actionLog.getLevel().getDescription();
        this.actionType = actionLog.getActionType().getDescription();
        this.message = actionLog.getMessage();
    }

}
