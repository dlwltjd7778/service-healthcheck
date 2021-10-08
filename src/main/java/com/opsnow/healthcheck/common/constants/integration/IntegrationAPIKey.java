package com.opsnow.healthcheck.common.constants.integration;


public enum IntegrationAPIKey {
    SEC_STANDARD_URL(EnvType.SEC, IntegrationType.standard, "332f15af19b35611eb395b1312695202b80b"),
    PRD_STANDARD_URL(EnvType.PRD, IntegrationType.standard, "2090be2d7c1c5511eb6a7bf20a30cde53ce4");

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