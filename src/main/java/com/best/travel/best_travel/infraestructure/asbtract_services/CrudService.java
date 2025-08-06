package com.best.travel.best_travel.infraestructure.asbtract_services;

public interface CrudService <RQ,RS,ID>{
    RS create(RQ request);
    RS read(ID id);
    RS update(ID id, RQ request);
    RS findById(ID id);
    void delete(ID id);
}
