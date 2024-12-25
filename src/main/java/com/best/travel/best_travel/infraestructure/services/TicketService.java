package com.best.travel.best_travel.infraestructure.services;

import com.best.travel.best_travel.api.models.request.TicketRequest;
import com.best.travel.best_travel.api.models.responses.FlyResponse;
import com.best.travel.best_travel.api.models.responses.TicketResponse;
import com.best.travel.best_travel.domain.entity.TicketEntity;
import com.best.travel.best_travel.domain.repository.CustomerRepository;
import com.best.travel.best_travel.domain.repository.FlyRepository;
import com.best.travel.best_travel.domain.repository.TicketRepository;
import com.best.travel.best_travel.infraestructure.asbtract_services.ITicketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class TicketService implements ITicketService {

    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;

    @Override
    public TicketResponse create(TicketRequest request) {

        var fly = flyRepository.findById(request.getIdFly()).orElseThrow();
        var customer = customerRepository.findById(request.getIdClient()).orElseThrow();

        var ticketToPersist = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().multiply(BigDecimal.valueOf(0.25)))
                .purchaseDate(LocalDate.now())
                .departureDate(LocalDateTime.now())
                .arrivalDate(LocalDateTime.now())
                .build();
        var ticketPersisted = ticketRepository.save(ticketToPersist);
        log.info("Ticket created with id: {}", ticketPersisted.getId());
        return entityToResponse(ticketPersisted);
    }

    @Override
    public TicketResponse read(UUID uuid) {

        var ticketFromDb = this.ticketRepository.findById(uuid).orElseThrow();
        return this.entityToResponse(ticketFromDb);
    }

    @Override
    public TicketResponse update(UUID uuid, TicketRequest request) {

        var ticketToUpdate = this.ticketRepository.findById(uuid).orElseThrow();
        var fly = flyRepository.findById(request.getIdFly()).orElseThrow();

        ticketToUpdate.setFly(fly);
        ticketToUpdate.setPrice(BigDecimal.valueOf(0.25));
        ticketToUpdate.setDepartureDate(LocalDateTime.now());
        ticketToUpdate.setArrivalDate(LocalDateTime.now());

        var ticketUpdated = this.ticketRepository.save(ticketToUpdate);
        log.info("Ticket updated with id {}", ticketUpdated.getId());

        return this.entityToResponse(ticketUpdated);
    }

    @Override
    public TicketResponse findById(UUID uuid) {
        return null;
    }

    @Override
    public void delete(UUID uuid) {
        var ticketToDelete = this.ticketRepository.findById(uuid).orElseThrow();
        this.ticketRepository.delete(ticketToDelete);
    }

    private TicketResponse entityToResponse(TicketEntity entity){
        var response = new TicketResponse();
        BeanUtils.copyProperties(entity, response);
        var flyResponse = new FlyResponse();
        BeanUtils.copyProperties(entity.getFly(), flyResponse);
        response.setFly(flyResponse);
        return response;
    }
}
