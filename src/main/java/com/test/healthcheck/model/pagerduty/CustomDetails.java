package com.test.healthcheck.model.pagerduty;

import com.test.healthcheck.model.incdtmg.IntegrationPayload;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomDetails {

    private IntegrationPayload integrationPayload;
    private String errorMsg;
    private String integrationCallTime;
}
