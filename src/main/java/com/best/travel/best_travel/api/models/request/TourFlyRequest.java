package com.best.travel.best_travel.api.models.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TourFlyRequest implements Serializable {

    @Positive(message = "id fly must be positive")
    @NotNull(message = "id fly is mandatory")
    public Long id;
}
