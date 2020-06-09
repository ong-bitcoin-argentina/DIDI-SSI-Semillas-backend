package com.atixlabs.semillasmiddleware.app.processControl.service;

import com.atixlabs.semillasmiddleware.app.processControl.exception.InvalidProcessException;
import com.atixlabs.semillasmiddleware.app.processControl.model.ProcessControl;
import com.atixlabs.semillasmiddleware.app.processControl.repository.ProcessControlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ProcessControlService {

    private ProcessControlRepository processControlRepository;

    @Autowired
    public ProcessControlService(ProcessControlRepository processControlRepository) {
        this.processControlRepository = processControlRepository;
    }

    public void setStatusToProcess(String processName, String processStatus) throws InvalidProcessException {
        Optional<ProcessControl> processControl = processControlRepository.findByProcessName(processName);
        if(processControl.isPresent()) {
            processControl.get().setStatus(processStatus);
            processControlRepository.save(processControl.get());
            log.info("Process " + processName + "is now set to " + processStatus);
        }
        else
            throw new InvalidProcessException("Error to set new status tu process: There is no process with name " + processName);
    }

    public boolean isProcessRunning(String processName) throws InvalidProcessException {
        Optional<ProcessControl> processControl = processControlRepository.findByProcessName(processName);
        if(processControl.isPresent())
            return processControl.get().isRunning();
        else
            throw new InvalidProcessException("There is no process with name " + processName);
    }


}
