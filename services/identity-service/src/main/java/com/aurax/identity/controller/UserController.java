package com.aurax.identity.controller;

import com.aurax.common.security.SecurityUtils;
import com.aurax.common.web.ApiResponse;
import com.aurax.common.web.PageResponse;
import com.aurax.identity.dto.request.AssignRoleRequest;
import com.aurax.identity.dto.request.UpdateUserRequest;
import com.aurax.identity.dto.response.UserResponse;
import com.aurax.identity.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        return ResponseEntity.ok(ApiResponse.success(userService.getCurrentUser()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getAllUsers(
            @PageableDefault(size = 20) Pageable pageable) {
        UUID tenantId = SecurityUtils.getCurrentTenantId();
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers(tenantId, pageable)));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateCurrentUser(
            @Valid @RequestBody UpdateUserRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(userService.updateUser(userId, request)));
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<ApiResponse<UserResponse>> assignRoles(
            @PathVariable UUID id,
            @Valid @RequestBody AssignRoleRequest request) {
        UUID tenantId = SecurityUtils.getCurrentTenantId();
        return ResponseEntity.ok(ApiResponse.success(userService.assignRoles(id, tenantId, request)));
    }
}
