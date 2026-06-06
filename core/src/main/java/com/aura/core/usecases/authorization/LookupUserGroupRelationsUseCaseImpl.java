package com.aura.core.usecases.authorization;

import com.aura.core.authorization.PermissionRegistry;
import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.clients.RelationshipAuthorizationService.RelationshipTuple;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LookupUserGroupRelationsUseCaseImpl implements LookupUserGroupRelationsUseCase {

    private final RelationshipAuthorizationService relationshipService;

    @Override
    public List<ResourceRelations> lookupUserGroupRelations(String userId,
                                                            Map<String, Set<String>> allowedResourceIdsByType) {
        Map<String, List<String>> aggregated = new HashMap<>();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (Map.Entry<String, Set<String>> entry : allowedResourceIdsByType.entrySet()) {
            String resourceType = entry.getKey();
            Set<String> allowed = entry.getValue();
            if (allowed.isEmpty()) {
                continue;
            }
            if (!PermissionRegistry.USER_GROUP_PERMISSIONS.containsKey(resourceType)) {
                continue;
            }
            futures.add(CompletableFuture.runAsync(() -> {
                List<RelationshipTuple> tuples = relationshipService.readRelationships(
                        resourceType, null, null, "user", userId);
                for (RelationshipTuple tuple : tuples) {
                    if (!allowed.contains(tuple.resourceId())) {
                        continue;
                    }
                    String key = resourceType + ":" + tuple.resourceId();
                    synchronized (aggregated) {
                        aggregated.computeIfAbsent(key, k -> new ArrayList<>()).add(tuple.relation());
                    }
                }
            }));
        }

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();

        return aggregated.entrySet().stream()
                .map(e -> {
                    String[] parts = e.getKey().split(":", 2);
                    return new ResourceRelations(parts[0], parts[1], Collections.unmodifiableList(e.getValue()));
                })
                .toList();
    }
}
