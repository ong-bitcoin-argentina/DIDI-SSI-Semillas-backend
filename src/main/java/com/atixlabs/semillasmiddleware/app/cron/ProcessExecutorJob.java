package com.atixlabs.semillasmiddleware.app.cron;

import com.atixlabs.semillasmiddleware.app.service.ProcessExecutorService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessExecutorJob implements Job {

    @Autowired
    private ProcessExecutorService processExecutorService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        processExecutorService.execute();
    }
}
