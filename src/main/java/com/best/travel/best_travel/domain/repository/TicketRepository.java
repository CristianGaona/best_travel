package com.best.travel.best_travel.domain.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.best.travel.best_travel.domain.entity.TicketEntity;

public interface TicketRepository extends CrudRepository<TicketEntity, UUID> {
    
}
