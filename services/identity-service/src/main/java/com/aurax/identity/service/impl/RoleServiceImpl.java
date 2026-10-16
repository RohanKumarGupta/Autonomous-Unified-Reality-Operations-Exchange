package com.aurax.identity.service.impl;

import com.aurax.common.exception.DuplicateResourceException;
import com.aurax.identity.dto.request.CreateRoleRequest;
import com.aurax.identity.dto.response.PermissionResponse;
import com.aurax.identity.dto.response.RoleResponse;
import com.aurax.identity.entity.Permission;
import com.aurax.identity.entity.Role;
import com.aurax.identity.mapper.PermissionMapper;
import com.aurax.identity.mapper.RoleMapper;
import com.aurax.identity.repository.PermissionRepository;
import com.aurax.identity.repository.RoleRepository;
import com.aurax.identity.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    @Override
    @Transactional
    public RoleResponse createRole(UUID tenantId, CreateRoleRequest request) {
        roleRepository.findByNameAndTenantId(request.name(), tenantId)
                .ifPresent(r -> {
                    throw new DuplicateResourceException("Role", "name", request.name());
                });

        List<Permission> permissions = request.permissionNames() != null
                ? permissionRepository.findAllByNameIn(request.permissionNames())
                : List.of();

        Role role = Role.builder()
                .name(request.name())
                .description(request.description())
                .tenantId(tenantId)
                .permissions(new HashSet<>(permissions))
                .build();

        role = roleRepository.save(role);
        log.info("Role created: {} for tenant: {}", role.getName(), tenantId);

        return roleMapper.toResponse(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> getRolesByTenantId(UUID tenantId) {
        return roleRepository.findAllByTenantId(tenantId).stream()
                .map(roleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionResponse> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toResponse)
                .collect(Collectors.toList());
    }
}
