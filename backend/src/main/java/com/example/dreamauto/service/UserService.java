package com.example.dreamauto.service;

import com.example.dreamauto.model.entity.User;
import java.util.Optional;

public interface UserService {

    User getById(Long id);

    User getByEmail(String email);

    Optional<User> findByEmail(String email);

    User getCurrentUser();

    Optional<User> getCurrentUserOptional();

    void blockUser(Long userId);

    void unblockUser(Long userId);

    boolean isAdmin(User user);
}
