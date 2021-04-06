package com.opsnow.healthcheck.common.constants.integration;


public enum IntegrationAPIKey {
    DEV_STANDARD_URL(EnvType.DEV, IntegrationType.standard, "6672be9c48620311eb1b86430a960e68e6c8"),
        DEV_STANDARD_URL2(EnvType.DEV, IntegrationType.standard, "830c7faf292a3511eb7b86420a960e68e6c8");

    private final String apiKey;
    private final EnvType envType;
    private final IntegrationType integrationType;

    IntegrationAPIKey(EnvType envType, IntegrationType integrationType, String apiKey) {
        this.envType = envType;
        this.integrationType = integrationType;
        this.apiKey = apiKey;
    }

    public EnvType getEnvType() {
        return envType;
    }
    public IntegrationType getIntegrationType() {
        return integrationType;
    }
    public String getApiKey() {
        return apiKey;
    }

}