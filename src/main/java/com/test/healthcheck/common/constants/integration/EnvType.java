package com.test.healthcheck.common.constants.integration;

public enum EnvType {
    DEV("###dev.com"), // 개발계는 빼기(10시에닫음)
    PRD("###prd.com"),
    SEC("###sec.com");

    private final String domain;

    EnvType(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

}
