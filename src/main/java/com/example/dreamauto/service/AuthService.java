package com.example.dreamauto.service;

import com.example.dreamauto.dto.auth.AuthResponse;
import com.example.dreamauto.dto.auth.LoginRequest;
import com.example.dreamauto.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
