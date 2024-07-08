package com.best.travel.best_travel.domain.repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.best.travel.best_travel.domain.entity.HotelEntity;

public interface HotelRepository extends JpaRepository<HotelEntity, Long> {

    Set<HotelEntity> findByPriceLessThan(BigDecimal price);

    Set<HotelEntity> findByPriceIsBetween(BigDecimal min, BigDecimal max );

    Set<HotelEntity> findByRatingGreaterThan(Integer ratting);

    Optional<HotelEntity> findByReservationId(UUID id);
    
}
