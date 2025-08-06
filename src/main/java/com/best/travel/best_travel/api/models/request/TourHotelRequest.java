package com.best.travel.best_travel.api.models.request;

import java.io.Serializable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class TourHotelRequest implements Serializable{

    @Positive(message = "id tour must be positive")
    @NotNull(message = "id tour is mandatory")
    public Long id;

    @Min(value = 1, message = "total days min one days to make reservation")
    @Max(value = 30, message = "total days max 30 days to make reservation")
    @NotNull(message = "total days is mandatory")
    private Integer totalDays;
}
