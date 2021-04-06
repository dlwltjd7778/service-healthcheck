package com.opsnow.healthcheck.common.constants;

public class Constants {

    // Format
    public final static String INTEGRATION_URL_FORMAT = "https://%s/integration/%s/v1/%s";
    public final static String NOTIFICATION_SUMMARY_FORMAT = "[%s][%s#%s] %s";
    // Integration payload - Summary Format
    public final static String SUMMARY_FORMAT = "[%s][%s#%s] Status Test";

    // PagerDuty URL, payload
    public final static String PAGERDUTY_URL = "https://events.pagerduty.com/v2/enqueue";
    public final static String PAYLOAD_SUMMARY = "Integration API call Error";
    public final static String PAYLOAD_SOURCE = "host name or FQDN here";
    public final static String PAYLOAD_SEVERITY = "critical";
    public final static String ROUTING_KEY = "3e6fccb4ddba4904d0298d87598e49dc";
    public final static String EVENT_ACTION = "trigger";

    // JobScheduler 주기
    public final static String INTEGRATION_CRON_EXPRESSION = "0 0/1 * * * *";
    public final static String INCIDENT_CRON_EXPRESSION = "30 0/1 * * * *";

    // Incidents Creation Status
    public final static String INCIDENT_CREATED = "created";
    public final static String INCIDENT_NOT_CREATED = "notCreated";
    public final static String INCIDENT_TIMEOUT = "timeout";

    // SLA minutes
    public final static int SLA_MINUTES = 5;

    // Redis TTL
    public final static int INTEGRATION_PAYLOAD_TTL = 10000;

    public static String INTEGRATION_ENVIRONMENT;
    public static String INTEGRATION_TYPE;
    public static String INTEGRATION_URL;

}
