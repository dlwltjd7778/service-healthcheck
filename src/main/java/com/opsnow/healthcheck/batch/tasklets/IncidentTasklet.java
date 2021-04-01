package com.opsnow.healthcheck.batch.tasklets;

import com.opsnow.healthcheck.service.incident.IncidentService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class IncidentTasklet implements Tasklet {

    @NonNull
    private final IncidentService incidentService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        log.info("IncidentTasklet 실행");

        incidentService.jobFacade();

        return RepeatStatus.FINISHED;
    }
}
