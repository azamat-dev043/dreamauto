package com.example.dreamauto.dto.user;

import com.example.dreamauto.model.enums.Role;

public record UserSummaryDto(
        Long id,
        String email,
        String name,
        String phoneNumber,
        Role role,
        boolean active
) {
}
