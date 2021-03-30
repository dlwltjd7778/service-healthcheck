package com.opsnow.healthcheck.model.alertnow;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder
@RedisHash("HealthCheckEventIdList")
public class EventId {
    @Id
    private String eventId;
}
