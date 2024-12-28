package com.best.travel.best_travel.api.models.api.controllers;

import com.best.travel.best_travel.api.models.request.ReservationRequest;
import com.best.travel.best_travel.api.models.responses.ReservationResponse;
import com.best.travel.best_travel.domain.entity.ReservationEntity;
import com.best.travel.best_travel.infraestructure.services.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "reservation")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> post (@RequestBody ReservationRequest request){
        return ResponseEntity.ok(reservationService.create(request));
    }
}
