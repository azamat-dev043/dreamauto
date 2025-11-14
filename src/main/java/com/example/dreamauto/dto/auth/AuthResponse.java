package com.example.dreamauto.dto.auth;

import com.example.dreamauto.model.enums.Role;

public record AuthResponse(String token, Long userId, Role role) {
}
