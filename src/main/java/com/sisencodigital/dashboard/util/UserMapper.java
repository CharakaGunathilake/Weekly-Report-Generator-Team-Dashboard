package com.sisencodigital.dashboard.util;

import com.sisencodigital.dashboard.dto.response.UserResponse;
import com.sisencodigital.dashboard.entity.User;

public class UserMapper {
    private UserMapper(){
    }

    public static UserResponse convertToResponse(User user) {
       return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                user.getLastLoginAt(),
                user.getCreatedAt()
        );
    }
}
