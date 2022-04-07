package com.test.healthcheck.batch.jobs;

import com.test.healthcheck.batch.tasklets.IncidentTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class IncidentJobConfig {

    private final IncidentTasklet incidentTasklet;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job incidentJob(){
        return jobBuilderFactory.get("incidentJob")
                .start(chkIncidentStep())
                .build();
    }

    @Bean
    public Step chkIncidentStep(){
        return stepBuilderFactory.get("chkIncidentStep")
                .tasklet(incidentTasklet)
                .allowStartIfComplete(true)
                .build();
    }
}
