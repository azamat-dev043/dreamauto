package com.example.dreamauto.service;

import com.example.dreamauto.dto.common.PageResponse;
import com.example.dreamauto.dto.favorite.FavoriteResponseDto;

public interface FavoriteService {

    FavoriteResponseDto addToFavorites(Long adId);

    void removeFromFavorites(Long adId);

    PageResponse<FavoriteResponseDto> getFavorites(int page, int size);
}
