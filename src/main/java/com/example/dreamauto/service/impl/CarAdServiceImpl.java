package com.example.dreamauto.service.impl;

import com.example.dreamauto.dto.carad.CarAdCreateUpdateDto;
import com.example.dreamauto.dto.carad.CarAdFilter;
import com.example.dreamauto.dto.carad.CarAdResponseDto;
import com.example.dreamauto.dto.common.PageResponse;
import com.example.dreamauto.exception.ForbiddenOperationException;
import com.example.dreamauto.exception.NotFoundException;
import com.example.dreamauto.exception.UserBlockedException;
import com.example.dreamauto.mapper.CarAdMapper;
import com.example.dreamauto.model.entity.CarAd;
import com.example.dreamauto.model.entity.CarPhoto;
import com.example.dreamauto.model.entity.User;
import com.example.dreamauto.repository.CarAdRepository;
import com.example.dreamauto.repository.FavoriteRepository;
import com.example.dreamauto.service.CarAdService;
import com.example.dreamauto.service.UserService;
import com.example.dreamauto.service.util.CarAdSpecifications;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CarAdServiceImpl implements CarAdService {

    private static final String DEFAULT_SORT = "createdAt";

    private final CarAdRepository carAdRepository;
    private final FavoriteRepository favoriteRepository;
    private final CarAdMapper carAdMapper;
    private final UserService userService;

    public CarAdServiceImpl(CarAdRepository carAdRepository,
                            FavoriteRepository favoriteRepository,
                            CarAdMapper carAdMapper,
                            UserService userService) {
        this.carAdRepository = carAdRepository;
        this.favoriteRepository = favoriteRepository;
        this.carAdMapper = carAdMapper;
        this.userService = userService;
    }

    @Override
    @Transactional
    public CarAdResponseDto create(CarAdCreateUpdateDto request) {
        User currentUser = userService.getCurrentUser();
        if (!currentUser.isActive()) {
            throw new UserBlockedException("Blocked users cannot create ads");
        }
        CarAd carAd = carAdMapper.toEntity(request);
        carAd.setSeller(currentUser);
        carAd.setActive(true);
        CarAd saved = carAdRepository.save(carAd);
        return mapToDto(saved);
    }

    @Override
    @Transactional
    public CarAdResponseDto update(Long adId, CarAdCreateUpdateDto request) {
        CarAd carAd = getOwnedOrAdminAd(adId);
        carAdMapper.update(carAd, request);
        return mapToDto(carAd);
    }

    @Override
    @Transactional
    public void delete(Long adId) {
        CarAd carAd = getOwnedOrAdminAd(adId);
        carAd.setActive(false);
        carAdRepository.save(carAd);
    }

    @Override
    public CarAdResponseDto getById(Long adId) {
        CarAd carAd = carAdRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException("Ad %d not found".formatted(adId)));
        if (!carAd.isActive()) {
            User current = userService.getCurrentUserOptional().orElse(null);
            boolean canView = current != null && (userService.isAdmin(current)
                    || Objects.equals(current.getId(), carAd.getSeller().getId()));
            if (!canView) {
                throw new NotFoundException("Ad %d not found".formatted(adId));
            }
        }
        return mapToDto(carAd);
    }

    @Override
    public PageResponse<CarAdResponseDto> search(CarAdFilter filter, int page, int size, boolean includeInactive) {
        Pageable pageable = PageRequest.of(page, size, resolveSort(filter));
        Page<CarAd> ads = carAdRepository.findAll(CarAdSpecifications.withFilter(filter, includeInactive), pageable);
        List<CarAdResponseDto> content = ads.stream().map(this::mapToDto).toList();
        return new PageResponse<>(content, ads.getNumber(), ads.getSize(), ads.getTotalElements(), ads.getTotalPages());
    }

    @Override
    public PageResponse<CarAdResponseDto> getMyAds(int page, int size, boolean includeInactive) {
        User currentUser = userService.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, DEFAULT_SORT));
        Page<CarAd> ads = includeInactive
                ? carAdRepository.findBySellerId(currentUser.getId(), pageable)
                : carAdRepository.findBySellerIdAndIsActiveTrue(currentUser.getId(), pageable);
        List<CarAdResponseDto> content = ads.stream().map(this::mapToDto).toList();
        return new PageResponse<>(content, ads.getNumber(), ads.getSize(), ads.getTotalElements(), ads.getTotalPages());
    }

    @Override
    @Transactional
    public CarAdResponseDto updatePhotos(Long adId, List<String> photoUrls) {
        CarAd carAd = getOwnedOrAdminAd(adId);
        if (photoUrls == null) {
            return mapToDto(carAd);
        }
        List<CarPhoto> photos = new ArrayList<>();
        for (int i = 0; i < photoUrls.size(); i++) {
            photos.add(new CarPhoto(photoUrls.get(i), i));
        }
        carAd.replacePhotos(photos);
        return mapToDto(carAd);
    }

    @Override
    @Transactional
    public void setActive(Long adId, boolean active) {
        CarAd carAd = carAdRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException("Ad %d not found".formatted(adId)));
        carAd.setActive(active);
    }

    private CarAd getOwnedOrAdminAd(Long adId) {
        CarAd carAd = carAdRepository.findById(adId)
                .orElseThrow(() -> new NotFoundException("Ad %d not found".formatted(adId)));
        User currentUser = userService.getCurrentUser();
        boolean isOwner = Objects.equals(currentUser.getId(), carAd.getSeller().getId());
        if (!isOwner && !userService.isAdmin(currentUser)) {
            throw new ForbiddenOperationException("You cannot modify this ad");
        }
        return carAd;
    }

    private Sort resolveSort(CarAdFilter filter) {
        String sortBy = filter != null && filter.sortBy() != null ? filter.sortBy() : DEFAULT_SORT;
        if (!List.of("createdAt", "price").contains(sortBy)) {
            sortBy = DEFAULT_SORT;
        }
        Sort.Direction direction = Sort.Direction.DESC;
        if (filter != null && "asc".equalsIgnoreCase(filter.sortDirection())) {
            direction = Sort.Direction.ASC;
        }
        return Sort.by(direction, sortBy);
    }

    private CarAdResponseDto mapToDto(CarAd ad) {
        long favoriteCount = favoriteRepository.countByCarAd(ad);
        return carAdMapper.toDto(ad, favoriteCount);
    }
}
