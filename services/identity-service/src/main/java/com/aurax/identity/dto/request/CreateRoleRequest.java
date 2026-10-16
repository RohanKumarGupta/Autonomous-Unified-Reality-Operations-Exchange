package com.aurax.identity.dto.request;

import jakarta.validation.constraints.*;
import java.util.List;

public record CreateRoleRequest(
    @NotBlank(message = "Role name is required")
    @Size(max = 100)
    String name,

    @Size(max = 500)
    String description,

    List<String> permissionNames
) {}
