package com.aura.core.usecases.authorization;

import com.aura.core.clients.RelationshipAuthorizationService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GrantUserGroupRoleUseCaseImpl implements GrantUserGroupRoleUseCase {

    private static final String USER_SUBJECT_TYPE = "user";

    private final RelationshipAuthorizationService relationshipService;

    @Override
    public void grant(String userGroupType, String userGroupId, String role, String targetUserId) {
        relationshipService.writeRelationship(
                userGroupType, userGroupId, role, USER_SUBJECT_TYPE, targetUserId);
    }
}
