package com.aura.core.usecases.accounts;

import com.aura.core.clients.IdentityService;


import static com.aura.core.usecases.onboarding.RoleTypes.STUDENT_REPORT_MOBILE_ROLE;

public class AssignParentRoleUseCase implements AssignRoleUseCase {
    private final IdentityService identityService;
    private final String tenantId;
    private final String userId;

    public AssignParentRoleUseCase(IdentityService identityService, String tenantId, String userId) {
        this.identityService = identityService;
        this.tenantId = tenantId;
        this.userId = userId;
    }

    @Override
    public void assignRole() {
        String parentRole = String.format("%s_%s", tenantId, STUDENT_REPORT_MOBILE_ROLE);
        identityService.assignRolesToUser(userId, parentRole);
    }
}
