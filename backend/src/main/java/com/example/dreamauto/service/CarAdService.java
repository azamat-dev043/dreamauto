package com.example.dreamauto.service;

import com.example.dreamauto.dto.carad.CarAdCreateUpdateDto;
import com.example.dreamauto.dto.carad.CarAdFilter;
import com.example.dreamauto.dto.carad.CarAdResponseDto;
import com.example.dreamauto.dto.common.PageResponse;
import java.util.List;

public interface CarAdService {

    CarAdResponseDto create(CarAdCreateUpdateDto request);

    CarAdResponseDto update(Long adId, CarAdCreateUpdateDto request);

    void delete(Long adId);

    CarAdResponseDto getById(Long adId);

    PageResponse<CarAdResponseDto> search(CarAdFilter filter, int page, int size, boolean includeInactive);

    PageResponse<CarAdResponseDto> getMyAds(int page, int size, boolean includeInactive);

    CarAdResponseDto updatePhotos(Long adId, List<String> photoUrls);

    void setActive(Long adId, boolean active);
}
