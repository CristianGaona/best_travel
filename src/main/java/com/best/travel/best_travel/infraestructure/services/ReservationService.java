package com.best.travel.best_travel.infraestructure.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.best.travel.best_travel.api.models.request.ReservationRequest;
import com.best.travel.best_travel.api.models.responses.HotelResponse;
import com.best.travel.best_travel.api.models.responses.ReservationResponse;
import com.best.travel.best_travel.domain.entity.ReservationEntity;
import com.best.travel.best_travel.domain.repository.CustomerRepository;
import com.best.travel.best_travel.domain.repository.HotelRepository;
import com.best.travel.best_travel.domain.repository.ReservationRepository;
import com.best.travel.best_travel.infraestructure.asbtract_services.IReservationService;
import com.best.travel.best_travel.infraestructure.helpers.ApiCurrencyConnectorHelper;
import com.best.travel.best_travel.infraestructure.helpers.BlockListHelper;
import com.best.travel.best_travel.infraestructure.helpers.CustomerHelper;
import com.best.travel.best_travel.infraestructure.helpers.EmailHelper;
import com.best.travel.best_travel.util.enums.Tables;
import com.best.travel.best_travel.util.exceptions.IdNotFoundException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class ReservationService implements IReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final HotelRepository hotelRepository;
    private final CustomerHelper customerHelper;
    private final BlockListHelper blackListHelper;
    private final ApiCurrencyConnectorHelper apiCurrencyConnectorHelper;
    private final EmailHelper emailHelper;

    @Override
    public ReservationResponse create(ReservationRequest request) {
        blackListHelper.isInBlackListCustomer(request.getIdClient());
        var hotel = this.hotelRepository.findById(request.getIdHotel()).orElseThrow(() -> new IdNotFoundException("hotel"));
        var customer  = this.customerRepository.findById(request.getIdClient()).orElseThrow(() -> new IdNotFoundException("customer"));

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
        this.customerHelper.increase(customer.getDni(), ReservationService.class);

        if(Objects.nonNull(request.getEmail())) this.emailHelper.sendEmail(request.getEmail(), customer.getFullName(), Tables.reservation.name());
        return this.entityToResponse(reservationPersisted);
    }

    @Override
    public ReservationResponse read(UUID uuid) {
        var reservationFromDb = this.reservationRepository.findById(uuid).orElseThrow(() -> new IdNotFoundException("reservation"));
        return this.entityToResponse(reservationFromDb);
    }

    @Override
    public ReservationResponse update(UUID uuid, ReservationRequest request) {
        var hotel = this.hotelRepository.findById(request.getIdHotel()).orElseThrow(() -> new IdNotFoundException("hotel"));

        var reservationToUpdate = this.reservationRepository.findById(uuid).orElseThrow(() -> new IdNotFoundException("reservation"));

        reservationToUpdate.setHotel(hotel);
        reservationToUpdate.setTotalDays(request.getTotalDays());
        reservationToUpdate.setDateTimeReservation(LocalDateTime.now());
        reservationToUpdate.setDateStart(LocalDate.now());
        reservationToUpdate.setDateEnd(LocalDate.now().plusDays(request.getTotalDays()));
        reservationToUpdate.setPrice(hotel.getPrice().add(hotel.getPrice().multiply(CHARGES_PRICE_PERCENTAGES)));

        var reservationUpdated = this.reservationRepository.save(reservationToUpdate);

        log.info("Reservation updated with id: {}", reservationUpdated.getId());

        return this.entityToResponse(reservationUpdated);
    }

    @Override
    public ReservationResponse findById(UUID uuid) {
        return null;
    }

    @Override
    public void delete(UUID uuid) {
        var reservationToDelete = this.reservationRepository.findById(uuid).orElseThrow(() -> new IdNotFoundException("reservation"));
        this.reservationRepository.delete(reservationToDelete);
    }


    private ReservationResponse entityToResponse(ReservationEntity reservation){
        var response = new ReservationResponse();
        BeanUtils.copyProperties(reservation, response);
        var hotelResponse = new HotelResponse();
        BeanUtils.copyProperties(reservation.getHotel(), hotelResponse);
        response.setHotelResponse(hotelResponse);
        return response;
    }

    @Override
    public BigDecimal findPrice(Long hotelId, Currency currency) {
        var hotel = this.hotelRepository.findById(hotelId).orElseThrow(() -> new IdNotFoundException("hotel"));
        var priceInDollars = hotel.getPrice().add(hotel.getPrice().multiply(CHARGES_PRICE_PERCENTAGES));
        if(currency.equals(Currency.getInstance("USD"))) return priceInDollars;
        var currencyDTO = this.apiCurrencyConnectorHelper.getExchangeRate(currency);
        log.info("API currency in {}, response: {}", currencyDTO.getExchangeDate(), currencyDTO.getRates());
        return priceInDollars.multiply(currencyDTO.getRates().get(currency));
    }

    private static final BigDecimal CHARGES_PRICE_PERCENTAGES = BigDecimal.valueOf(0,20);
}
