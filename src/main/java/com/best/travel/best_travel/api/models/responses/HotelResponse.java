package com.best.travel.best_travel.api.models.responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HotelResponse implements Serializable {
    private Long id;
    private String name;
    private String address;
    private String rating;
    private BigDecimal price;
}
