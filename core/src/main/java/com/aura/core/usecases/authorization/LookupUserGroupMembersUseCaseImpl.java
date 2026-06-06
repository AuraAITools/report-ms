package com.aura.core.usecases.authorization;

import com.aura.core.authorization.PermissionRegistry;
import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.clients.RelationshipAuthorizationService.RelationshipTuple;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LookupUserGroupMembersUseCaseImpl implements LookupUserGroupMembersUseCase {

    private final RelationshipAuthorizationService relationshipService;

    @Override
    public List<UserGroupMember> lookupUserGroupMembers(String resourceType,
                                                        String resourceId,
                                                        @Nullable String relation) {
        if (!PermissionRegistry.USER_GROUP_PERMISSIONS.containsKey(resourceType)) {
            return List.of();
        }

        List<RelationshipTuple> tuples = relationshipService.readRelationships(
                resourceType, resourceId, relation, "user", null);

        Map<String, List<String>> aggregated = new LinkedHashMap<>();
        for (RelationshipTuple tuple : tuples) {
            aggregated.computeIfAbsent(tuple.subjectId(), k -> new ArrayList<>())
                    .add(tuple.relation());
        }

        return aggregated.entrySet().stream()
                .map(e -> new UserGroupMember(e.getKey(), e.getValue()))
                .toList();
    }
}
