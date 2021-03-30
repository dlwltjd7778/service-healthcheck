package com.opsnow.healthcheck.repository;

import com.opsnow.healthcheck.model.alertnow.EventId;
import org.springframework.data.repository.CrudRepository;

public interface EventIdRepository extends CrudRepository<EventId,String> {
}
