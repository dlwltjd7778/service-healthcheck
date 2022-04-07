package com.test.healthcheck.batch.tasklets;

import com.test.healthcheck.service.notification.NotificationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationTasklet implements Tasklet {

    @NonNull
    private final NotificationService notificationService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        log.info("PagerDutyTasklet 실행");
        notificationService.sendNotification();
        return RepeatStatus.FINISHED;
    }

    /*
    contribution - mutable state to be passed back to update the current step execution 현재 단계 실행을 업데이트하기 위해 다시 전달할 수 있는 상태
    chunkContext - attributes shared between invocations but not between restarts 호출 간에는 공유되지만 재시작 간에는 공유되지 않는 속성
     */


}
