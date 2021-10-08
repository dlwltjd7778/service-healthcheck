package com.opsnow.healthcheck.service.redis;

import com.opsnow.healthcheck.common.constants.Constants;
import com.opsnow.healthcheck.model.alertnow.IntegrationPayload;
import com.opsnow.healthcheck.repository.PayloadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayloadRedisService {

    private final PayloadRepository payloadRepository;

    public IntegrationPayload saveEventData(String eventId, String eventSummary, String integrationType, ZonedDateTime integrationCallTime, String integrationKey, String env){
        IntegrationPayload integrationPayload = IntegrationPayload.builder()
                .eventId(eventId)
                .eventSummary(eventSummary)
                .integrationType(integrationType)
                .integrationKey(integrationKey)
                .integrationCallTime(integrationCallTime)
                .incidentCreationStatus(Constants.INCIDENT_NOT_CREATED)
                .integrationEnvironment(env)
                .build();
        IntegrationPayload result = payloadRepository.save(integrationPayload);
        log.info("save IntegrationPayload in Redis : {}", integrationPayload );
        return result;
    }

    // eventId를 가지고 조회
    public IntegrationPayload getIntegrationPayloadByEventId(String eventId) {
        Optional<IntegrationPayload> integrationPayload = payloadRepository.findById(eventId);
        return integrationPayload.orElse(null);
    }

    public void deleteIntegrationPayloadByEventId(String eventId){
        payloadRepository.deleteById(eventId);
        log.info("delete from HealthCheckPayload - eventId : {}",eventId);
    }
    
    public void changeIncidentStatus(String eventId, String status){
        IntegrationPayload integrationPayload = getIntegrationPayloadByEventId(eventId);
        String nowStatus = integrationPayload.getIncidentCreationStatus();
        integrationPayload.setIncidentCreationStatus(status);
        payloadRepository.save(integrationPayload);
        log.warn("{} 의 incidentCreationStatus 를 {} -> {} 로 변경", eventId, nowStatus, status);
    }

}
