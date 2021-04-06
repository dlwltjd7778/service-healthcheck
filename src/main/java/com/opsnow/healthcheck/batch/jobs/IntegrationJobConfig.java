package com.opsnow.healthcheck.batch.jobs;

import com.opsnow.healthcheck.batch.tasklets.IntegrationTasklet;
import com.opsnow.healthcheck.batch.tasklets.NotificationTasklet;
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
public class IntegrationJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final IntegrationTasklet integrationTasklet;
    private final NotificationTasklet notificationTasklet;


    @Bean
    public Job integrationJob(){
        return jobBuilderFactory.get("integrationJob")
                .start(sendIntegrationStep())         // Job 시작
                    .on("FAILED")           // step1 이 Failed 일 경우
                    .to(sendPagerDutyStep())          // step2 로 이동
                    .on("*")                // step2 의 결과와 관계 없이
                    .end()                            // step2 로 이동 하면 flow 종료
                .from(sendIntegrationStep())          // step1으로 부터,
                    .on("*")                // Failed 가 아닌 모든 경우
                    .end()                            // flow 종료
                .end()                                // Job 종료
                .build();
    }



    // pagerduty랑 연결하는 step
    @Bean
    public Step sendPagerDutyStep() {
        return stepBuilderFactory.get("sendPagerDuty")
                .tasklet(notificationTasklet)
                .allowStartIfComplete(true)
                .build();
    }

    // integration api 호출 step
    @Bean
    public Step sendIntegrationStep() {
        return stepBuilderFactory.get("sendIntegration")
                .tasklet(integrationTasklet)
                .allowStartIfComplete(true)
                .build();
    }

}
