package com.opsnow.healthcheck.model.alertnow;


import com.opsnow.healthcheck.common.constants.Constants;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.ZonedDateTime;

@Data
@Builder
@RedisHash(value = "HealthCheckPayload", timeToLive = Constants.INTEGRATION_PAYLOAD_TTL)
public class IntegrationPayload {
    @Id
    private String eventId;                      // uuid
    private String eventSummary;                 // summary
    private String environment;                  // env
    private String url;                          // url
    private String integrationType;              // integration type
    private ZonedDateTime integrationCallTime;   // call time
    private String incidentCreationStatus;

}
