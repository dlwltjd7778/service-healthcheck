package com.opsnow.healthcheck.common.constants;

public class PagerDutyEnum {

    public enum CauseMsg{
        INTEGRATION_API_RESTTEMPLATE_ERROR("RestTemplate 에러 : "),
        NOT_200_OK("Integration 을 호출했으나, 정상적 응답인 \"200,ok\" 가 오지 않음. producer 문제로 예상됨."),
        TIMEOUT_IN_INTEGRATION_API_CONNECTION("RestTemplate으로 Integration api 호출할 때, TimeOut"),

        TIMEOUT_IN_INCIDENTJOB("Integration 호출 시간으로부터 5분이 경과했지만, Webhook을 통한 알림을 받지 못함."),
        TIMEOUT_IN_WEBHOOK("Integration 호출 시간으로부터 5분이 지나서 Webhook을 통해 알림이 전송되었다. SLA에 위배됨"),
        CANNOT_FIND_EVENT_ID_IN_WEBHOOK("WebHook으로 전송된 UUID 가 DB(Redis)에 저장되어 있지 않음.");




        String causeMsg;
        CauseMsg(String causeMsg) {
            this.causeMsg = causeMsg;
        }

        public String getCauseMsg() {
            return causeMsg;
        }
    }


}
