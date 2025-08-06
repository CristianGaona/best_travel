package com.best.travel.best_travel.api.models.api.controllers;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.best.travel.best_travel.api.models.request.TourRequest;
import com.best.travel.best_travel.api.models.responses.ErrorResponse;
import com.best.travel.best_travel.api.models.responses.TourResponse;
import com.best.travel.best_travel.infraestructure.asbtract_services.ITourService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;



@RestController
@RequestMapping(path = "tour")
@AllArgsConstructor
@Tag(name = "Tour", description = "Tour API")
public class TourController {
    
    private final ITourService tourService;

    @ApiResponse(responseCode = "400", description = "When the request have a field invalid we response this", content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
    })
    @Operation(summary = "Save in system a tour based in list of hotels and flights")
    @PostMapping
    public ResponseEntity<TourResponse> post(@Valid @RequestBody TourRequest tourRequest) {
        
        return ResponseEntity.ok(this.tourService.create(tourRequest));
    }

    @Operation(summary = "Return a tour with all information based in id")
    @GetMapping(path = "{id}")
    public ResponseEntity<TourResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(this.tourService.read(id));
    }
    

    @Operation(summary = "Delete a tour based in id")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.tourService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove a ticket from a tour based in tour id and ticket id")
    @PatchMapping(path = "{tourId}/remove_ticket/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long tourId, @PathVariable UUID ticketId) {
        this.tourService.removeTicket(tourId, ticketId);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Add a ticket to a tour based in tour id and fly id")
    @PatchMapping(path = "{tourId}/add_ticket/{flyId}")
    public ResponseEntity<Map<String, UUID>> postTicket(@PathVariable Long tourId, @PathVariable Long flyId) {
        var response = Collections.singletonMap("ticketId", this.tourService.addTicket(tourId, flyId ));
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Remove a reservation from a tour based in tour id and reservation id")
    @PatchMapping(path = "{tourId}/remove_reservation/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long tourId, @PathVariable UUID reservationId) {
        this.tourService.removeReservation(tourId, reservationId);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Add a reservation to a tour based in tour id and hotel id")
    @PatchMapping(path = "{tourId}/add_reservation/{hotelId}")
    public ResponseEntity<Map<String, UUID>> postReservation(@PathVariable Long tourId, @PathVariable Long hotelId, @RequestParam Integer totalDays) {
        var response = Collections.singletonMap("ticketId", this.tourService.addReservation( hotelId, tourId, totalDays));
        return ResponseEntity.ok(response);
    }
}
