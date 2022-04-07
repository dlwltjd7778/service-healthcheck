package com.test.healthcheck.model.incdtmg;


import com.test.healthcheck.common.constants.Constants;
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
    private String integrationKey;               // integration Key
    private String integrationType;              // integration type
    private String integrationEnvironment;       // integration env
    private ZonedDateTime integrationCallTime;   // integration call time
    private String incidentCreationStatus;       // incident 생성 여부

}
