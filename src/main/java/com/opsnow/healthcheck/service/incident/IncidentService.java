package com.opsnow.healthcheck.service.incident;

import com.opsnow.healthcheck.common.Constants;
import com.opsnow.healthcheck.model.alertnow.EventId;
import com.opsnow.healthcheck.model.alertnow.IntegrationPayload;
import com.opsnow.healthcheck.service.pagerduty.PagerDutyService;
import com.opsnow.healthcheck.service.redis.EventIdService;
import com.opsnow.healthcheck.service.redis.IntegrationPayloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Iterator;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentService {

    private final EventIdService eventIdService;
    private final IntegrationPayloadService integrationPayloadService;
    private final PagerDutyService pagerDutyService;

    // 전체 로직 메소드
    public void controllerFacade(String eventId, ZonedDateTime controllerTime){
        // alertId로 redis에서 조회하기
        log.info("상태 변경 대상 id > {}", eventId);
        IntegrationPayload integrationPayload = integrationPayloadService.getIntegrationPayloadByEventId(eventId);


        if(integrationPayload==null){
            // pagerduty..? 레디스에 아이디가 없다.....
            pagerDutyService.sendPagerDuty(eventId, "레디스에 Id가 없다..");
        } else {
            String status = integrationPayload.getIncidentCreationStatus();
            // 시간 비교
            if(compareTimeZone(integrationPayload.getIntegrationCallTime(), controllerTime)){ // 정상일 때
                integrationPayloadService.changeIncidentStatus(eventId, Constants.INCIDENT_CREATED); // 상태 변경
                log.warn("{} 의 상태를 created 로 변경", eventId);

            } else { // 5분 지났을 때
                integrationPayloadService.changeIncidentStatus(eventId, Constants.INCIDENT_TIMEOUT); // 상태 변경
                pagerDutyService.sendPagerDuty(eventId, "5분지났는데 안만들어졌다니..");
            }
        }

    }

    public void jobFacade(){

        Iterable<EventId> list =  eventIdService.getEventIdChkList();
        Iterator<EventId> iterList = list.iterator();

        while(iterList.hasNext()){
            // eventIdList를 순회한다.
            EventId eventIdObj = iterList.next();
            String eventId = eventIdObj.getEventId();

            // 하나의 eventId 로 저장된 정보 불러온다.
            IntegrationPayload integrationPayload = integrationPayloadService.getIntegrationPayloadByEventId(eventId);
            if (integrationPayload==null) continue;
            String nowStatus = integrationPayload.getIncidentCreationStatus();
            if (nowStatus.equals(Constants.INCIDENT_NOT_CREATED)){
                if(compareTimeZone(integrationPayload.getIntegrationCallTime(), ZonedDateTime.now())){
                    continue;
                     // 아직 5분 안지났으면 아무것도 하지않는다.
                } else {
                    integrationPayloadService.changeIncidentStatus(eventId,Constants.INCIDENT_TIMEOUT); // 5분 지났으면 timeout으로 상태 변경
                    //pagerduty 발송
                    pagerDutyService.sendPagerDuty(eventId,"INCIDENTJOB-TIMEOUT");
                }
            }
            // CREATED 상태와 TIMEOUT 상태는 리스트에서 지운다.
            eventIdService.deleteEventCheckListByEventId(eventId);
        }

    }

    public boolean compareTimeZone(ZonedDateTime integrationCallTime, ZonedDateTime compareTime){
//        log.info("integrationCallTime : {}", integrationCallTime);
//        log.info("integrationCallTime+5min : {}", integrationCallTime.plusMinutes(5));
//        log.info("compareTime : {}", compareTime);
        return integrationCallTime.plusMinutes(Constants.SLA_MINUTES).isAfter(compareTime);
        // 인티그레이션 호출한 시간 + 5분 보다 controllerTime 이 더 이전이면 true >> 5분 안지났으면 true
    }

}
