package com.example.dreamauto.repository;

import com.example.dreamauto.model.entity.CarAd;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CarAdRepository extends JpaRepository<CarAd, Long>, JpaSpecificationExecutor<CarAd> {

    Optional<CarAd> findByIdAndIsActiveTrue(Long id);
}
