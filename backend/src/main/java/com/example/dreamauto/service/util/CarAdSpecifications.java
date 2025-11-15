package com.example.dreamauto.service.util;

import com.example.dreamauto.dto.carad.CarAdFilter;
import com.example.dreamauto.model.entity.CarAd;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class CarAdSpecifications {

    private CarAdSpecifications() {
    }

    public static Specification<CarAd> withFilter(CarAdFilter filter, boolean includeInactive) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!includeInactive) {
                predicates.add(cb.isTrue(root.get("isActive")));
            }
            if (filter != null) {
                if (filter.brand() != null && !filter.brand().isBlank()) {
                    predicates.add(cb.like(cb.lower(root.get("brand")), like(filter.brand())));
                }
                if (filter.model() != null && !filter.model().isBlank()) {
                    predicates.add(cb.like(cb.lower(root.get("model")), like(filter.model())));
                }
                if (filter.city() != null && !filter.city().isBlank()) {
                    predicates.add(cb.like(cb.lower(root.get("city")), like(filter.city())));
                }
                if (filter.minYear() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("year"), filter.minYear()));
                }
                if (filter.maxYear() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("year"), filter.maxYear()));
                }
                if (filter.minPrice() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("price"), filter.minPrice()));
                }
                if (filter.maxPrice() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("price"), filter.maxPrice()));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static String like(String value) {
        return "%" + value.toLowerCase() + "%";
    }
}
