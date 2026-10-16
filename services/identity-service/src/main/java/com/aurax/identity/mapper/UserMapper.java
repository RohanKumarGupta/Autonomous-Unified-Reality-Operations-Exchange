package com.aurax.identity.mapper;

import com.aurax.identity.dto.response.RoleResponse;
import com.aurax.identity.dto.response.TenantInfo;
import com.aurax.identity.dto.response.UserResponse;
import com.aurax.identity.entity.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse toResponse(User user, List<UserRole> userRoles, List<UserTenant> userTenants) {
        List<RoleResponse> roles = userRoles != null
            ? userRoles.stream().map(this::toRoleResponse).collect(Collectors.toList())
            : Collections.emptyList();

        List<TenantInfo> tenants = userTenants != null
            ? userTenants.stream().map(ut -> new TenantInfo(ut.getTenantId(), ut.isDefault())).collect(Collectors.toList())
            : Collections.emptyList();

        return new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getPhone(),
            user.getAvatarUrl(),
            user.getStatus().name(),
            roles,
            tenants,
            user.getCreatedAt()
        );
    }

    public UserResponse toSimpleResponse(User user) {
        return toResponse(user, Collections.emptyList(), Collections.emptyList());
    }

    private RoleResponse toRoleResponse(UserRole userRole) {
        Role role = userRole.getRole();
        List<String> permissionNames = role.getPermissions() != null
            ? role.getPermissions().stream()
                .map(Permission::getName)
                .collect(Collectors.toList())
            : Collections.emptyList();
        return new RoleResponse(role.getId(), role.getName(), role.getDescription(), permissionNames, role.isSystemRole());
    }
}
