package com.aura.core.usecases.authorization;

import com.aura.core.authorization.PermissionRegistry;
import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.clients.RelationshipAuthorizationService.RelationshipTuple;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LookupUserGroupRelationsUseCaseImpl implements LookupUserGroupRelationsUseCase {

    private final RelationshipAuthorizationService relationshipService;

    @Override
    public List<ResourceRelations> lookupUserGroupRelations(String userId,
                                                            List<String> resourceTypes,
                                                            @Nullable String resourceId) {
        Map<String, List<String>> aggregated = new HashMap<>();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (String resourceType : resourceTypes) {
            if (!PermissionRegistry.USER_GROUP_PERMISSIONS.containsKey(resourceType)) {
                continue;
            }
            futures.add(CompletableFuture.runAsync(() -> {
                List<RelationshipTuple> tuples = relationshipService.readRelationships(
                        resourceType, resourceId, null, "user", userId);
                for (RelationshipTuple tuple : tuples) {
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
                    return new ResourceRelations(parts[0], parts[1], e.getValue());
                })
                .toList();
    }
}
