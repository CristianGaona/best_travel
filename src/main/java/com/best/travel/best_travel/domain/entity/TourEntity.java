package com.best.travel.best_travel.domain.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "tour")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TourEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
        cascade = CascadeType.ALL,
        fetch =  FetchType.EAGER,
        orphanRemoval = true,
        mappedBy = "tour"
    )
    private Set<ReservationEntity> reservations;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
        cascade = CascadeType.ALL,
        fetch =  FetchType.EAGER,
        orphanRemoval = true,
        mappedBy = "tour"
    )
    private Set<TicketEntity> tickets;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "id_customer")
    private CustomerEntity customer;

    public void addTicket(TicketEntity ticket){
        if(Objects.isNull(this.tickets)) this.tickets = new HashSet<>();
        this.tickets.add(ticket);
    }

    public void removeTicket(UUID id){
        if(Objects.isNull(this.tickets)) this.tickets = new HashSet<>();
        this.tickets.removeIf(ticket -> ticket.getId().equals(id));

    }

    public void updateTicket(){
        this.tickets.forEach(ticket -> ticket.setTour(this));
    }


    public void addReservation(ReservationEntity reservation){
        if (Objects.isNull(this.reservations)) this.reservations = new HashSet<>();
        this.reservations.add(reservation);
    }

    public void removeReservation(UUID idReservation){
        if (Objects.isNull(this.reservations)) this.reservations = new HashSet<>();
        this.reservations.removeIf(r -> r.getId().equals(idReservation));

    }

    public void updateReservation(){
        this.reservations.forEach(r -> r.setTour(this));
    }
}
