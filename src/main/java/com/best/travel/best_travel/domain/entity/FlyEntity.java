package com.best.travel.best_travel.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import com.best.travel.best_travel.util.AeroLine;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "fly")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FlyEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double originLat;
    private Double originLng;
    private Double destinyLat;
    private Double destinyLng;

    @Column(length = 20)
    private String originName;

    @Column(length = 20)
    private String destinyName;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private AeroLine aeroLine;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch =  FetchType.EAGER,
            orphanRemoval = true,
            mappedBy = "fly"
    )
    private Set<TicketEntity> tickets;



}


