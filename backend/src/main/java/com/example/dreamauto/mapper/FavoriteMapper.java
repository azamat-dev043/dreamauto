package com.example.dreamauto.mapper;

import com.example.dreamauto.dto.favorite.FavoriteResponseDto;
import com.example.dreamauto.model.entity.CarPhoto;
import com.example.dreamauto.model.entity.Favorite;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FavoriteMapper {

    public FavoriteResponseDto toDto(Favorite favorite) {
        List<String> photoUrls = favorite.getCarAd().getPhotos().stream()
                .sorted(Comparator.comparingInt(CarPhoto::getOrderIndex))
                .map(CarPhoto::getUrl)
                .toList();
        return new FavoriteResponseDto(
                favorite.getId(),
                favorite.getCarAd().getId(),
                favorite.getCarAd().getTitle(),
                favorite.getCarAd().getBrand(),
                favorite.getCarAd().getModel(),
                favorite.getCarAd().getPrice(),
                photoUrls
        );
    }
}
