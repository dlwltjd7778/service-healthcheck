package com.opsnow.healthcheck.common.constants;

import java.util.ArrayList;
import java.util.List;

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
        standard,
        datadog,
        cloudwatch
    }

    // IntegrationUrl 실제 사용되는 URL
    public enum IntegrationUrl {
        DEV_STANDARD_URL(EnvType.DEV, IntegrationType.standard, "6672be9c48620311eb1b86430a960e68e6c8"),
        PRD_TEST_URL(EnvType.PRD, IntegrationType.datadog,"1234abcd");

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



        public List<String> getAllUrl(){
            List<String> result = new ArrayList<>();
            for (IntegrationUrl url : values()){
                result.add(getUrl(url));
            }
            return result;
        }

    }
    public static String getUrl(IntegrationUrl url) {
        return String.format(Constants.INTEGRATION_URL_FORMAT, url.getEnvType().getDomain(), url.getIntegrationType(), url.getApiKey());
    }

    /*
        -- 어디서 쓸건가
        1. integrationjob
            - 얼럿나우 인시던트 Summary 만들 때
            - 보낼 url 만들 때 ( url을 리스트로 만들어서 돌리려고 했었다. )
            - url, integration type 레디스에 저장할 때
        2. incedentjob
            - pagerduty summary 만들 때
            - 로그 찍을 때..? 등..

        -- 문제점
        원래는 public static 변수를 만들어서 실행할 URL 을 순회할 때, 변수의 값들이 변경되게 하려고 했는데
        job이 제각각 실행되기 때문에 변수에 저장된 값이 실제 원하는 값인지 알 수 없다..

        해결 방안.? redis에 현재 integration type은 저장되어 있기 때문에 개발환경을 저장해주면 불러오기가 가능하지 않을까..?

     */


}
