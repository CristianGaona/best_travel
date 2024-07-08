package com.best.travel.best_travel.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "ticket")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TicketEntity implements Serializable{
    
    @Id
    private UUID id;

    private LocalDate departureDate;

    private LocalDate arrivalDate;

    private LocalDate purchaseDate;

    private BigDecimal price;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "fly_id")
    private FlyEntity fly;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = true)
    private TourEntity tour;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;


}
