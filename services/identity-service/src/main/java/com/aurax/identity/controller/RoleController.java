package com.aurax.identity.controller;

import com.aurax.common.security.SecurityUtils;
import com.aurax.common.web.ApiResponse;
import com.aurax.identity.dto.request.CreateRoleRequest;
import com.aurax.identity.dto.response.RoleResponse;
import com.aurax.identity.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(
            @Valid @RequestBody CreateRoleRequest request) {
        var tenantId = SecurityUtils.getCurrentTenantId();
        RoleResponse response = roleService.createRole(tenantId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getRoles() {
        var tenantId = SecurityUtils.getCurrentTenantId();
        return ResponseEntity.ok(ApiResponse.success(roleService.getRolesByTenantId(tenantId)));
    }
}
