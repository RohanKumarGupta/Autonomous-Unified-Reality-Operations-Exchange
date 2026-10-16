package com.aurax.identity.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String email,
    String firstName,
    String lastName,
    String phone,
    String avatarUrl,
    String status,
    List<RoleResponse> roles,
    List<TenantInfo> tenants,
    LocalDateTime createdAt
) {}
