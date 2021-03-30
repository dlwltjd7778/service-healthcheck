package com.opsnow.healthcheck.repository;

import com.opsnow.healthcheck.model.alertnow.IntegrationPayload;
import org.springframework.data.repository.CrudRepository;

public interface PayloadRepository extends CrudRepository<IntegrationPayload,String> {
}
