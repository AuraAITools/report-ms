package com.aura.core.usecases.authorization;

import java.util.List;
import org.jspecify.annotations.Nullable;

public interface LookupUserGroupMembersUseCase {
    List<UserGroupMember> lookupUserGroupMembers(String resourceType,
                                                 String resourceId,
                                                 @Nullable String relation);
}
