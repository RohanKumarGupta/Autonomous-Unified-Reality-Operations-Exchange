package com.aurax.identity.dto.response;

import java.util.UUID;

public record PermissionResponse(
    UUID id,
    String name,
    String resource,
    String action,
    String description
) {}
