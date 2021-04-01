package com.opsnow.healthcheck;

import com.opsnow.healthcheck.common.constants.Constants;
import com.opsnow.healthcheck.common.constants.ConstantsEnum;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@EnableScheduling       // 스케줄러 기능 활성화
@EnableBatchProcessing  // 배치 기능 활성화
@SpringBootApplication
public class HealthCheckApplication {
    public static void main(String[] args) {
        SpringApplication.run(HealthCheckApplication.class, args);
//        List<String> list = ConstantsEnum.IntegrationUrl.getAllUrl();
//        for(String s:list){
//            System.out.println(s);
//        }
//
//        Constants.ENVIRONMENT = ConstantsEnum.EnvType.DEV.toString();
//        System.out.println("1. "+Constants.ENVIRONMENT);
//        Constants.ENVIRONMENT = ConstantsEnum.EnvType.PRD.toString();
//        System.out.println("2. "+Constants.ENVIRONMENT);

    }
}
