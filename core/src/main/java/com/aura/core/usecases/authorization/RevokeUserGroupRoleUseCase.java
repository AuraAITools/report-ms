package com.aura.core.usecases.authorization;

public interface RevokeUserGroupRoleUseCase {

    void revoke(String userGroupType, String userGroupId, String role, String targetUserId);
}
