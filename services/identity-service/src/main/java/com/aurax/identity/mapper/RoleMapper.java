package com.aurax.identity.mapper;

import com.aurax.identity.dto.response.RoleResponse;
import com.aurax.identity.entity.Permission;
import com.aurax.identity.entity.Role;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RoleMapper {

    public RoleResponse toResponse(Role role) {
        return new RoleResponse(
            role.getId(),
            role.getName(),
            role.getDescription(),
            role.getPermissions() != null
                ? role.getPermissions().stream().map(Permission::getName).collect(Collectors.toList())
                : java.util.Collections.emptyList(),
            role.isSystemRole()
        );
    }
}
