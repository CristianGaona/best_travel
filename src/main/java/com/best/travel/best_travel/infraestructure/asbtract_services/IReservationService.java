package com.best.travel.best_travel.infraestructure.asbtract_services;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import com.best.travel.best_travel.api.models.request.ReservationRequest;
import com.best.travel.best_travel.api.models.responses.ReservationResponse;

public interface IReservationService extends CrudService<ReservationRequest, ReservationResponse, UUID> {

    public BigDecimal findPrice(Long hotelId, Currency currency);
}

