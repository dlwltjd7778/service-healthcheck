package com.test.healthcheck.batch.schedulers;

import com.test.healthcheck.batch.jobs.IncidentJobConfig;
import com.test.healthcheck.batch.jobs.IntegrationJobConfig;
import com.test.healthcheck.common.constants.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final IntegrationJobConfig integrationJobConfig;
    private final IncidentJobConfig incidentJobConfig;
    private final JobLauncher jobLauncher;

    @Scheduled(cron = Constants.INTEGRATION_CRON_EXPRESSION)
    public void executeIntegrationJob() {
        try {
            jobLauncher.run(
                    integrationJobConfig.integrationJob(), new JobParametersBuilder().addString("datetime", String.valueOf(LocalDateTime.now())).toJobParameters() //Job Parameter의 역할은 반복해서 실행되는 Job의 유일한 ID라고 생각하면 됨.
            );
        } catch (JobExecutionException e) {
            log.error("{}", e.getMessage());
        }
    }

    @Scheduled(cron = Constants.INCIDENT_CRON_EXPRESSION)
    public void executeIncidentJob() {
        try {
            jobLauncher.run(
                    incidentJobConfig.incidentJob(), new JobParametersBuilder().addString("datetime", String.valueOf(LocalDateTime.now())).toJobParameters() //Job Parameter의 역할은 반복해서 실행되는 Job의 유일한 ID라고 생각하면 됨.
            );
        } catch (JobExecutionException e) {
            log.error("{}", e.getMessage());
        }
    }

}
