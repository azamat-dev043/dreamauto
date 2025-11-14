package com.example.dreamauto.dto.carad;

import com.example.dreamauto.model.enums.BodyType;
import com.example.dreamauto.model.enums.DriveType;
import com.example.dreamauto.model.enums.EngineType;
import com.example.dreamauto.model.enums.Transmission;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CarAdCreateUpdateDto(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String brand,
        @NotBlank String model,
        @NotNull @Min(1950) @Max(2050) Integer year,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal price,
        @NotNull @Positive Long mileage,
        @NotNull BodyType bodyType,
        @NotNull EngineType engineType,
        @NotNull Transmission transmission,
        @NotNull DriveType driveType,
        @NotBlank String city
) {
}
