package com.atixlabs.semillasmiddleware.app.processControl.model;

import com.atixlabs.semillasmiddleware.app.processControl.model.constant.ProcessControlStatusCodes;
import com.atixlabs.semillasmiddleware.security.model.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table
@ToString
@Slf4j
public class ProcessControl extends AuditableEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private String processName;

    private String status;

    public boolean isRunning(){return status.equals(ProcessControlStatusCodes.RUNNING.getCode());}

}
