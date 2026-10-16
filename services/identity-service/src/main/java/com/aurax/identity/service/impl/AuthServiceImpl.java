package com.aurax.identity.service.impl;

import com.aurax.common.security.JwtTokenProvider;
import com.aurax.identity.dto.request.LoginRequest;
import com.aurax.identity.dto.request.RefreshTokenRequest;
import com.aurax.identity.dto.request.RegisterRequest;
import com.aurax.identity.dto.response.AuthResponse;
import com.aurax.identity.dto.response.UserResponse;
import com.aurax.identity.entity.*;
import com.aurax.identity.exception.EmailAlreadyExistsException;
import com.aurax.identity.exception.InvalidCredentialsException;
import com.aurax.identity.exception.TokenExpiredException;
import com.aurax.identity.mapper.UserMapper;
import com.aurax.identity.repository.*;
import com.aurax.identity.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserTenantRepository userTenantRepository;
    private final UserRoleRepository userRoleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    @Value("${aurax.jwt.expiration:3600000}")
    private long jwtExpiration;

    @Value("${aurax.jwt.refresh-expiration:86400000}")
    private long refreshExpiration;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering user with email: {}", request.email());

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .status(UserStatus.ACTIVE)
                .emailVerified(false)
                .build();

        user = userRepository.save(user);

        // Create a default tenant for the user
        UUID tenantId = UUID.randomUUID();
        UserTenant userTenant = UserTenant.builder()
                .userId(user.getId())
                .tenantId(tenantId)
                .isDefault(true)
                .build();
        userTenantRepository.save(userTenant);

        // Assign default role if exists
        roleRepository.findByNameAndTenantId("ADMIN", tenantId)
                .ifPresent(role -> {
                    UserRole userRole = UserRole.builder()
                            .user(user)
                            .role(role)
                            .tenantId(tenantId)
                            .build();
                    userRoleRepository.save(userRole);
                });

        List<UserRole> userRoles = userRoleRepository.findAllByUserIdAndTenantId(user.getId(), tenantId);
        List<UserTenant> userTenants = userTenantRepository.findAllByUserId(user.getId());
        UserResponse userResponse = userMapper.toResponse(user, userRoles, userTenants);

        List<String> roles = userRoles.stream()
                .map(ur -> ur.getRole().getName())
                .toList();

        String accessToken = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), tenantId, roles);
        String refreshToken = createRefreshToken(user);

        log.info("User registered successfully: {}", user.getId());

        return new AuthResponse(accessToken, refreshToken, jwtExpiration / 1000, userResponse);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.email());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        // Get default tenant
        List<UserTenant> userTenants = userTenantRepository.findAllByUserId(user.getId());
        UUID tenantId = userTenants.stream()
                .filter(UserTenant::isDefault)
                .map(UserTenant::getTenantId)
                .findFirst()
                .orElse(userTenants.isEmpty() ? UUID.randomUUID() : userTenants.get(0).getTenantId());

        List<UserRole> userRoles = userRoleRepository.findAllByUserIdAndTenantId(user.getId(), tenantId);
        List<String> roles = userRoles.stream()
                .map(ur -> ur.getRole().getName())
                .toList();

        String accessToken = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), tenantId, roles);
        String refreshToken = createRefreshToken(user);

        UserResponse userResponse = userMapper.toResponse(user, userRoles, userTenants);

        log.info("User logged in successfully: {}", user.getId());

        return new AuthResponse(accessToken, refreshToken, jwtExpiration / 1000, userResponse);
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(TokenExpiredException::new);

        if (storedToken.isRevoked() || storedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException();
        }

        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        User user = storedToken.getUser();
        List<UserTenant> userTenants = userTenantRepository.findAllByUserId(user.getId());
        UUID tenantId = userTenants.stream()
                .filter(UserTenant::isDefault)
                .map(UserTenant::getTenantId)
                .findFirst()
                .orElse(UUID.randomUUID());

        List<UserRole> userRoles = userRoleRepository.findAllByUserIdAndTenantId(user.getId(), tenantId);
        List<String> roles = userRoles.stream()
                .map(ur -> ur.getRole().getName())
                .toList();

        String accessToken = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), tenantId, roles);
        String newRefreshToken = createRefreshToken(user);

        UserResponse userResponse = userMapper.toResponse(user, userRoles, userTenants);

        return new AuthResponse(accessToken, newRefreshToken, jwtExpiration / 1000, userResponse);
    }

    @Override
    @Transactional
    public void logout(UUID userId) {
        log.info("Logging out user: {}", userId);
        refreshTokenRepository.deleteAllByUserId(userId);
    }

    private String createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusSeconds(refreshExpiration / 1000))
                .build();
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }
}
