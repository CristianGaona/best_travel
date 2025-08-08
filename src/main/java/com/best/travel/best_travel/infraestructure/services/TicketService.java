package com.best.travel.best_travel.infraestructure.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.best.travel.best_travel.api.models.request.TicketRequest;
import com.best.travel.best_travel.api.models.responses.FlyResponse;
import com.best.travel.best_travel.api.models.responses.TicketResponse;
import com.best.travel.best_travel.domain.entity.TicketEntity;
import com.best.travel.best_travel.domain.repository.CustomerRepository;
import com.best.travel.best_travel.domain.repository.FlyRepository;
import com.best.travel.best_travel.domain.repository.TicketRepository;
import com.best.travel.best_travel.infraestructure.asbtract_services.ITicketService;
import com.best.travel.best_travel.infraestructure.helpers.BlockListHelper;
import com.best.travel.best_travel.infraestructure.helpers.CustomerHelper;
import com.best.travel.best_travel.util.BestTravelUtil;
import com.best.travel.best_travel.util.enums.Tables;
import com.best.travel.best_travel.util.exceptions.IdNotFoundException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional(readOnly = true)
@Service
@Slf4j
@AllArgsConstructor
public class TicketService implements ITicketService {

    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final CustomerHelper customerHelper;
    private final BlockListHelper blockListHelper;

    @Override
    public TicketResponse create(TicketRequest request) {
        blockListHelper.isInBlackListCustomer(request.getIdClient());
        var fly = flyRepository.findById(request.getIdFly()).orElseThrow(() -> new IdNotFoundException(Tables.fly.name()));
        var customer = customerRepository.findById(request.getIdClient()).orElseThrow(() -> new IdNotFoundException(Tables.customer.name()));

        var ticketToPersist = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().add(fly.getPrice().multiply(CHARGER_PRICE_PERCENTAGE)))
                .purchaseDate(LocalDate.now())
                .departureDate(BestTravelUtil.getRandomSoon())
                .arrivalDate(BestTravelUtil.getRandomLatrer())
                .build();
        var ticketPersisted = ticketRepository.save(ticketToPersist);
        this.customerHelper.increase(customer.getDni(), TicketService.class);
        log.info("Ticket created with id: {}", ticketPersisted.getId());
        return entityToResponse(ticketPersisted);
    }

    @Override
    public TicketResponse read(UUID uuid) {

        var ticketFromDb = this.ticketRepository.findById(uuid).orElseThrow(() -> new IdNotFoundException(Tables.ticket.name()));
        return this.entityToResponse(ticketFromDb);
    }

    @Override
    public TicketResponse update(UUID uuid, TicketRequest request) {

        var ticketToUpdate = this.ticketRepository.findById(uuid).orElseThrow(() -> new IdNotFoundException(Tables.ticket.name()));
        var fly = flyRepository.findById(request.getIdFly()).orElseThrow(() -> new IdNotFoundException(Tables.fly.name()));

        ticketToUpdate.setFly(fly);
        ticketToUpdate.setPrice(fly.getPrice().add(fly.getPrice().multiply(CHARGER_PRICE_PERCENTAGE)));
        ticketToUpdate.setDepartureDate(BestTravelUtil.getRandomSoon());
        ticketToUpdate.setArrivalDate(BestTravelUtil.getRandomLatrer());

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
        var ticketToDelete = this.ticketRepository.findById(uuid).orElseThrow(() -> new IdNotFoundException(Tables.ticket.name()));
        this.ticketRepository.delete(ticketToDelete);
    }

    @Override
    public BigDecimal findPrice(Long flyId) {
        var fly = this.flyRepository.findById(flyId).orElseThrow(() -> new IdNotFoundException(Tables.fly.name()));
        return fly.getPrice().add(fly.getPrice().multiply(CHARGER_PRICE_PERCENTAGE));
    }

    private TicketResponse entityToResponse(TicketEntity entity){
        var response = new TicketResponse();
        BeanUtils.copyProperties(entity, response);
        var flyResponse = new FlyResponse();
        BeanUtils.copyProperties(entity.getFly(), flyResponse);
        response.setFly(flyResponse);
        return response;
    }

    public static final BigDecimal CHARGER_PRICE_PERCENTAGE = BigDecimal.valueOf(0.25);
}
