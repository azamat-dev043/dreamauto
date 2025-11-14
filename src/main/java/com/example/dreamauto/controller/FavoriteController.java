package com.example.dreamauto.controller;

import com.example.dreamauto.dto.common.PageResponse;
import com.example.dreamauto.dto.favorite.FavoriteResponseDto;
import com.example.dreamauto.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/{adId}")
    public ResponseEntity<FavoriteResponseDto> add(@PathVariable Long adId) {
        return ResponseEntity.ok(favoriteService.addToFavorites(adId));
    }

    @DeleteMapping("/{adId}")
    public ResponseEntity<Void> remove(@PathVariable Long adId) {
        favoriteService.removeFromFavorites(adId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<FavoriteResponseDto>> list(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(favoriteService.getFavorites(page, size));
    }
}
