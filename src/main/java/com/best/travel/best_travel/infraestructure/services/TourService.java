package com.best.travel.best_travel.infraestructure.services;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.best.travel.best_travel.api.models.request.TourRequest;
import com.best.travel.best_travel.api.models.responses.TourResponse;
import com.best.travel.best_travel.domain.repository.CustomerRepository;
import com.best.travel.best_travel.domain.repository.FlyRepository;
import com.best.travel.best_travel.domain.repository.HotelRepository;
import com.best.travel.best_travel.domain.repository.TourRepository;
import com.best.travel.best_travel.infraestructure.asbtract_services.ITourService;
import com.best.travel.best_travel.infraestructure.helpers.TourHelper;

import lombok.AllArgsConstructor;

@Transactional
@Service
@AllArgsConstructor
public class TourService implements ITourService {

    private final TourRepository tourRepository;
    private final FlyRepository flyRepository;
    private HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;

    private final TourHelper tourHelper;

    @Override
    public TourResponse create(TourRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public TourResponse read(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'read'");
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public void removeTicket(UUID ticketId, Long tourId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeTicket'");
    }

    @Override
    public UUID addTicket(Long flyId, Long tourId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addTicket'");
    }

    @Override
    public void removeReservation(UUID reservationId, Long tourId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeReservation'");
    }

    @Override
    public UUID addReservation(Long reservationId, Long tourId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addReservation'");
    }

}
 
