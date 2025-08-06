package com.best.travel.best_travel.infraestructure.asbtract_services;

import java.util.Set;

import com.best.travel.best_travel.api.models.responses.HotelResponse;

public interface IHotelService extends CatalogService<HotelResponse> {

    Set<HotelResponse> readGreaterThan(Integer rating);
}
