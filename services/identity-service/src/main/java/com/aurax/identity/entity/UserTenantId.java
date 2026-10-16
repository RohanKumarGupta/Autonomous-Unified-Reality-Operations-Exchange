package com.aurax.identity.entity;

import lombok.*;
import java.io.Serializable;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class UserTenantId implements Serializable {
    private UUID userId;
    private UUID tenantId;
}
