package com.best.travel.best_travel.infraestructure.asbtract_services;

import com.best.travel.best_travel.api.models.request.ReservationRequest;
import com.best.travel.best_travel.api.models.responses.ReservationResponse;

import java.util.UUID;

public interface IReservationService extends CrudService<ReservationRequest, ReservationResponse, UUID> {
}
