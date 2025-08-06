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

import com.best.travel.best_travel.api.models.responses.HotelResponse;
import com.best.travel.best_travel.infraestructure.asbtract_services.IHotelService;
import com.best.travel.best_travel.util.SortType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping(path = "hotel")
@AllArgsConstructor
@Tag(name = "Hotel", description = "Hotel API")
public class HotelController {
    
    private final IHotelService hotelService;
    
    @Operation(summary = "Return a page of all hotels in system")
    @GetMapping
    public ResponseEntity<Page<HotelResponse>> getAll(@RequestParam Integer page, @RequestParam Integer size, @RequestHeader(required = false) SortType sortType) {
        if(ObjectUtils.isEmpty(sortType)) {
            sortType = SortType.NONE;
        }
        var response = this.hotelService.readAll(page, size, sortType);
        return response.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }


    @Operation(summary = "Return a hotel with all information based in id")
    @GetMapping(path= "less-price")
    public ResponseEntity<Set<HotelResponse>> getLessPrice(@RequestParam BigDecimal price) {
        var response = this.hotelService.readLessPrice(price);
        return response.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Return a hotel with all information based in id")
    @GetMapping(path = "between-price")
    public ResponseEntity<Set<HotelResponse>> getBetween(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        var response = this.hotelService.readBetweenPrices(min, max);
        return response.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Return a hotel with all information based in id")
    @GetMapping(path = "rating")
    public ResponseEntity<Set<HotelResponse>> getRating(@RequestParam Integer rating) {
        if(rating>4)rating = 4;
        if(rating<1)rating = 1;
        var response = this.hotelService.readGreaterThan(rating);
        return response.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }
}
