package com.best.travel.best_travel.domain.repository.jpa;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.best.travel.best_travel.domain.entity.jpa.ReservationEntity;

public interface ReservationRepository extends CrudRepository<ReservationEntity, UUID> {
    
}
