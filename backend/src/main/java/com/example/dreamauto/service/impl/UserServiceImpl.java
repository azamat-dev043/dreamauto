package com.example.dreamauto.service.impl;

import com.example.dreamauto.exception.ForbiddenOperationException;
import com.example.dreamauto.exception.NotFoundException;
import com.example.dreamauto.model.entity.User;
import com.example.dreamauto.model.enums.Role;
import com.example.dreamauto.repository.UserRepository;
import com.example.dreamauto.service.UserService;
import java.util.Optional;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User %d not found".formatted(id)));
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User %s not found".formatted(email)));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getCurrentUser() {
        return getCurrentUserOptional()
                .orElseThrow(() -> new ForbiddenOperationException("Authentication is required"));
    }

    @Override
    public Optional<User> getCurrentUserOptional() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return Optional.of(user);
        }
        if (principal instanceof UserDetails userDetails) {
            return findByEmail(userDetails.getUsername());
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void blockUser(Long userId) {
        User user = getById(userId);
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unblockUser(Long userId) {
        User user = getById(userId);
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && user.getRole() == Role.ADMIN;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User %s not found".formatted(username)));
    }
}
