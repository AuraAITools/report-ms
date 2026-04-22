package com.aura.core.auth;

import java.util.List;
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
    private String userId;

    private String email;

    private boolean emailVerified;

    private String name;

    private String givenName;

    private String familyName;

    private Set<String> tenantIds;

    private List<String> groups;

    private String currentTenantAccessed;

    public boolean accessToCurrentTenantIsAllowed() {
        return tenantIds.contains(currentTenantAccessed);
    }

}
