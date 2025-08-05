package com.best.travel.best_travel.api.models.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.best.travel.best_travel.api.models.request.TourRequest;
import com.best.travel.best_travel.api.models.responses.TourResponse;
import com.best.travel.best_travel.infraestructure.asbtract_services.ITourService;

import lombok.AllArgsConstructor;



@RestController
@RequestMapping(path = "tour")
@AllArgsConstructor
public class TourController {
    
    private final ITourService tourService;

    @PostMapping
    public ResponseEntity<TourResponse> post(@RequestBody TourRequest tourRequest) {
        
        return ResponseEntity.ok(this.tourService.create(tourRequest));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<TourResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(this.tourService.read(id));
    }
    

     @GetMapping(path = "{id}")
    public ResponseEntity<TourResponse> delete(@PathVariable Long id) {
        this.tourService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
}
