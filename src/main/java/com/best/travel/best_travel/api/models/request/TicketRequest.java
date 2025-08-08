package com.best.travel.best_travel.api.models.request;


import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TicketRequest implements Serializable {

    @Size(min = 18, max = 20, message = "idClient must be between 18 and 20 characters")
    @NotBlank(message = "id client is mandatory")
    private String idClient;
    @Positive(message = "id fly must be positive")
    @NotNull(message = "id fly is mandatory")
    private Long idFly;
}
