package com.test.healthcheck.service.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.healthcheck.common.constants.Constants;
import com.test.healthcheck.common.CustomRestTemplate;
import com.test.healthcheck.model.incdtmg.IntegrationPayload;
import com.test.healthcheck.model.pagerduty.CustomDetails;
import com.test.healthcheck.model.pagerduty.PagerDutyPayload;
import com.test.healthcheck.model.pagerduty.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final CustomRestTemplate customRestTemplate;

    // overloading
    public void sendNotification(String eventId, String msg) {
        PagerDutyPayload payload = PagerDutyPayload.builder()
                .payload(Payload.builder()
                        .summary(msg)
                        .source(eventId)
                        .build())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        Map reqBody = objectMapper.convertValue(payload,Map.class);
        customRestTemplate.callPostRestTemplate(reqBody, Constants.PAGERDUTY_URL);
        log.info("PAGERDUTY - EventId : {}, msg : {}", eventId, msg);
    }

    public void sendNotification(IntegrationPayload integrationPayload, String msg, String errorMsg) {
        String summary = String.format(Constants.NOTIFICATION_SUMMARY_FORMAT
                ,integrationPayload.getIntegrationEnvironment()
                ,integrationPayload.getIntegrationType()
                ,integrationPayload.getEventId()
                ,msg);

        String integrationCallTime = integrationPayload.getIntegrationCallTime().toString();
        integrationPayload.setIntegrationCallTime(null);
        CustomDetails customDetails = CustomDetails.builder().integrationPayload(integrationPayload).errorMsg(errorMsg).integrationCallTime(integrationCallTime).build();
        PagerDutyPayload payload = PagerDutyPayload.builder().payload(
                Payload.builder()
                        .summary(summary)
                        .source(integrationPayload.getEventId())
                        .custom_details(customDetails)
                        .build())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        Map reqBody = objectMapper.convertValue(payload,Map.class);
        customRestTemplate.callPostRestTemplate(reqBody, Constants.PAGERDUTY_URL);
        log.info("PAGERDUTY SUMMARY - {}", summary);
    }

    public void sendNotification(){
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
