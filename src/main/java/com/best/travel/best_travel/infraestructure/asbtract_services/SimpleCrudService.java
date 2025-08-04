package com.best.travel.best_travel.infraestructure.asbtract_services;

public interface SimpleCrudService<RQ, RS, ID> {
    
    RS create(RQ request);

    RS read(ID id);

    void deleteById(ID id);
}
