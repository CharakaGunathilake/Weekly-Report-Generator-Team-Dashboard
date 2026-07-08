package com.sisencodigital.dashboard.service.impl;

import com.sisencodigital.dashboard.dto.response.UserResponse;
import com.sisencodigital.dashboard.entity.User;
import com.sisencodigital.dashboard.repository.UserRepository;
import com.sisencodigital.dashboard.service.UserService;
import com.sisencodigital.dashboard.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getMyProfile(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

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

    @Override
    public List<UserResponse> getUsers(Long userId) {
        if (userId != null) {
            User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return List.of(new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole(),
                    user.getStatus(),
                    user.getLastLoginAt(),
                    user.getCreatedAt()
            ));
        }

        return userRepository.findAll().stream()
                .map(UserMapper::convertToResponse)
                .toList();
    }
}
