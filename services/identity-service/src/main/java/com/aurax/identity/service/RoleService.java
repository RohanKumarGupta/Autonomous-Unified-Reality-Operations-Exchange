package com.aurax.identity.service;

import com.aurax.identity.dto.request.CreateRoleRequest;
import com.aurax.identity.dto.response.PermissionResponse;
import com.aurax.identity.dto.response.RoleResponse;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    RoleResponse createRole(UUID tenantId, CreateRoleRequest request);
    List<RoleResponse> getRolesByTenantId(UUID tenantId);
    List<PermissionResponse> getAllPermissions();
}
