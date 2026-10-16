package com.aurax.identity.service;

import com.aurax.identity.dto.request.LoginRequest;
import com.aurax.identity.dto.request.RefreshTokenRequest;
import com.aurax.identity.dto.request.RegisterRequest;
import com.aurax.identity.dto.response.AuthResponse;

import java.util.UUID;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
    void logout(UUID userId);
}
