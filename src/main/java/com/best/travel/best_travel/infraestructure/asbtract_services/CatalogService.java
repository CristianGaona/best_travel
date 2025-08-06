package com.best.travel.best_travel.infraestructure.asbtract_services;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.best.travel.best_travel.util.SortType;

public interface CatalogService<R> {
    Page<R> readAll(Integer page, Integer size, SortType sortType);

    Set<R> readLessPrice(BigDecimal price);

    Set<R> readBetweenPrices(BigDecimal min, BigDecimal max);

    String FIELD_BY_SORT = "price";
}
