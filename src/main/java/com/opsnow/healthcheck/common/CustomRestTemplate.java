package com.opsnow.healthcheck.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomRestTemplate {

    private final RestTemplateBuilder restTemplateBuilder;

    public Map<Object, Object> callPostRestTemplate(Map<String,String> reqBody, String URL)  {

        RestTemplate restTemplate = restTemplateBuilder.build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // http entity에 body, header 담아줌
        HttpEntity entity = new HttpEntity<>(reqBody,headers);

        // api 호출
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);

        ObjectMapper mapper = new ObjectMapper();

        Map<Object, Object> resMap = null;
        try {
            resMap = mapper.readValue(response.getBody(), Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return resMap;
    }
}
