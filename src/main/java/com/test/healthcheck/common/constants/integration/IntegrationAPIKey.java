package com.test.healthcheck.common.constants.integration;


public enum IntegrationAPIKey {
    SEC_STANDARD_URL(EnvType.SEC, IntegrationType.standard, "###apikey"),
    PRD_STANDARD_URL(EnvType.PRD, IntegrationType.standard, "###apikey");

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
