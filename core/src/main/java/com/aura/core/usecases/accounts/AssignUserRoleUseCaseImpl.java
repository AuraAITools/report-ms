package com.aura.core.usecases.accounts;

import com.aura.core.clients.IdentityService;
import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.clients.UserIdentity;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AssignUserRoleUseCaseImpl implements AssignUserRoleUseCase {
    private final IdentityService identityService;
    private final RelationshipAuthorizationService relationshipService;

    @Override
    public void assignRole(String email, String resourceType, String resourceId, String role) {
        List<UserIdentity> users = identityService.findByEmail(email);
        if (users.isEmpty()) {
            throw new IllegalStateException("User with email '%s' not found".formatted(email));
        }
        String userId = users.getFirst().getId();
        relationshipService.writeRelationship(resourceType, resourceId, role, "user", userId);
    }
}
