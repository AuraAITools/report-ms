package com.aura.core.auth;

import com.aura.core.entities.accounts.TenantAwareRole;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Set<TenantAwareRole> tenantRoles;

    private String email;

    private Set<String> tenantIds;

    private boolean emailVerified;

    private String userId;

    private String name;

    private String givenName;

    private String familyName;

}
