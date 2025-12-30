package com.aura.core.clients;

import com.aura.core.entities.accounts.TenantAwareRole;
import java.util.List;

public interface IdentityService {
    void createDefaultTenantAwareRoles(List<TenantAwareRole> roles);

    List<UserIdentity> findByEmail(String email);

    UserIdentity createUserIdentity(UserIdentity userIdentity, String tenantId);

    void addTenantToUser(String userId, String tenantId);

    void assignRolesToUser(String userId, String... roles);

}
