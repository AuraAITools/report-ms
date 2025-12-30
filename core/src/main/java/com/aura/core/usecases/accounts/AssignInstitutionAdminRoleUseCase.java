package com.aura.core.usecases.accounts;

import com.aura.core.clients.IdentityService;


import static com.aura.core.auth.RoleTypes.INSTITUTION_ADMIN;

public class AssignInstitutionAdminRoleUseCase implements AssignRoleUseCase {
    private final IdentityService identityService;
    private final String tenantId;
    private final String userId;

    public AssignInstitutionAdminRoleUseCase(IdentityService identityService, String tenantId, String userId) {
        this.identityService = identityService;
        this.tenantId = tenantId;
        this.userId = userId;
    }

    @Override
    public void assignRole() {
        String institutionAdminRole = String.format("%s_%s", tenantId, INSTITUTION_ADMIN);
        identityService.assignRolesToUser(userId, institutionAdminRole);
    }
}
