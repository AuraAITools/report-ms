package com.aura.core.clients;

import com.aura.core.entities.accounts.TenantAwareRole;
import java.util.List;

public interface IdentityService {
    void createDefaultTenantAwareRoles(List<TenantAwareRole> roles);
}
