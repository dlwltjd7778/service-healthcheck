package com.opsnow.healthcheck;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling       // 스케줄러 기능 활성화
@EnableBatchProcessing  // 배치 기능 활성화
@SpringBootApplication
public class HealthCheckApplication {
    public static void main(String[] args) {
        SpringApplication.run(HealthCheckApplication.class, args);
    }
}
