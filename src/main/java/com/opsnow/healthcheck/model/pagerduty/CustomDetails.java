package com.opsnow.healthcheck.model.pagerduty;

import com.opsnow.healthcheck.model.alertnow.IntegrationPayload;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomDetails {

    private IntegrationPayload integrationPayload;
    private String errorMsg;
    private String integrationCallTime;
}
