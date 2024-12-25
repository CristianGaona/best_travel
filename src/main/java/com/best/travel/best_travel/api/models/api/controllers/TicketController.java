package com.best.travel.best_travel.api.models.api.controllers;

import com.best.travel.best_travel.api.models.request.TicketRequest;
import com.best.travel.best_travel.api.models.responses.TicketResponse;
import com.best.travel.best_travel.infraestructure.services.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "ticket")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponse> post(@RequestBody TicketRequest request) {
        return ResponseEntity.ok(ticketService.create(request));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<TicketResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(ticketService.read(id));
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<TicketResponse> put(@PathVariable UUID id, @RequestBody TicketRequest request) {
        return ResponseEntity.ok(ticketService.update(id, request));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
