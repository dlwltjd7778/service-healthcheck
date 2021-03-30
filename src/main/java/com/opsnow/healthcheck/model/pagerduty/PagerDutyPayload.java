package com.opsnow.healthcheck.model.pagerduty;

import com.opsnow.healthcheck.common.Constants;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagerDutyPayload {

    @Builder.Default
    private String routing_key = Constants.ROUTING_KEY;
    @Builder.Default
    private String event_action = Constants.EVENT_ACTION;
    private Payload payload;


}
