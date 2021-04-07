package com.opsnow.healthcheck.common.constants.integration;

public enum EnvType {
    DEV("alertnowitgrdev.opsnow.com"), // 개발계는 빼기(10시에닫음)
    PRD("alertnowitgr.opsnow.com"),
    SEC("alertnowitgr.sec-alertnow.com");

    private final String domain;

    EnvType(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

}
