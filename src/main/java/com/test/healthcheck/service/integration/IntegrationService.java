package com.test.healthcheck.service.integration;

import com.test.healthcheck.common.CustomRestTemplate;
import com.test.healthcheck.common.constants.Constants;
import com.test.healthcheck.model.incdtmg.IntegrationPayload;
import com.test.healthcheck.service.notification.NotificationService;
import com.test.healthcheck.service.redis.EventIdListRedisService;
import com.test.healthcheck.service.redis.PayloadRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntegrationService {

    private final CustomRestTemplate customRestTemplate;
    private final EventIdListRedisService eventIdListRedisService;
    private final StandardPayloadImpl standardPayloadServiceImpl;
    private final PayloadRedisService payloadRedisService;
    private final NotificationService notificationService;

    public void sendIntegrationAPI() {

        ZonedDateTime zonedDateTime;
        Map<String, String> reqBody = standardPayloadServiceImpl.makePayload("open");
        String eventId = reqBody.get("event_id");
        String summary = reqBody.get("summary");
        zonedDateTime = ZonedDateTime.now();

        // uuid Redis에 저장
        IntegrationPayload integrationPayload = payloadRedisService.saveEventData(eventId, summary, Constants.INTEGRATION_TYPE, zonedDateTime, Constants.INTEGRATION_KEY, Constants.INTEGRATION_ENVIRONMENT);
        eventIdListRedisService.addEventCheckList(eventId);

        log.warn("start calling [{}] {} integration", Constants.INTEGRATION_ENVIRONMENT, Constants.INTEGRATION_TYPE);
        Map<Object, Object> resMap = customRestTemplate.callPostRestTemplate(reqBody, Constants.INTEGRATION_URL);

        Object code = resMap.get("code");

        // 삼성향은 code 가 Integer로 들어온다.
        if(code instanceof Integer){
            code = code.toString();
        }

        if (!( "ok".equals(resMap.get("msg")) && "200".equals(code))){
            log.warn("응답 : {}",resMap);
            eventIdListRedisService.deleteEventCheckListByEventId(eventId); // 체크리스트에서 삭제해주자
            payloadRedisService.deleteIntegrationPayloadByEventId(eventId); // 실제 저장된 데이터에서 삭제해주자
            notificationService.sendNotification(integrationPayload, (String)resMap.get("msg"), (String)resMap.get("cause"));
        }
    }
}
