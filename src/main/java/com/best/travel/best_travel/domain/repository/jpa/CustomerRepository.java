package com.best.travel.best_travel.domain.repository.jpa;

import org.springframework.data.repository.CrudRepository;

import com.best.travel.best_travel.domain.entity.jpa.CustomerEntity;

public interface CustomerRepository extends CrudRepository<CustomerEntity, String> {
    
}
