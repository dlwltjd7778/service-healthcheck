package com.opsnow.healthcheck.service.pagerduty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opsnow.healthcheck.common.Constants;
import com.opsnow.healthcheck.common.CustomRestTemplate;
import com.opsnow.healthcheck.model.pagerduty.PagerDutyPayload;
import com.opsnow.healthcheck.model.pagerduty.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagerDutyService {

    private final CustomRestTemplate customRestTemplate;
    // https://jiseonglee.pagerduty.com

    public void sendPagerDuty(String eventId, String msg) {
        log.info("PAGERDUTY >> EventId : {}, msg : {}", eventId, msg);
        PagerDutyPayload payload = PagerDutyPayload.builder().payload(Payload.builder().summary(msg).build()).build();
        ObjectMapper objectMapper = new ObjectMapper();
        Map reqBody = objectMapper.convertValue(payload,Map.class);
        customRestTemplate.callPostRestTemplate(reqBody, Constants.PAGERDUTY_URL);
    }

    //임시......
    public void sendPagerDuty(){
        PagerDutyPayload payload = PagerDutyPayload.builder().payload(Payload.builder().build()).build();
        ObjectMapper objectMapper = new ObjectMapper();
        Map reqBody = objectMapper.convertValue(payload,Map.class);
        customRestTemplate.callPostRestTemplate(reqBody, Constants.PAGERDUTY_URL);
    }
}
