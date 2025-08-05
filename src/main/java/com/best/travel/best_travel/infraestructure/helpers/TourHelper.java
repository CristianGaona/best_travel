package com.best.travel.best_travel.infraestructure.helpers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.best.travel.best_travel.domain.entity.CustomerEntity;
import com.best.travel.best_travel.domain.entity.FlyEntity;
import com.best.travel.best_travel.domain.entity.HotelEntity;
import com.best.travel.best_travel.domain.entity.ReservationEntity;
import com.best.travel.best_travel.domain.entity.TicketEntity;
import com.best.travel.best_travel.domain.repository.ReservationRepository;
import com.best.travel.best_travel.domain.repository.TicketRepository;
import com.best.travel.best_travel.util.BestTravelUtil;

import lombok.AllArgsConstructor;

@Transactional
@Component
@AllArgsConstructor
public class TourHelper {
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;
    

    public Set<TicketEntity> createTickets(Set<FlyEntity> fligths, CustomerEntity customer) {
        var response = new HashSet<TicketEntity>(fligths.size());
        fligths.forEach(fly -> {
             var ticketToPersist = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().add(fly.getPrice().multiply(CHARGER_PRICE_PERCENTAGE)))
                .purchaseDate(LocalDate.now())
                .departureDate(BestTravelUtil.getRandomSoon())
                .arrivalDate(BestTravelUtil.getRandomLatrer())
                .build();
                response.add(this.ticketRepository.save(ticketToPersist));
        });
        return response;
    }
    
    public Set<ReservationEntity> createReservations(HashMap<HotelEntity, Integer> hotels, CustomerEntity customer) {
        var response = new HashSet<ReservationEntity>(hotels.size());
        hotels.forEach((hotel, totalDays) -> {
            var reservationToPersist = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .totalDays(totalDays)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(totalDays))
                .price(hotel.getPrice().add(hotel.getPrice().multiply(CHARGES_PRICE_PERCENTAGES)))
                .build();
            response.add(this.reservationRepository.save(reservationToPersist));
        });
        return response;
    }

    public static final BigDecimal CHARGER_PRICE_PERCENTAGE = BigDecimal.valueOf(0.25);
    private static final BigDecimal CHARGES_PRICE_PERCENTAGES = BigDecimal.valueOf(0,20);

}
