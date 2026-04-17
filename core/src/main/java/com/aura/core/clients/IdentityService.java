package com.aura.core.clients;

import java.util.List;

public interface IdentityService {

    List<UserIdentity> findByEmail(String email);

    UserIdentity createUserIdentityAndTagToTenant(UserIdentity userIdentity, String tenantId);

    void tagTenantToUser(String userId, String tenantId);

    void assignRolesToUser(String userId, String... roles);

}
