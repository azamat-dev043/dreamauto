package com.example.dreamauto.service.impl;

import com.example.dreamauto.dto.carad.CarAdFilter;
import com.example.dreamauto.dto.carad.CarAdResponseDto;
import com.example.dreamauto.dto.common.PageResponse;
import com.example.dreamauto.dto.user.UserSummaryDto;
import com.example.dreamauto.exception.ForbiddenOperationException;
import com.example.dreamauto.mapper.UserMapper;
import com.example.dreamauto.model.entity.User;
import com.example.dreamauto.service.AdminService;
import com.example.dreamauto.service.CarAdService;
import com.example.dreamauto.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserService userService;
    private final CarAdService carAdService;
    private final UserMapper userMapper;

    public AdminServiceImpl(UserService userService,
                            CarAdService carAdService,
                            UserMapper userMapper) {
        this.userService = userService;
        this.carAdService = carAdService;
        this.userMapper = userMapper;
    }

    @Override
    public UserSummaryDto blockUser(Long userId) {
        ensureAdmin();
        userService.blockUser(userId);
        return userMapper.toSummary(userService.getById(userId));
    }

    @Override
    public UserSummaryDto unblockUser(Long userId) {
        ensureAdmin();
        userService.unblockUser(userId);
        return userMapper.toSummary(userService.getById(userId));
    }

    @Override
    public CarAdResponseDto deactivateAd(Long adId) {
        ensureAdmin();
        carAdService.setActive(adId, false);
        return carAdService.getById(adId);
    }

    @Override
    public CarAdResponseDto activateAd(Long adId) {
        ensureAdmin();
        carAdService.setActive(adId, true);
        return carAdService.getById(adId);
    }

    @Override
    public PageResponse<CarAdResponseDto> listAllAds(CarAdFilter filter, int page, int size) {
        ensureAdmin();
        return carAdService.search(filter, page, size, true);
    }

    private void ensureAdmin() {
        User current = userService.getCurrentUser();
        if (!userService.isAdmin(current)) {
            throw new ForbiddenOperationException("Admin privileges required");
        }
    }
}
