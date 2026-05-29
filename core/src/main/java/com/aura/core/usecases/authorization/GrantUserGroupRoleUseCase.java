package com.aura.core.usecases.authorization;

public interface GrantUserGroupRoleUseCase {

    void grant(String userGroupType, String userGroupId, String role, String targetUserId);
}
