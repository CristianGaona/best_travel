package com.best.travel.best_travel.domain.repository.jpa;

import org.springframework.data.repository.CrudRepository;

import com.best.travel.best_travel.domain.entity.jpa.TourEntity;

public interface TourRepository extends CrudRepository<TourEntity, Long> {

    
    
}
