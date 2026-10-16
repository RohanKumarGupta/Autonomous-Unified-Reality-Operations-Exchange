package com.aurax.identity.repository;

import com.aurax.identity.entity.UserTenant;
import com.aurax.identity.entity.UserTenantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserTenantRepository extends JpaRepository<UserTenant, UserTenantId> {
    List<UserTenant> findAllByUserId(UUID userId);
    Optional<UserTenant> findByUserIdAndTenantId(UUID userId, UUID tenantId);
    boolean existsByUserIdAndTenantId(UUID userId, UUID tenantId);
}
