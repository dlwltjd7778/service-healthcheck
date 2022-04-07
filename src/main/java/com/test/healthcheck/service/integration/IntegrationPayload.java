package com.test.healthcheck.service.integration;

import java.util.Map;

public interface IntegrationPayload {

    Map<String, String> makePayload(String status);
}
