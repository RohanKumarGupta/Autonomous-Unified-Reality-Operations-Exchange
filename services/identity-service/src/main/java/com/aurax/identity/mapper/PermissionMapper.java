package com.aurax.identity.mapper;

import com.aurax.identity.dto.response.PermissionResponse;
import com.aurax.identity.entity.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {

    public PermissionResponse toResponse(Permission permission) {
        return new PermissionResponse(
            permission.getId(),
            permission.getName(),
            permission.getResource(),
            permission.getAction(),
            permission.getDescription()
        );
    }
}
