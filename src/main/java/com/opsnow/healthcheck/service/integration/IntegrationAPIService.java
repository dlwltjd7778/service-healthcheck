package com.opsnow.healthcheck.service.integration;

import com.opsnow.healthcheck.common.CallIntegrationException;
import com.opsnow.healthcheck.common.Constants;
import com.opsnow.healthcheck.common.CustomRestTemplate;
import com.opsnow.healthcheck.service.pagerduty.PagerDutyService;
import com.opsnow.healthcheck.service.redis.EventIdService;
import com.opsnow.healthcheck.service.redis.IntegrationPayloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntegrationAPIService {

    private final CustomRestTemplate customRestTemplate;
    private final EventIdService eventIdService;
    private final IntegrationPayloadServiceImpl integrationService;
    private final IntegrationPayloadService integrationPayloadService;
    private final PagerDutyService pagerDutyService;

    public void sendIntegrationAPI(String type) throws Exception {

        ZonedDateTime zonedDateTime;
        Map<String, String> reqBody = integrationService.makeStandardPayload();
        zonedDateTime = ZonedDateTime.now();

        // uuid Redis에 저장
        integrationPayloadService.saveEventData(reqBody.get("event_id"), reqBody.get("summary"), type, zonedDateTime, Constants.STANDARD_INTEGRATION_URL);
        eventIdService.addEventCheckList(reqBody.get("event_id"));

        log.warn("start calling integration");

//        Map<Object, Object> resMap = customRestTemplate.callPostRestTemplate(reqBody, Constants.STANDARD_INTEGRATION_URL);
//
//        if (!((200) == (Integer) resMap.get("code")) && "ok".equals(resMap.get("msg"))){
//            pagerDutyService.sendPagerDuty(reqBody.get("event_id"), "integration 호출 시 문제 발생 (not 200, ok)");
//            throw new CallIntegrationException();
//        }

    }

}
