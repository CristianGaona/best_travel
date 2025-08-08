package com.best.travel.best_travel.api.models.request;
import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class ReservationRequest implements Serializable{

    @Size(min = 18, max = 20, message = "idClient must be between 18 and 20 characters")
    @NotBlank(message = "id client is mandatory")
    private String idClient;

    @Positive(message = "id hotel must be positive")
    @NotNull(message = "id hotel is mandatory")
    private Long idHotel;
    @Min(value = 1, message = "total days min one days to make reservation")
    @Max(value = 30, message = "total days max 30 days to make reservation")
    @NotNull(message = "total days is mandatory")
    private Integer totalDays;
    //@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email is not valid")
    @Email(message = "Email is not valid")
    private String email;
}
