package com.atixlabs.semillasmiddleware.app.model.action;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table
public class ActionLog {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime executionDateTime;

    private  String userName;

    private ActionLevel level;

    private String levelDescription;

    private ActionTypeEnum actionType;
    private String actionTypeDescription;

    private String message;
}
