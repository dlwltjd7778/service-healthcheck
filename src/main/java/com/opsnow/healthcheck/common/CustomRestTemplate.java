package com.opsnow.healthcheck.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opsnow.healthcheck.common.constants.notification.NotiErrorMsg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomRestTemplate {

    private final RestTemplateBuilder restTemplateBuilder;

    public Map<Object, Object> callPostRestTemplate(Map<String,String> reqBody, String URL)  {

        Map<Object, Object> resMap = new HashMap<>();
        RestTemplate restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(10)).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // http entity에 body, header 담아줌
        HttpEntity entity = new HttpEntity<>(reqBody,headers);

        try {
            // api 호출
            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
            ObjectMapper mapper = new ObjectMapper();
            resMap = mapper.readValue(response.getBody(), Map.class);

        } catch (HttpStatusCodeException e){
            resMap.put("msg", NotiErrorMsg.NOT_200_OK.getNotiErrorMsg() + " : " + e.getClass().getName() + "-" + e.getStatusCode().toString());
            log.error("ERROR MSG - {}", resMap.get("msg"), e);
        } catch (Exception e){
            resMap.put("msg",NotiErrorMsg.INTEGRATION_API_RESTTEMPLATE_ERROR.getNotiErrorMsg() + e.getClass().getName());
            resMap.put("cause", e.getMessage());
            log.error("ERROR MSG - {}", resMap.get("msg"), e);
        }

        return resMap;


    }
}
