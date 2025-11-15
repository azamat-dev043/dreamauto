package com.example.dreamauto.mapper;

import com.example.dreamauto.dto.user.UserSummaryDto;
import com.example.dreamauto.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserSummaryDto toSummary(User user) {
        return new UserSummaryDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber(),
                user.getRole(),
                user.isActive()
        );
    }
}
