package com.best.travel.best_travel.infraestructure.services;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.best.travel.best_travel.api.models.responses.HotelResponse;
import com.best.travel.best_travel.config.CacheConstants;
import com.best.travel.best_travel.domain.entity.HotelEntity;
import com.best.travel.best_travel.domain.repository.HotelRepository;
import com.best.travel.best_travel.infraestructure.asbtract_services.IHotelService;
import com.best.travel.best_travel.util.SortType;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Transactional
@Service
@AllArgsConstructor
public class HotelService implements IHotelService {

private final HotelRepository hotelRepository;
    
    @Override
    public Page<HotelResponse> readAll(Integer page, Integer size, SortType sortType) {
       PageRequest pageRequest = null;
        switch (sortType) {
            case NONE -> pageRequest = PageRequest.of(page, size);
            case LOWER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
            case UPPER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
        }
        return this.hotelRepository.findAll(pageRequest)
                .map(this::entityToResponse);
    }

    @Override
    @Cacheable(value = CacheConstants.HOTEL_CACHE_NAME)
    public Set<HotelResponse> readLessPrice(BigDecimal price) {
        return this.hotelRepository.findByPriceLessThan(price)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    @Override
     @Cacheable(value = CacheConstants.HOTEL_CACHE_NAME)
    public Set<HotelResponse> readBetweenPrices(BigDecimal min, BigDecimal max) {
        return this.hotelRepository.findByPriceIsBetween(min, max)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    @Override
     @Cacheable(value = CacheConstants.HOTEL_CACHE_NAME)
    public Set<HotelResponse> readGreaterThan(Integer rating) {
        return this.hotelRepository.findByRatingGreaterThan(rating)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }


     private HotelResponse entityToResponse(HotelEntity fly) {
        var response = new HotelResponse();
        BeanUtils.copyProperties(fly, response);
        return response;
    }
    
}
