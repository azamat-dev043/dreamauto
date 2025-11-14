package com.example.dreamauto.dto.carad;

import java.math.BigDecimal;

public record CarAdFilter(
        String brand,
        String model,
        String city,
        Integer minYear,
        Integer maxYear,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        String sortBy,
        String sortDirection
) {
}
