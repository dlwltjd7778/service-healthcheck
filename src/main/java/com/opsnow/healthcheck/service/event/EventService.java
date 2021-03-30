package com.opsnow.healthcheck.service.event;

import com.opsnow.healthcheck.common.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    @Value("${spring.profiles.active}")
    String profile;

    // event_id summary 생성
    public String getEventSummary(){
        return String.format(Constants.SUMMARY_FORMAT, profile);
    }

    // event_id 생성
    public String getEventId(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-","");
    }


}
