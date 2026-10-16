package com.aurax.identity.dto.response;

import java.util.UUID;

public record TenantInfo(
    UUID tenantId,
    boolean isDefault
) {}
