package com.example.dreamauto.controller;

import com.example.dreamauto.dto.carad.CarAdFilter;
import com.example.dreamauto.dto.carad.CarAdResponseDto;
import com.example.dreamauto.dto.common.PageResponse;
import com.example.dreamauto.dto.user.UserSummaryDto;
import com.example.dreamauto.service.AdminService;
import java.math.BigDecimal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PatchMapping("/users/{userId}/block")
    public ResponseEntity<UserSummaryDto> blockUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.blockUser(userId));
    }

    @PatchMapping("/users/{userId}/unblock")
    public ResponseEntity<UserSummaryDto> unblockUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.unblockUser(userId));
    }

    @PatchMapping("/ads/{adId}/deactivate")
    public ResponseEntity<CarAdResponseDto> deactivateAd(@PathVariable Long adId) {
        return ResponseEntity.ok(adminService.deactivateAd(adId));
    }

    @PatchMapping("/ads/{adId}/activate")
    public ResponseEntity<CarAdResponseDto> activateAd(@PathVariable Long adId) {
        return ResponseEntity.ok(adminService.activateAd(adId));
    }

    @GetMapping("/ads")
    public ResponseEntity<PageResponse<CarAdResponseDto>> listAllAds(@RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "20") int size,
                                                                     @RequestParam(required = false) String brand,
                                                                     @RequestParam(required = false) String model,
                                                                     @RequestParam(required = false) String city,
                                                                     @RequestParam(required = false) Integer minYear,
                                                                     @RequestParam(required = false) Integer maxYear,
                                                                     @RequestParam(required = false) BigDecimal minPrice,
                                                                     @RequestParam(required = false) BigDecimal maxPrice,
                                                                     @RequestParam(required = false) String sortBy,
                                                                     @RequestParam(required = false) String sortDirection) {
        CarAdFilter filter = new CarAdFilter(brand, model, city, minYear, maxYear, minPrice, maxPrice, sortBy, sortDirection);
        return ResponseEntity.ok(adminService.listAllAds(filter, page, size));
    }
}
