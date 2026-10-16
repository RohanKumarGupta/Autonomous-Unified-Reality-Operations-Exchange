package com.aurax.identity.repository;

import com.aurax.identity.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByNameAndTenantId(String name, UUID tenantId);
    List<Role> findAllByTenantId(UUID tenantId);
}
