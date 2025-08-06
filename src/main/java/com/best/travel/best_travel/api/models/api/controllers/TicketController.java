package com.best.travel.best_travel.api.models.api.controllers;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.best.travel.best_travel.api.models.request.TicketRequest;
import com.best.travel.best_travel.api.models.responses.ErrorResponse;
import com.best.travel.best_travel.api.models.responses.TicketResponse;
import com.best.travel.best_travel.infraestructure.services.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "ticket")
@AllArgsConstructor
@Tag(name = "Ticket", description = "Ticket API")
public class TicketController {

    private final TicketService ticketService;

    @ApiResponse(responseCode = "400", description = "When the request have a field invalid we response this", content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
    })
    @Operation(summary = "Create a ticket based on request")
    @PostMapping
    public ResponseEntity<TicketResponse> post(@Valid @RequestBody TicketRequest request) {
        return ResponseEntity.ok(ticketService.create(request));
    }

    @Operation(summary = "Return a ticket with all information based on id")
    @GetMapping(path = "{id}")
    public ResponseEntity<TicketResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(ticketService.read(id));
    }

    @Operation(summary = "Update a ticket based on id and request")
    @PutMapping(path = "{id}")
    public ResponseEntity<TicketResponse> put(@Valid @PathVariable UUID id, @RequestBody TicketRequest request) {
        return ResponseEntity.ok(ticketService.update(id, request));
    }

    @Operation(summary = "Delete a ticket based on id")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Return the total price of a ticket based on ticket id")
    @GetMapping
    public ResponseEntity<Map<String, BigDecimal>> getFlyPrice(@RequestParam Long flyId) {
        return ResponseEntity.ok(Collections.singletonMap("flyPrice", ticketService.findPrice(flyId)));
    }
}
