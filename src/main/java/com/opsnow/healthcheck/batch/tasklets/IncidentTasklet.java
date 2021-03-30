package com.opsnow.healthcheck.batch.tasklets;

import com.opsnow.healthcheck.service.extension.ExtensionService;
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
    private final ExtensionService extensionService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("incidentTasklet 실행");

        extensionService.jobFacade();

        return RepeatStatus.FINISHED;
    }
}
