package com.aura.core.usecases.authorization;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LookupUserGroupRelationsUseCase {
    /**
     * For each (resourceType, allowedResourceIds) entry, find the relations the user
     * holds on each resource of that type whose id is in the allowed set. One SpiceDB
     * ReadRelationships call per type (subject filter only), then tuples are filtered
     * by the allowed ids on the Java side. Types not in {@code PermissionRegistry.USER_GROUP_PERMISSIONS}
     * and types whose allowed set is empty are skipped.
     */
    List<ResourceRelations> lookupUserGroupRelations(String userId,
                                                     Map<String, Set<String>> allowedResourceIdsByType);
}
