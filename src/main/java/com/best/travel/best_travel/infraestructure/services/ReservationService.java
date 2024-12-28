package com.best.travel.best_travel.infraestructure.services;

import com.best.travel.best_travel.api.models.request.ReservationRequest;
import com.best.travel.best_travel.api.models.responses.FlyResponse;
import com.best.travel.best_travel.api.models.responses.HotelResponse;
import com.best.travel.best_travel.api.models.responses.ReservationResponse;
import com.best.travel.best_travel.api.models.responses.TicketResponse;
import com.best.travel.best_travel.domain.entity.ReservationEntity;
import com.best.travel.best_travel.domain.entity.TicketEntity;
import com.best.travel.best_travel.domain.repository.CustomerRepository;
import com.best.travel.best_travel.domain.repository.HotelRepository;
import com.best.travel.best_travel.domain.repository.ReservationRepository;
import com.best.travel.best_travel.infraestructure.asbtract_services.IReservationService;
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
public class ReservationService implements IReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final HotelRepository hotelRepository;


    @Override
    public ReservationResponse create(ReservationRequest request) {
        var hotel = this.hotelRepository.findById(request.getIdHotel()).orElseThrow();
        var customer  = this.customerRepository.findById(request.getIdClient()).orElseThrow();

        var reservationToPersist = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .totalDays(request.getTotalDays())
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(request.getTotalDays()))
                .price(hotel.getPrice().add(hotel.getPrice().multiply(CHARGES_PRICE_PERCENTAGES)))
                .build();

        var reservationPersisted = this.reservationRepository.save(reservationToPersist);

        return this.entityToResponse(reservationPersisted);
    }

    @Override
    public ReservationResponse read(UUID uuid) {
        return null;
    }

    @Override
    public ReservationResponse update(UUID uuid, ReservationRequest request) {
        return null;
    }

    @Override
    public ReservationResponse findById(UUID uuid) {
        return null;
    }

    @Override
    public void delete(UUID uuid) {

    }

    private static final BigDecimal CHARGES_PRICE_PERCENTAGES = BigDecimal.valueOf(0,20);

    private ReservationResponse entityToResponse(ReservationEntity reservation){
        var response = new ReservationResponse();
        BeanUtils.copyProperties(reservation, response);
        var hotelResponse = new HotelResponse();
        BeanUtils.copyProperties(reservation.getHotel(), hotelResponse);
        response.setHotelResponse(hotelResponse);
        return response;
    }
}
