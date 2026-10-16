package com.aurax.identity.service;

import com.aurax.common.web.PageResponse;
import com.aurax.identity.dto.request.AssignRoleRequest;
import com.aurax.identity.dto.request.UpdateUserRequest;
import com.aurax.identity.dto.response.UserResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    UserResponse getCurrentUser();
    UserResponse getUserById(UUID id);
    PageResponse<UserResponse> getAllUsers(UUID tenantId, Pageable pageable);
    UserResponse updateUser(UUID id, UpdateUserRequest request);
    UserResponse assignRoles(UUID userId, UUID tenantId, AssignRoleRequest request);
}
