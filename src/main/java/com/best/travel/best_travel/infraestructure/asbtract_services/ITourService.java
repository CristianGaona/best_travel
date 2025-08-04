package com.best.travel.best_travel.infraestructure.asbtract_services;

import java.util.UUID;

import com.best.travel.best_travel.api.models.request.TourRequest;
import com.best.travel.best_travel.api.models.responses.TourResponse;

public interface ITourService extends SimpleCrudService<TourRequest, TourResponse, Long> {

    void removeTicket(UUID ticketId, Long tourId);
    UUID addTicket(Long flyId, Long tourId);
    void removeReservation(UUID reservationId, Long tourId);
    UUID addReservation(Long reservationId, Long tourId);
}
