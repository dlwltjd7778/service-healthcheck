package com.opsnow.healthcheck.controller;

import com.opsnow.healthcheck.service.extension.ExtensionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ExtensionController {
    private final ExtensionService extensionService;

    @PostMapping("/extension")
    public void alertIdCheck(@RequestBody Map<String, Object> reqMap){
        String eventId = (String) reqMap.get("id");
        log.info("get ALERT_EVENT_ID from extension : {}", eventId);
        extensionService.controllerFacade(eventId, ZonedDateTime.now());
    }

}
