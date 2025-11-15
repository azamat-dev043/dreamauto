package com.example.dreamauto.service;

import com.example.dreamauto.dto.carad.CarAdFilter;
import com.example.dreamauto.dto.carad.CarAdResponseDto;
import com.example.dreamauto.dto.common.PageResponse;
import com.example.dreamauto.dto.user.UserSummaryDto;

public interface AdminService {

    UserSummaryDto blockUser(Long userId);

    UserSummaryDto unblockUser(Long userId);

    CarAdResponseDto deactivateAd(Long adId);

    CarAdResponseDto activateAd(Long adId);

    PageResponse<CarAdResponseDto> listAllAds(CarAdFilter filter, int page, int size);
}
