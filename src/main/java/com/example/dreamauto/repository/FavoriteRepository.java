package com.example.dreamauto.repository;

import com.example.dreamauto.model.entity.CarAd;
import com.example.dreamauto.model.entity.Favorite;
import com.example.dreamauto.model.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsByUserIdAndCarAdId(Long userId, Long carAdId);

    Optional<Favorite> findByUserIdAndCarAdId(Long userId, Long carAdId);

    Page<Favorite> findByUser(User user, Pageable pageable);

    long countByCarAd(CarAd carAd);
}
