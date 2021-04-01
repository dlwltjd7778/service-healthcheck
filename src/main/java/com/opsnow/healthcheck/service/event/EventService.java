package com.opsnow.healthcheck.service.event;

import com.opsnow.healthcheck.common.constants.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    // event_id summary 생성
    public String getEventSummary(){
        return String.format(Constants.SUMMARY_FORMAT, Constants.ENVIRONMENT);
    }

    // event_id 생성
    public String getEventId(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-","");
    }


}
