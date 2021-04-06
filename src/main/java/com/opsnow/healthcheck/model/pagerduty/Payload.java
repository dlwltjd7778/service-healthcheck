package com.opsnow.healthcheck.model.pagerduty;

import com.opsnow.healthcheck.common.constants.Constants;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payload{
    @Builder.Default
    private String summary = Constants.PAYLOAD_SUMMARY;
    @Builder.Default
    private String source = Constants.PAYLOAD_SOURCE;
    @Builder.Default
    private String severity = Constants.PAYLOAD_SEVERITY;
    private CustomDetails custom_details;
}
