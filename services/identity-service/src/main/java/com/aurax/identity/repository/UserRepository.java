package com.aurax.identity.repository;

import com.aurax.identity.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findAllByIdIn(List<UUID> ids);

    @Query("SELECT u FROM User u JOIN UserTenant ut ON u.id = ut.userId WHERE ut.tenantId = :tenantId")
    Page<User> findAllByTenantId(UUID tenantId, Pageable pageable);
}
