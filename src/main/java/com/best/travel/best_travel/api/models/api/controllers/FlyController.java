package com.best.travel.best_travel.api.models.api.controllers;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.best.travel.best_travel.api.models.responses.FlyResponse;
import com.best.travel.best_travel.infraestructure.asbtract_services.IFlyService;
import com.best.travel.best_travel.util.SortType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping(path = "fly")
@AllArgsConstructor
@Tag(name = "Fly", description = "Fly API")
public class FlyController {
    
    private final IFlyService flyService;

    @Operation(summary = "Return a page of all flights in system")
    @GetMapping
    public ResponseEntity<Page<FlyResponse>> getAll(@RequestParam Integer page, @RequestParam Integer size, @RequestHeader(required = false) SortType sortType) {
        if(ObjectUtils.isEmpty(sortType)) {
            sortType = SortType.NONE;
        }
        var response = this.flyService.readAll(page, size, sortType);
        return response.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Return a flight with all information based in id")
    @GetMapping(path= "less-price")
    public ResponseEntity<Set<FlyResponse>> getLessPrice(@RequestParam BigDecimal price) {
        var response = this.flyService.readLessPrice(price);
        return response.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }


    @Operation(summary = "Return a flight with all information based in id")
    @GetMapping(path = "between-price")
    public ResponseEntity<Set<FlyResponse>> getBetween(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        var response = this.flyService.readBetweenPrices(min, max);
        return response.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }


    @Operation(summary = "Return a flight with all information based in id")
    @GetMapping(path = "origin-destiny")
    public ResponseEntity<Set<FlyResponse>> getByOriginDestiny(@RequestParam String origin, @RequestParam String destiny) {
        var response = this.flyService.readByOriginDestiny(origin, destiny);
        return response.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

}
