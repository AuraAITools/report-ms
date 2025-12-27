package com.aura.core.auth;

import com.aura.core.entities.accounts.TenantAwareRole;
import java.util.Set;

public class RBACAuthorizationStrategy implements AuthorizationStrategy {
    private Set<TenantAwareRole> allowedRoles;
    private User user;

    public RBACAuthorizationStrategy(Set<TenantAwareRole> allowedRoles, User user) {
        this.allowedRoles = allowedRoles;
        this.user = user;
    }

    @Override
    public void checkAuthorization() {
        boolean hasAuthorization = user
                .getTenantRoles()
                .stream()
                .anyMatch(role -> allowedRoles.contains(role));
        if (!hasAuthorization) throw new RuntimeException("Authorization Denied");
    }
}
