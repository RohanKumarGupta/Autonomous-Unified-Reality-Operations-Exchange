package com.aurax.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SecurityUtils {

    private SecurityUtils() {
        // Utility class
    }

    public static UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return (UserPrincipal) authentication.getPrincipal();
        }
        return null;
    }

    public static UUID getCurrentUserId() {
        UserPrincipal user = getCurrentUser();
        return user != null ? user.getUserId() : null;
    }

    public static UUID getCurrentTenantId() {
        UserPrincipal user = getCurrentUser();
        return user != null ? user.getTenantId() : null;
    }

    public static String getCurrentEmail() {
        UserPrincipal user = getCurrentUser();
        return user != null ? user.getEmail() : null;
    }

    public static List<String> getCurrentRoles() {
        UserPrincipal user = getCurrentUser();
        if (user != null) {
            return user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(role -> role.replaceFirst("^ROLE_", ""))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
