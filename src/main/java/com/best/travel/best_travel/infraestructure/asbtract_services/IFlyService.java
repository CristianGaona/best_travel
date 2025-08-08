package com.best.travel.best_travel.infraestructure.asbtract_services;

import java.util.Set;

import com.best.travel.best_travel.api.models.responses.FlyResponse;

public interface IFlyService extends CatalogService<FlyResponse> {
    Set<FlyResponse> readByOriginDestiny(String origin, String destiny);
}
