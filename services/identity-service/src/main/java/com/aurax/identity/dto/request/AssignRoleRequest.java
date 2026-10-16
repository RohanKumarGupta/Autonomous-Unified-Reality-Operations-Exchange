package com.aurax.identity.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

public record AssignRoleRequest(
    @NotEmpty(message = "At least one role ID is required")
    List<UUID> roleIds
) {}
