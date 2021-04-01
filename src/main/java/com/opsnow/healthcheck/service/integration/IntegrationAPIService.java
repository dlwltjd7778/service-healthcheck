package com.opsnow.healthcheck.service.integration;

import com.opsnow.healthcheck.common.CallIntegrationException;
import com.opsnow.healthcheck.common.constants.Constants;
import com.opsnow.healthcheck.common.CustomRestTemplate;
import com.opsnow.healthcheck.common.constants.ConstantsEnum;
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
        String eventId = reqBody.get("event_id");
        String summary = reqBody.get("summary");
        zonedDateTime = ZonedDateTime.now();
        String url = ConstantsEnum.getUrl(ConstantsEnum.IntegrationUrl.DEV_STANDARD_URL);

        // uuid Redis에 저장
        integrationPayloadService.saveEventData(eventId, summary, type, zonedDateTime, url);
        eventIdService.addEventCheckList(eventId);

        log.warn("start calling integration");
        Map<Object, Object> resMap = customRestTemplate.callPostRestTemplate(reqBody, url);

        if (!((200) == (Integer) resMap.get("code")) && "ok".equals(resMap.get("msg"))){
            pagerDutyService.sendPagerDuty(eventId, "integration 호출 시 문제 발생 (not 200, ok)");
            throw new CallIntegrationException();
        }

    }

}
