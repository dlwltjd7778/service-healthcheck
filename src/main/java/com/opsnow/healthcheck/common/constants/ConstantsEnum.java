package com.opsnow.healthcheck.common.constants;

import lombok.Getter;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;

import java.util.Collections;

public class ConstantsEnum {

    // 개발 환경별 도메인
    public enum EnvType {
        DEV("https://alertnowitgrdev.opsnow.com"),
        PRD("domain-prd");

        private final String domain;

        EnvType(String domain) {
            this.domain = domain;
        }

        public String getDomain() {
            return domain;
        }

    }

    // Integration type
    public enum IntegrationType {
        STANDARD,
        DATADOG,
        CLOUDWATCH
    }

    // 실제 사용되는 URL
    public enum IntegrationUrl {
        DEV_STANDARD_URL(EnvType.DEV, IntegrationType.STANDARD, "6672be9c48620311eb1b86430a960e68e6c8");

        private final String apiKey;
        private final EnvType envType;
        private final IntegrationType integrationType;

        IntegrationUrl(EnvType envType, IntegrationType integrationType, String apiKey) {
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

        public String getUrl(IntegrationUrl url) {
            String FORMAT = "%s/integration/%s/v1/%s";
            return String.format(FORMAT, url.getEnvType().getDomain(), url.getIntegrationType(), url.getApiKey());
        }
    }



}
