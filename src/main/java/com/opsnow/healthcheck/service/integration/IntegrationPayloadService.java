package com.opsnow.healthcheck.service.integration;

import java.util.Map;

public interface IntegrationPayloadService {

    Map<String, String> makePayload();
}
