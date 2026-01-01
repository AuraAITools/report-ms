package com.aura.core.utils;

import com.aura.core.auth.User;
import com.aura.core.entities.accounts.TenantAwareRole;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


import static com.aura.core.entities.accounts.TenantAwareRole.ROLE_TYPE.INSTITUTION_LEVEL;
import static com.aura.core.entities.accounts.TenantAwareRole.ROLE_TYPE.TECHNICAL_USER;

public class AuthUtils {
    private AuthUtils() {
    }

    public static boolean hasAnyRoles(User user, String... roles) {
        Set<TenantAwareRole> authorizedRoles = Arrays
                .stream(roles)
                .map(roleName -> TenantAwareRole
                        .builder()
                        .tenantId(user.getCurrentTenantAccessed())
                        .roleType(INSTITUTION_LEVEL)
                        .institutionId(user.getCurrentTenantAccessed())
                        .name(roleName)
                        .build())
                .collect(Collectors.toSet());
        return user
                .getTenantRoles()
                .stream()
                .anyMatch(authorizedRoles::contains);
    }

    public static boolean hasAllRoles(User user, String... roles) {
        Set<TenantAwareRole> authorizedRoles = Arrays
                .stream(roles)
                .map(roleName -> TenantAwareRole
                        .builder()
                        .tenantId(user.getCurrentTenantAccessed())
                        .roleType(INSTITUTION_LEVEL)
                        .institutionId(user.getCurrentTenantAccessed())
                        .name(roleName)
                        .build())
                .collect(Collectors.toSet());
        return authorizedRoles.containsAll(user.getTenantRoles());
    }

    public static boolean hasAnyTechnicalUserRole(User user, String... roles) {
        Set<TenantAwareRole> authorizedRoles = Arrays
                .stream(roles)
                .map(roleName -> TenantAwareRole
                        .builder()
                        .tenantId(null)
                        .roleType(TECHNICAL_USER)
                        .institutionId(null)
                        .name(roleName)
                        .build())
                .collect(Collectors.toSet());
        return user
                .getTenantRoles()
                .stream()
                .anyMatch(authorizedRoles::contains);
    }

    public static boolean isAggregateQuery(User user) {
        return "*".equals(user.getCurrentTenantAccessed());
    }
}
