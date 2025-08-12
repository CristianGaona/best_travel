package com.best.travel.best_travel.domain.repository.jpa;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.best.travel.best_travel.domain.entity.jpa.TicketEntity;

public interface TicketRepository extends CrudRepository<TicketEntity, UUID> {
    
}
