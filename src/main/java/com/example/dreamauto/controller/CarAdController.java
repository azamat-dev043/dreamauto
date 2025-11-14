package com.example.dreamauto.controller;

import com.example.dreamauto.dto.carad.CarAdCreateUpdateDto;
import com.example.dreamauto.dto.carad.CarAdFilter;
import com.example.dreamauto.dto.carad.CarAdResponseDto;
import com.example.dreamauto.dto.carad.CarPhotoRequest;
import com.example.dreamauto.dto.common.PageResponse;
import com.example.dreamauto.service.CarAdService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ads")
public class CarAdController {

    private final CarAdService carAdService;

    public CarAdController(CarAdService carAdService) {
        this.carAdService = carAdService;
    }

    @PostMapping
    public ResponseEntity<CarAdResponseDto> create(@Valid @RequestBody CarAdCreateUpdateDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carAdService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarAdResponseDto> update(@PathVariable Long id,
                                                   @Valid @RequestBody CarAdCreateUpdateDto request) {
        return ResponseEntity.ok(carAdService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carAdService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarAdResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(carAdService.getById(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<CarAdResponseDto>> search(@RequestParam(defaultValue = "0") int page,
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
        PageResponse<CarAdResponseDto> response = carAdService.search(filter, page, size, false);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/photos")
    public ResponseEntity<CarAdResponseDto> updatePhotos(@PathVariable Long id,
                                                         @Valid @RequestBody CarPhotoRequest request) {
        return ResponseEntity.ok(carAdService.updatePhotos(id, request.photoUrls()));
    }
}
