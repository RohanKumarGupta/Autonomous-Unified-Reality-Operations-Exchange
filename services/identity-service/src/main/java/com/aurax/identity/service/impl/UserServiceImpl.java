package com.aurax.identity.service.impl;

import com.aurax.common.exception.ResourceNotFoundException;
import com.aurax.common.security.SecurityUtils;
import com.aurax.common.web.PageResponse;
import com.aurax.identity.dto.request.AssignRoleRequest;
import com.aurax.identity.dto.request.UpdateUserRequest;
import com.aurax.identity.dto.response.UserResponse;
import com.aurax.identity.entity.Role;
import com.aurax.identity.entity.User;
import com.aurax.identity.entity.UserRole;
import com.aurax.identity.entity.UserTenant;
import com.aurax.identity.mapper.UserMapper;
import com.aurax.identity.repository.*;
import com.aurax.identity.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserTenantRepository userTenantRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser() {
        UUID userId = SecurityUtils.getCurrentUserId();
        UUID tenantId = SecurityUtils.getCurrentTenantId();
        return getUserWithDetails(userId, tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        UUID tenantId = SecurityUtils.getCurrentTenantId();
        return getUserWithDetails(id, tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getAllUsers(UUID tenantId, Pageable pageable) {
        Page<User> users = userRepository.findAllByTenantId(tenantId, pageable);
        List<UserResponse> responses = users.getContent().stream()
                .map(user -> {
                    List<UserRole> roles = userRoleRepository.findAllByUserIdAndTenantId(user.getId(), tenantId);
                    List<UserTenant> tenants = userTenantRepository.findAllByUserId(user.getId());
                    return userMapper.toResponse(user, roles, tenants);
                })
                .toList();

        return new PageResponse<>(
                responses,
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.hasNext(),
                users.hasPrevious()
        );
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));

        if (request.firstName() != null) user.setFirstName(request.firstName());
        if (request.lastName() != null) user.setLastName(request.lastName());
        if (request.phone() != null) user.setPhone(request.phone());
        if (request.avatarUrl() != null) user.setAvatarUrl(request.avatarUrl());

        user = userRepository.save(user);

        UUID tenantId = SecurityUtils.getCurrentTenantId();
        return getUserWithDetails(user.getId(), tenantId);
    }

    @Override
    @Transactional
    public UserResponse assignRoles(UUID userId, UUID tenantId, AssignRoleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        for (UUID roleId : request.roleIds()) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId.toString()));

            boolean exists = userRoleRepository.findAllByUserIdAndTenantId(userId, tenantId).stream()
                    .anyMatch(ur -> ur.getRole().getId().equals(roleId));

            if (!exists) {
                UserRole userRole = UserRole.builder()
                        .user(user)
                        .role(role)
                        .tenantId(tenantId)
                        .grantedBy(SecurityUtils.getCurrentUserId())
                        .build();
                userRoleRepository.save(userRole);
            }
        }

        return getUserWithDetails(userId, tenantId);
    }

    private UserResponse getUserWithDetails(UUID userId, UUID tenantId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));
        List<UserRole> userRoles = userRoleRepository.findAllByUserIdAndTenantId(userId, tenantId);
        List<UserTenant> userTenants = userTenantRepository.findAllByUserId(userId);
        return userMapper.toResponse(user, userRoles, userTenants);
    }
}
