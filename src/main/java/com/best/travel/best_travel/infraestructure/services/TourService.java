package com.best.travel.best_travel.infraestructure.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.best.travel.best_travel.api.models.request.TourRequest;
import com.best.travel.best_travel.api.models.responses.TourResponse;
import com.best.travel.best_travel.domain.entity.FlyEntity;
import com.best.travel.best_travel.domain.entity.HotelEntity;
import com.best.travel.best_travel.domain.entity.ReservationEntity;
import com.best.travel.best_travel.domain.entity.TicketEntity;
import com.best.travel.best_travel.domain.entity.TourEntity;
import com.best.travel.best_travel.domain.repository.CustomerRepository;
import com.best.travel.best_travel.domain.repository.FlyRepository;
import com.best.travel.best_travel.domain.repository.HotelRepository;
import com.best.travel.best_travel.domain.repository.TourRepository;
import com.best.travel.best_travel.infraestructure.asbtract_services.ITourService;
import com.best.travel.best_travel.infraestructure.helpers.BlockListHelper;
import com.best.travel.best_travel.infraestructure.helpers.CustomerHelper;
import com.best.travel.best_travel.infraestructure.helpers.TourHelper;
import com.best.travel.best_travel.util.enums.Tables;
import com.best.travel.best_travel.util.exceptions.IdNotFoundException;

import lombok.AllArgsConstructor;

@Transactional
@Service
@AllArgsConstructor
public class TourService implements ITourService {

    private final TourRepository tourRepository;
    private final FlyRepository flyRepository;
    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final TourHelper tourHelper;
    private final CustomerHelper customerHelper;
    private final BlockListHelper blockListHelper;

    @Override
    public TourResponse create(TourRequest request) {
        blockListHelper.isInBlackListCustomer(request.getCustomerId());
        var customer = customerRepository.findById(request.getCustomerId()).orElseThrow(() -> new IdNotFoundException(Tables.customer.name()));
        var flights = new HashSet<FlyEntity>();
        request.getFlights().forEach(fly -> flights.add(this.flyRepository.findById(fly.getId()).orElseThrow(() -> new IdNotFoundException(Tables.fly.name()))));
        var hotels = new HashMap<HotelEntity, Integer>();
        request.getHotels().forEach(hotel -> hotels.put(this.hotelRepository.findById(hotel.getId()).orElseThrow(() -> new IdNotFoundException(Tables.hotel.name())), hotel.getTotalDays()));
            
       var tourToSave = TourEntity.builder()
       .tickets(this.tourHelper.createTickets(flights, customer))
       .reservations(this.tourHelper.createReservations(hotels, customer))
       .customer(customer)
       .build();

       var tourSaved = this.tourRepository.save(tourToSave);
       this.customerHelper.increase(customer.getDni(), TourService.class);
        return TourResponse.builder()
        .reservationIds(tourSaved.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
        .ticketIds(tourSaved.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
        .id(tourSaved.getId())
        .build();
    }

    @Override
    public TourResponse read(Long id) {
        var tourFromDb = this.tourRepository.findById(id).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        return TourResponse.builder()
        .reservationIds(tourFromDb.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
        .ticketIds(tourFromDb.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
        .id(tourFromDb.getId())
        .build();
    }

    @Override
    public void deleteById(Long id) {
        var tourToDelete = this.tourRepository.findById(id).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        this.tourRepository.delete(tourToDelete);
    }

    @Override
    public void removeTicket(Long tourId, UUID ticketId) {
        var tourUpdate = this.tourRepository.findById(tourId).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        tourUpdate.removeTicket(ticketId);
        this.tourRepository.save(tourUpdate);
    }

    @Override
    public UUID addTicket(Long tourId, Long flyId) {
        var tourUpdate = this.tourRepository.findById(tourId).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        var fly = this.flyRepository.findById(flyId).orElseThrow(() -> new IdNotFoundException(Tables.fly.name()));
        var ticket = this.tourHelper.createTicket(fly, tourUpdate.getCustomer());
        tourUpdate.addTicket(ticket);
        this.tourRepository.save(tourUpdate);
        return ticket.getId();
    }

    @Override
    public void removeReservation( Long tourId, UUID reservationId) {
        var tourUpdate = this.tourRepository.findById(tourId).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        tourUpdate.removeReservation(reservationId);
        this.tourRepository.save(tourUpdate);
    }

    @Override
    public UUID addReservation(Long hotelId, Long tourId, Integer totalDays) {
        var tourUpdate = this.tourRepository.findById(tourId).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        var hotel = this.hotelRepository.findById(hotelId).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        var reservation = this.tourHelper.createReservation(hotel, tourUpdate.getCustomer(), totalDays);
        tourUpdate.addReservation(reservation);
        this.tourRepository.save(tourUpdate);
        return reservation.getId();
    }

}
 
