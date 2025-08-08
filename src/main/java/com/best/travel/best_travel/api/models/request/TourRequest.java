package com.best.travel.best_travel.api.models.request;

import java.io.Serializable;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TourRequest implements Serializable {
    
    @Size(min = 18, max = 20, message = "idClient must be between 18 and 20 characters")
    @NotBlank(message = "id client is mandatory")
    public String customerId;
    @Size(min = 1, message = "at least one flight is required")
    private Set<TourFlyRequest> flights;
    @Size(min = 1, message = "at least one hotel is required")
    private Set<TourHotelRequest> hotels;
}
