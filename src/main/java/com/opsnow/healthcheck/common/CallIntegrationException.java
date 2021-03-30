package com.opsnow.healthcheck.common;

public class CallIntegrationException extends Exception{
    public CallIntegrationException(){
        super("응답으로 200ok 못 받음");
    }
}
