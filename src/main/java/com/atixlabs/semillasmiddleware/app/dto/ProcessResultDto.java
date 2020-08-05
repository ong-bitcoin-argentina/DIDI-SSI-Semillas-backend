package com.atixlabs.semillasmiddleware.app.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProcessResultDto {

    private Long toProcess;
    private Long processOk;
    private Long processWithErrors;

    public ProcessResultDto(){
        toProcess = 0L;
        processOk = 0L;
        processWithErrors = 0L;

    }

    public void addProcessOk(){
        processOk++;
    }

    public void addProcessWithErrors(){
        processWithErrors++;
    }



}
