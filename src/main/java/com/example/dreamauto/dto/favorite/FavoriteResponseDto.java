package com.example.dreamauto.dto.favorite;

import java.math.BigDecimal;
import java.util.List;

public record FavoriteResponseDto(
        Long id,
        Long carAdId,
        String title,
        String brand,
        String model,
        BigDecimal price,
        List<String> photoUrls
) {
}
