package com.best.travel.best_travel.infraestructure.asbtract_services;

import com.best.travel.best_travel.api.models.request.TicketRequest;
import com.best.travel.best_travel.api.models.responses.TicketResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface ITicketService extends CrudService<TicketRequest, TicketResponse, UUID> {

    BigDecimal findPrice(Long flyId);
}
