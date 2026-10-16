package com.aurax.identity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_tenants")
@IdClass(UserTenantId.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserTenant {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Id
    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private boolean isDefault = false;

    @Column(name = "joined_at", nullable = false)
    @Builder.Default
    private LocalDateTime joinedAt = LocalDateTime.now();
}
