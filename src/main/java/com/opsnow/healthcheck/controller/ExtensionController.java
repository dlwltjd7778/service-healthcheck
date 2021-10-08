package com.opsnow.healthcheck.controller;

import com.opsnow.healthcheck.service.incident.IncidentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ExtensionController {
    private final IncidentService incidentService;

    @PostMapping("/extension")
    public void alertIdCheck(@RequestBody Map<String, Object> reqMap){
        final StopWatch stopwatch = new StopWatch();

        log.info("**--**--**--**--**  START Controller ( extension ) **--**--**--**--**");
        stopwatch.start("controller time");
        String eventId = (String) reqMap.get("id");
        log.info("get ALERT_EVENT_ID from extension : {}", eventId);
        incidentService.controllerFacade(eventId, ZonedDateTime.now());
        stopwatch.stop();
        log.info("controller time >>> {}", stopwatch.prettyPrint());
        log.info("**--**--**--**--**  END Controller ( extension ) **--**--**--**--**");
    }

}
