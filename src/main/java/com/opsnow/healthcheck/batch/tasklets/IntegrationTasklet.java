package com.opsnow.healthcheck.batch.tasklets;

import com.opsnow.healthcheck.common.constants.Constants;
import com.opsnow.healthcheck.common.constants.ConstantsEnum;
import com.opsnow.healthcheck.service.integration.IntegrationAPIService;
import com.opsnow.healthcheck.service.notification.NotificationService;
import com.opsnow.healthcheck.service.redis.PayloadRedisService;
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
public class IntegrationTasklet implements Tasklet {

    @NonNull
    private final IntegrationAPIService integrationAPIService;
    private final NotificationService s;
    private final PayloadRedisService ips;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        for(ConstantsEnum.IntegrationAPIKey apiKey : ConstantsEnum.IntegrationAPIKey.values()){
            Constants.INTEGRATION_ENVIRONMENT = apiKey.getEnvType().toString();
            Constants.INTEGRATION_TYPE = apiKey.getIntegrationType().toString();
            Constants.INTEGRATION_URL = ConstantsEnum.getUrl(apiKey);
            log.info("IntegrationTasklet 실행 : ENV - {}, INTEGRATION_TYPE - {}, INTEGRATION_URL - {}"
                    ,Constants.INTEGRATION_ENVIRONMENT
                    ,Constants.INTEGRATION_TYPE
                    ,Constants.INTEGRATION_URL);
            integrationAPIService.sendIntegrationAPI();
        }

        return RepeatStatus.FINISHED;
    }

    /*
    contribution - mutable state to be passed back to update the current step execution 현재 단계 실행을 업데이트하기 위해 다시 전달할 수 있는 상태
    chunkContext - attributes shared between invocations but not between restarts 호출 간에는 공유되지만 재시작 간에는 공유되지 않는 속성
     */


}
