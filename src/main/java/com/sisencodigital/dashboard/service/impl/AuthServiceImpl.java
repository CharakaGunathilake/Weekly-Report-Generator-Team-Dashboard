package com.sisencodigital.dashboard.service.impl;

import com.sisencodigital.dashboard.dto.request.LoginRequest;
import com.sisencodigital.dashboard.dto.request.RegisterRequest;
import com.sisencodigital.dashboard.dto.response.LoginResponse;
import com.sisencodigital.dashboard.dto.response.RegisterResponse;
import com.sisencodigital.dashboard.entity.User;
import com.sisencodigital.dashboard.enums.UserRole;
import com.sisencodigital.dashboard.enums.UserStatus;
import com.sisencodigital.dashboard.exceptions.custom.UserAlreadyExistsException;
import com.sisencodigital.dashboard.repository.UserRepository;
import com.sisencodigital.dashboard.service.AuthService;
import com.sisencodigital.dashboard.exceptions.custom.InvalidUserRoleException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public RegisterResponse register(RegisterRequest registerRequestDto) {
        String email = registerRequestDto.email().trim();
        String role = registerRequestDto.role().trim();

        if (!role.equalsIgnoreCase("team_member") && !role.equalsIgnoreCase("manager")) {
            throw new InvalidUserRoleException("Not a valid user role provided please try User or Admin");
        }

        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User with Email:" + email + " already exists");
        }

        String encodedPassword = passwordEncoder.encode(registerRequestDto.password().trim());

        User userToSave = User.builder()
                .name(registerRequestDto.name())
                .email(registerRequestDto.email())
                .password(encodedPassword)
                .status(UserStatus.ACTIVE)
                .role(UserRole.valueOf(registerRequestDto.role().toUpperCase()))
                .lastLoginAt(null)
                .build();

        User savedUser = userRepository.save(userToSave);

//        applicationEventPublisher.publishEvent(new EntityActivityEvent(
//                ENTITY_NAME,
//                savedUser.getId(),
//                ActionType.REGISTER,
//                String.format("%s registered with email: %s at %tc", savedUser.getRole().name(), savedUser.getEmail(), ZonedDateTime.now()),
//                savedUser.getId()
//        ));

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getStatus()
        );
    }

    @Override
    public LoginResponse login(LoginRequest loginRequestDto) {
        String email = loginRequestDto.username().toLowerCase().trim();
        String password = loginRequestDto.password().trim();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by email: " + email));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        String token = jwtService.createToken(
                Map.of("userId", user.getId()),
                user.getUsername(),
                user.getRole().name()
        );

        user.setLastLoginAt(Instant.now());
        final User savedUser = userRepository.save(user);

//        applicationEventPublisher.publishEvent(new EntityActivityEvent(
//                ENTITY_NAME,
//                savedUser.getId(),
//                ActionType.LOGIN,
//                String.format("%s logged in with email: %s at %tc", savedUser.getRole().name(), savedUser.getEmail(), ZonedDateTime.now()),
//                user.getId()
//        ));

        return new LoginResponse(
                token,
                user.getUsername(),
                user.getRole().name()
        );
    }
}
