package com.opsnow.healthcheck.service.redis;

import com.opsnow.healthcheck.common.Constants;
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
public class IntegrationPayloadService {

    private final PayloadRepository payloadRepository;

    public void saveEventData(String eventId, String eventSummary, String integrationType, ZonedDateTime integrationCallTime, String url){
        IntegrationPayload integrationPayload = IntegrationPayload.builder()
                .eventId(eventId)
                .eventSummary(eventSummary)
                .integrationType(integrationType)
                .url(url)
                .integrationCallTime(integrationCallTime)
                .incidentCreationStatus(Constants.INCIDENT_NOT_CREATED)
                .build();
        payloadRepository.save(integrationPayload);
        log.info("save IntegrationPayload in Redis : {}", integrationPayload );
    }

    // eventId를 가지고 조회
    public IntegrationPayload getIntegrationPayloadByEventId(String eventId) {
        Optional<IntegrationPayload> integrationPayload = payloadRepository.findById(eventId);
        if(integrationPayload.isPresent()){
            return integrationPayload.get();
        }
        return null;
    }

    public void changeIncidentStatus(String eventId, String status){
        IntegrationPayload integrationPayload = getIntegrationPayloadByEventId(eventId);
        integrationPayload.setIncidentCreationStatus(status);
        payloadRepository.save(integrationPayload);
    }

}
