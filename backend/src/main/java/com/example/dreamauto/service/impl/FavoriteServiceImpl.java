package com.example.dreamauto.service.impl;

import com.example.dreamauto.dto.common.PageResponse;
import com.example.dreamauto.dto.favorite.FavoriteResponseDto;
import com.example.dreamauto.exception.BadRequestException;
import com.example.dreamauto.exception.NotFoundException;
import com.example.dreamauto.mapper.FavoriteMapper;
import com.example.dreamauto.model.entity.CarAd;
import com.example.dreamauto.model.entity.Favorite;
import com.example.dreamauto.model.entity.User;
import com.example.dreamauto.repository.CarAdRepository;
import com.example.dreamauto.repository.FavoriteRepository;
import com.example.dreamauto.service.FavoriteService;
import com.example.dreamauto.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final CarAdRepository carAdRepository;
    private final FavoriteMapper favoriteMapper;
    private final UserService userService;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository,
                               CarAdRepository carAdRepository,
                               FavoriteMapper favoriteMapper,
                               UserService userService) {
        this.favoriteRepository = favoriteRepository;
        this.carAdRepository = carAdRepository;
        this.favoriteMapper = favoriteMapper;
        this.userService = userService;
    }

    @Override
    @Transactional
    public FavoriteResponseDto addToFavorites(Long adId) {
        User user = userService.getCurrentUser();
        CarAd carAd = carAdRepository.findByIdAndIsActiveTrue(adId)
                .orElseThrow(() -> new NotFoundException("Active ad %d not found".formatted(adId)));
        if (favoriteRepository.existsByUserIdAndCarAdId(user.getId(), adId)) {
            throw new BadRequestException("Ad already in favorites");
        }
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setCarAd(carAd);
        Favorite saved = favoriteRepository.save(favorite);
        return favoriteMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void removeFromFavorites(Long adId) {
        User user = userService.getCurrentUser();
        Favorite favorite = favoriteRepository.findByUserIdAndCarAdId(user.getId(), adId)
                .orElseThrow(() -> new NotFoundException("Favorite not found"));
        favoriteRepository.delete(favorite);
    }

    @Override
    public PageResponse<FavoriteResponseDto> getFavorites(int page, int size) {
        User user = userService.getCurrentUser();
        Page<Favorite> favorites = favoriteRepository.findByUser(user, PageRequest.of(page, size));
        return new PageResponse<>(
                favorites.map(favoriteMapper::toDto).getContent(),
                favorites.getNumber(),
                favorites.getSize(),
                favorites.getTotalElements(),
                favorites.getTotalPages()
        );
    }
}
