package com.best.travel.best_travel.domain.repository;

import org.springframework.data.repository.CrudRepository;

import com.best.travel.best_travel.domain.entity.CustomerEntity;

public interface CustomerRepository extends CrudRepository<CustomerEntity, String> {
    
}
