package com.opsnow.healthcheck.service.incident;

import com.opsnow.healthcheck.common.CustomRestTemplate;
import com.opsnow.healthcheck.common.constants.Constants;
import com.opsnow.healthcheck.common.constants.integration.EnvType;
import com.opsnow.healthcheck.common.constants.notification.NotiErrorMsg;
import com.opsnow.healthcheck.model.alertnow.EventId;
import com.opsnow.healthcheck.model.alertnow.IntegrationPayload;
import com.opsnow.healthcheck.service.notification.NotificationService;
import com.opsnow.healthcheck.service.redis.EventIdListRedisService;
import com.opsnow.healthcheck.service.redis.PayloadRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentService {

    private final EventIdListRedisService eventIdListRedisService;
    private final PayloadRedisService payloadRedisService;
    private final NotificationService notificationService;
    private final CustomRestTemplate customRestTemplate;


    // 전체 로직 메소드
    public void controllerFacade(String eventId, ZonedDateTime controllerTime){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("redis 조회");
        // alertId로 redis에서 조회하기
        IntegrationPayload integrationPayload = payloadRedisService.getIntegrationPayloadByEventId(eventId);
        stopWatch.stop();

        if(integrationPayload==null){
            // pagerduty..? 레디스에 아이디가 없다.....
            notificationService.sendNotification(eventId, NotiErrorMsg.CANNOT_FIND_EVENT_ID_FROM_WEBHOOK.getNotiErrorMsg());
        } else {
            stopWatch.start("시간 비교");
            String status = integrationPayload.getIncidentCreationStatus();
            // 시간 비교
            if(compareTimeZone(integrationPayload.getIntegrationCallTime(), controllerTime)){ // 정상일 때
                payloadRedisService.changeIncidentStatus(eventId, Constants.INCIDENT_CREATED); // 상태 변경

            } else { // 5분 지났을 때
                payloadRedisService.changeIncidentStatus(eventId, Constants.INCIDENT_TIMEOUT); // 상태 변경
                notificationService.sendNotification(payloadRedisService.getIntegrationPayloadByEventId(eventId)
                        , NotiErrorMsg.TIMEOUT_IN_WEBHOOK.getNotiErrorMsg()
                        , NotiErrorMsg.TIMEOUT_IN_WEBHOOK.getNotiErrorMsg());
            }
            stopWatch.stop();
            stopWatch.start("인시던트 상태변경");
            changeIncidentStatus(integrationPayload);
            stopWatch.stop();
        }
        log.info("controllerFacade 시간 측정{}",stopWatch.prettyPrint());
    }

    public void incidentJobFacade(){

        Iterable<EventId> list =  eventIdListRedisService.getEventIdChkList();

        for (EventId eventIdObj : list) {
            // eventIdList를 순회한다.
            String eventId = eventIdObj.getEventId();

            // 하나의 eventId 로 저장된 정보 불러온다.
            IntegrationPayload integrationPayload = payloadRedisService.getIntegrationPayloadByEventId(eventId);
            if (integrationPayload == null) {
                notificationService.sendNotification(eventId, NotiErrorMsg.CANNOT_FIND_EVENT_ID_IN_REDIS.getNotiErrorMsg());
            } else {
                String nowStatus = integrationPayload.getIncidentCreationStatus();
                if (nowStatus.equals(Constants.INCIDENT_NOT_CREATED)) {
                    if (compareTimeZone(integrationPayload.getIntegrationCallTime(), ZonedDateTime.now())) {
                        continue;
                        // 아직 5분 안지났으면 아무것도 하지않는다.
                    } else {
                        payloadRedisService.changeIncidentStatus(eventId, Constants.INCIDENT_TIMEOUT); // 5분 지났으면 timeout으로 상태 변경
                        //pagerduty 발송
                        notificationService.sendNotification(integrationPayload
                                , NotiErrorMsg.TIMEOUT_IN_INCIDENTJOB.getNotiErrorMsg()
                                , NotiErrorMsg.TIMEOUT_IN_INCIDENTJOB.getNotiErrorMsg());
                    }
                }
            }

            // CREATED 상태와 TIMEOUT 상태는 리스트에서 지운다.
            eventIdListRedisService.deleteEventCheckListByEventId(eventId);
        }

    }

    public void changeIncidentStatus(IntegrationPayload integrationPayload){

        // 인시던트 상태를 close로 변경
        String env = integrationPayload.getIntegrationEnvironment();
        String type = integrationPayload.getIntegrationType();
        String apiKey = integrationPayload.getIntegrationKey();
        String domain = "";
        for(EnvType envType : EnvType.values()){
            if(env.equals(envType.toString())){
                domain = envType.getDomain();
                break;
            }
        }
        String url = String.format(Constants.INTEGRATION_URL_FORMAT,domain,type,apiKey);
        log.info("integration url(incident status close) : {}", url);
        Map<String, String> reqBody = new HashMap<>();
        reqBody.put("summary", integrationPayload.getEventSummary());
        reqBody.put("event_id", integrationPayload.getEventId());
        reqBody.put("status", "close");

        customRestTemplate.callPostRestTemplate(reqBody,url);
        log.info("Incident open->close [{}]{}#{}", env,type, integrationPayload.getEventId());
    }

    public boolean compareTimeZone(ZonedDateTime integrationCallTime, ZonedDateTime compareTime){
        return integrationCallTime.plusMinutes(Constants.SLA_MINUTES).isAfter(compareTime);
        // 인티그레이션 호출한 시간 + 5분 보다 controllerTime 이 더 이전이면 true >> 5분 안지났으면 true
    }

}
