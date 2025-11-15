package com.example.dreamauto.dto.carad;

import com.example.dreamauto.model.enums.BodyType;
import com.example.dreamauto.model.enums.DriveType;
import com.example.dreamauto.model.enums.EngineType;
import com.example.dreamauto.model.enums.Transmission;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CarAdResponseDto(
        Long id,
        String title,
        String description,
        String brand,
        String model,
        Integer year,
        BigDecimal price,
        Long mileage,
        BodyType bodyType,
        EngineType engineType,
        Transmission transmission,
        DriveType driveType,
        String city,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long sellerId,
        String sellerName,
        String sellerPhoneNumber,
        List<String> photoUrls,
        long favoriteCount
) {
}
