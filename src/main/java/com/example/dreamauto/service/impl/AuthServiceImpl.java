package com.example.dreamauto.service.impl;

import com.example.dreamauto.dto.auth.AuthResponse;
import com.example.dreamauto.dto.auth.LoginRequest;
import com.example.dreamauto.dto.auth.RegisterRequest;
import com.example.dreamauto.exception.BadRequestException;
import com.example.dreamauto.exception.UserBlockedException;
import com.example.dreamauto.model.entity.User;
import com.example.dreamauto.model.enums.Role;
import com.example.dreamauto.repository.UserRepository;
import com.example.dreamauto.security.JwtService;
import com.example.dreamauto.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("User with email %s already exists".formatted(request.email()));
        }
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setName(request.name());
        user.setPhoneNumber(request.phoneNumber());
        user.setRole(Role.USER);
        user.setActive(true);
        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved);
        return new AuthResponse(token, saved.getId(), saved.getRole());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = (User) authentication.getPrincipal();
        if (!user.isActive()) {
            throw new UserBlockedException("Account is blocked. Contact support");
        }
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getId(), user.getRole());
    }
}
