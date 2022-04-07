package com.test.healthcheck.repository;

import com.test.healthcheck.model.incdtmg.IntegrationPayload;
import org.springframework.data.repository.CrudRepository;

public interface PayloadRepository extends CrudRepository<IntegrationPayload,String> {
}
