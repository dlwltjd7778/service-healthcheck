package com.opsnow.healthcheck.common;

public class Constants {

    // Integration URL
    public final static String STANDARD_INTEGRATION_URL = "https://alertnowitgrdev.opsnow.com/integration/standard/v1/6672be9c48620311eb1b86430a960e68e6c8";


    // PagerDuty URL, payload
    public final static String PAGERDUTY_URL = "https://events.pagerduty.com/v2/enqueue";
    public final static String PAYLOAD_SUMMARY = "Integration API call Error";
    public final static String PAYLOAD_SOURCE = "host name or FQDN here";
    public final static String PAYLOAD_SEVERITY = "critical";
    public final static String ROUTING_KEY = "10eb9a1b52e44409c007e2a274025fe2";
    public final static String EVENT_ACTION = "trigger";

    // Integration payload - Summary
    public final static String SUMMARY_FORMAT = "[%s]Status Test";

    // JobScheduler 주기
    public final static String INTEGRATION_CRON_EXPRESSION = "0 0/1 * * * *";
    public final static String INCIDENT_CRON_EXPRESSION = "0 0/2 * * * *";

    // Incidents Creation Status
    public final static String INCIDENT_CREATED = "created";
    public final static String INCIDENT_NOT_CREATED = "notCreated";
    public final static String INCIDENT_TIMEOUT = "timeout";

    // SLA minutes
    public final static int SLA_MINUTES = 5;

    // Redis TTL
    public final static int INTEGRATION_PAYLOAD_TTL = 1000000000;

}
