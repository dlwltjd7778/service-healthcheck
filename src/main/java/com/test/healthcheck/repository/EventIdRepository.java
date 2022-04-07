package com.test.healthcheck.repository;

import com.test.healthcheck.model.incdtmg.EventId;
import org.springframework.data.repository.CrudRepository;

public interface EventIdRepository extends CrudRepository<EventId,String> {
}
