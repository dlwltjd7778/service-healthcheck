package com.opsnow.healthcheck.service.pagerduty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opsnow.healthcheck.common.constants.Constants;
import com.opsnow.healthcheck.common.CustomRestTemplate;
import com.opsnow.healthcheck.model.alertnow.IntegrationPayload;
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
        PagerDutyPayload payload = PagerDutyPayload.builder().payload(Payload.builder().summary(msg).source(eventId).build()).build();
        ObjectMapper objectMapper = new ObjectMapper();
        Map reqBody = objectMapper.convertValue(payload,Map.class);
        customRestTemplate.callPostRestTemplate(reqBody, Constants.PAGERDUTY_URL);
    }


    public void sendPagerDuty(IntegrationPayload integrationPayload, String msg) {
        String summary = String.format(Constants.NOTIFICATION_SUMMARY_FORMAT
                                        ,integrationPayload.getEnvironment()
                                        ,integrationPayload.getIntegrationType()
                                        ,integrationPayload.getEventId()
                                        ,msg);
        PagerDutyPayload payload = PagerDutyPayload.builder().payload(
                Payload.builder()
                        .summary(summary)
                        .source(integrationPayload.getEventId())
                        .custom_details(integrationPayload)
                        .build())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        Map reqBody = objectMapper.convertValue(payload,Map.class);
        customRestTemplate.callPostRestTemplate(reqBody, Constants.PAGERDUTY_URL);
        log.info("PAGERDUTY SUMMARY >> {}", summary);
    }

    //임시......
    public void sendPagerDuty(){
        PagerDutyPayload payload = PagerDutyPayload.builder().payload(Payload.builder().build()).build();
        ObjectMapper objectMapper = new ObjectMapper();
        Map reqBody = objectMapper.convertValue(payload,Map.class);
        customRestTemplate.callPostRestTemplate(reqBody, Constants.PAGERDUTY_URL);
    }
}

/*
    - pagerduty는 언제 호출하는가.
    1. 인티그레이션 호출 시 200 Ok 안떨어졌을 때
    2. IncidentJob 에서 5분 지난 것을 확인했을 때 (이때는 인시던트 생성 안됨)
    3. 컨트롤러에서 5분지났는데 왔을 때 (인시던트 생성되었으나 시간이 초과됨)
       레디스에 ID가 없는 경우 (NullpoinException)

    - pagerduty 전송 양식은 어떻게 할 것인가.
    summary에 넣을 값 : [환경][integration이름#uuid] 발송된 이유
    ex) [dev][standard] 발송된 이유

    - 이유 영어로
    1. Integration 을 호출했으나, 정상적 응답인 "200,ok" 가 오지 않았다. producer 문제로 예상된다.
    2. Integration 호출 시간으로부터 5분이 경과했지만, Webhook을 통한 알림을 받지 못했다.
    3. Integration 호출 시간으로부터 5분 경과 후 Webhook을 통해 알림이 전송되었다.
    4. WebHook으로 전송된 UUID 가 DB(Redis)에 저장되어 있지 않다.


 */