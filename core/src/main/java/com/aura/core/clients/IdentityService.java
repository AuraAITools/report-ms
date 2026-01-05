package com.aura.core.clients;

import com.aura.core.entities.accounts.TenantAwareRole;
import java.util.List;

public interface IdentityService {
    void createDefaultTenantAwareRoles(List<TenantAwareRole> roles);

    List<UserIdentity> findByEmail(String email);

    UserIdentity createUserIdentityAndTagToTenant(UserIdentity userIdentity, String tenantId);

    void tagTenantToUser(String userId, String tenantId);

    void assignRolesToUser(String userId, String... roles);

}
