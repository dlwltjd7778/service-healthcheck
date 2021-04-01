package com.opsnow.healthcheck.service.integration;

import com.opsnow.healthcheck.service.event.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class IntegrationPayloadServiceImpl implements IntegrationPayloadService {

    private final EventService eventService;

    @Override
    public Map<String, String> makeStandardPayload() {
        log.info(">> makeStandardPayload 시작");

        // event id와 summary 가져오기
        String eventId = eventService.getEventId();
        String eventSummary = eventService.getEventSummary(eventId);
        log.info("eventId: {}, eventSummary: {}", eventId, eventSummary);

        // body 작성
        Map<String, String> reqBody = new HashMap<>();
        reqBody.put("summary", eventSummary);
        reqBody.put("event_id", eventId);

        return reqBody;
    }


}
