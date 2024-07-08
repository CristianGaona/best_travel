package com.best.travel.best_travel.domain.repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.best.travel.best_travel.domain.entity.FlyEntity;

@Repository
public interface FlyRepository extends JpaRepository<FlyEntity, Long> {

    @Query("select f from fly f where f.price < :price")
    Set<FlyEntity> selectLessPrice(BigDecimal price);


    @Query("SELECT f FROM fly f WHERE f.price between :min and :max")
    Set<FlyEntity> selectBetweenPrices(BigDecimal min, BigDecimal max);


    @Query("SELECT f FROM fly f WHERE f.originName = :origin and f.destinyName = :destiny")
    Set<FlyEntity> selectOriginDestiny(String origin, String destiny );


    @Query("SELECT f FROM fly f JOIN FETCH f.tickets t WHERE t.id = :id")
    Optional<FlyEntity> findTicketId(UUID id);

    
}
