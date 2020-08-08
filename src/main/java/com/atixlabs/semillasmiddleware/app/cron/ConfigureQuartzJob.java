package com.atixlabs.semillasmiddleware.app.cron;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureQuartzJob {

    private  String cron;

    public ConfigureQuartzJob(@Value("${app.cron.proccess}") String cron){
        this.cron = cron;
    }

    @Bean
    public JobDetail jobProcessExecutorDetails() {
        return JobBuilder.newJob(ProcessExecutorJob.class).withIdentity("ProcessExecutorJob")
                .storeDurably().build();
    }


    @Bean
    public Trigger jobProcessExecutorTrigger(JobDetail jobADetails) {

        return TriggerBuilder.newTrigger().forJob(jobADetails)

                .withIdentity("jobProcessExecutorTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(this.cron))
                .build();
    }


}
