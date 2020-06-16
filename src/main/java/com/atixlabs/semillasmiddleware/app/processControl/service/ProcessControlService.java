package com.atixlabs.semillasmiddleware.app.processControl.service;

import com.atixlabs.semillasmiddleware.app.processControl.exception.InvalidProcessException;
import com.atixlabs.semillasmiddleware.app.processControl.model.ProcessControl;
import com.atixlabs.semillasmiddleware.app.processControl.model.constant.ProcessControlStatusCodes;
import com.atixlabs.semillasmiddleware.app.processControl.repository.ProcessControlRepository;
import com.atixlabs.semillasmiddleware.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
            //set status
            processControl.get().setStatus(processStatus);
            //the process is set to start: set start time
            if(processStatus.equals(ProcessControlStatusCodes.RUNNING.getCode()))
                processControl.get().setStartTime(DateUtil.getLocalDateTimeNowWithFormat("yyyy-MM-dd HH:mm:ss"));

            processControlRepository.save(processControl.get());
            log.info("Process " + processName + " is now set to " + processStatus);
        }
        else
            throw new InvalidProcessException("Error setting new status tu process: There is no process with name " + processName);
    }

    public boolean isProcessRunning(String processName) throws InvalidProcessException {
        ProcessControl processControl = this.findByProcessName(processName);
        return processControl.isRunning();
    }

    public LocalDateTime getProcessTimeByProcessCode(String processName) throws InvalidProcessException {
        return this.findByProcessName(processName).getStartTime();
    }

    private ProcessControl findByProcessName(String processName) throws InvalidProcessException {
        Optional<ProcessControl> processControl = processControlRepository.findByProcessName(processName);
        if(processControl.isPresent())
            return processControl.get();
        else
            throw new InvalidProcessException("There is no process with name " + processName);
    }


}
