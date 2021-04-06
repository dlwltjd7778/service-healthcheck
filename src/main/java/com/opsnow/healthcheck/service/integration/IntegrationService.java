package com.opsnow.healthcheck.service.integration;

import com.opsnow.healthcheck.common.CustomRestTemplate;
import com.opsnow.healthcheck.common.constants.Constants;
import com.opsnow.healthcheck.model.alertnow.IntegrationPayload;
import com.opsnow.healthcheck.service.notification.NotificationService;
import com.opsnow.healthcheck.service.redis.EventIdListRedisService;
import com.opsnow.healthcheck.service.redis.PayloadRedisService;
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
    private final StandardPayloadServiceImpl standardPayloadServiceImpl;
    private final PayloadRedisService payloadRedisService;
    private final NotificationService notificationService;

    public void sendIntegrationAPI() {

        ZonedDateTime zonedDateTime;
        Map<String, String> reqBody = standardPayloadServiceImpl.makePayload();
        String eventId = reqBody.get("event_id");
        String summary = reqBody.get("summary");
        zonedDateTime = ZonedDateTime.now();

        // uuid Redis에 저장
        IntegrationPayload integrationPayload = payloadRedisService.saveEventData(eventId, summary, Constants.INTEGRATION_TYPE, zonedDateTime, Constants.INTEGRATION_URL, Constants.INTEGRATION_ENVIRONMENT);
        eventIdListRedisService.addEventCheckList(eventId);

        log.warn("start calling integration");
        Map<Object, Object> resMap = customRestTemplate.callPostRestTemplate(reqBody, Constants.INTEGRATION_URL);

        if (!( "ok".equals(resMap.get("msg")) && "200".equals(resMap.get("code")))){
            eventIdListRedisService.deleteEventCheckListByEventId(eventId); // 체크리스트에서 삭제해주자
            payloadRedisService.deleteIntegrationPayloadByEventId(eventId); // 실제 저장된 데이터에서 삭제해주자
            notificationService.sendNotification(integrationPayload, (String)resMap.get("msg"), (String)resMap.get("cause"));
        }
    }
}
