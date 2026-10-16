package com.aurax.identity.controller;

import com.aurax.common.web.ApiResponse;
import com.aurax.identity.dto.response.PermissionResponse;
import com.aurax.identity.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getPermissions() {
        return ResponseEntity.ok(ApiResponse.success(roleService.getAllPermissions()));
    }
}
