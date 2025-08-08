package com.best.travel.best_travel.infraestructure.asbtract_services;

import java.util.UUID;

import com.best.travel.best_travel.api.models.request.TourRequest;
import com.best.travel.best_travel.api.models.responses.TourResponse;

public interface ITourService extends SimpleCrudService<TourRequest, TourResponse, Long> {

    void removeTicket(Long tourId, UUID ticketId);
    UUID addTicket( Long tourId, Long flyId);
    void removeReservation(Long tourId, UUID reservationId);
    UUID addReservation(Long hotelId, Long tourId, Integer totalDays);
}
