package com.test.healthcheck.service.event;

import com.test.healthcheck.common.constants.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    // event_id summary 생성
    public String getEventSummary(String uuid){
        return String.format(Constants.SUMMARY_FORMAT, Constants.INTEGRATION_ENVIRONMENT,Constants.INTEGRATION_TYPE, uuid);
    }

    // event_id 생성
    public String getEventId(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-","");
    }


}
