package com.example.dreamauto.mapper;

import com.example.dreamauto.dto.carad.CarAdCreateUpdateDto;
import com.example.dreamauto.dto.carad.CarAdResponseDto;
import com.example.dreamauto.model.entity.CarAd;
import com.example.dreamauto.model.entity.CarPhoto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CarAdMapper {

    public CarAd toEntity(CarAdCreateUpdateDto dto) {
        CarAd carAd = new CarAd();
        apply(carAd, dto);
        return carAd;
    }

    public void update(CarAd carAd, CarAdCreateUpdateDto dto) {
        apply(carAd, dto);
    }

    private void apply(CarAd carAd, CarAdCreateUpdateDto dto) {
        carAd.setTitle(dto.title());
        carAd.setDescription(dto.description());
        carAd.setBrand(dto.brand());
        carAd.setModel(dto.model());
        carAd.setYear(dto.year());
        carAd.setPrice(dto.price());
        carAd.setMileage(dto.mileage());
        carAd.setBodyType(dto.bodyType());
        carAd.setEngineType(dto.engineType());
        carAd.setTransmission(dto.transmission());
        carAd.setDriveType(dto.driveType());
        carAd.setCity(dto.city());
    }

    public CarAdResponseDto toDto(CarAd carAd, long favoriteCount) {
        List<String> photoUrls = carAd.getPhotos()
                .stream()
                .sorted((a, b) -> Integer.compare(a.getOrderIndex(), b.getOrderIndex()))
                .map(CarPhoto::getUrl)
                .collect(Collectors.toList());
        return new CarAdResponseDto(
                carAd.getId(),
                carAd.getTitle(),
                carAd.getDescription(),
                carAd.getBrand(),
                carAd.getModel(),
                carAd.getYear(),
                carAd.getPrice(),
                carAd.getMileage(),
                carAd.getBodyType(),
                carAd.getEngineType(),
                carAd.getTransmission(),
                carAd.getDriveType(),
                carAd.getCity(),
                carAd.isActive(),
                carAd.getCreatedAt(),
                carAd.getUpdatedAt(),
                carAd.getSeller().getId(),
                carAd.getSeller().getName(),
                carAd.getSeller().getPhoneNumber(),
                photoUrls,
                favoriteCount
        );
    }
}
