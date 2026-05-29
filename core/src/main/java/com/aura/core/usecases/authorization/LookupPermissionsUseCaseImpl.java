package com.aura.core.usecases.authorization;

import com.aura.core.authorization.PermissionRegistry;
import com.aura.core.clients.RelationshipAuthorizationService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LookupPermissionsUseCaseImpl implements LookupPermissionsUseCase {

    private final RelationshipAuthorizationService relationshipService;

    @Override
    public List<ResourcePermission> lookupPermissions(String userId) {
        Map<String, List<String>> aggregated = new HashMap<>();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (var entry : PermissionRegistry.RESOURCE_PERMISSIONS.entrySet()) {
            String resourceType = entry.getKey();
            List<String> permissions = entry.getValue();

            if (PermissionRegistry.SYSTEM_RESOURCE_TYPE.equals(resourceType)) {
                for (String permission : permissions) {
                    futures.add(CompletableFuture.runAsync(() -> {
                        boolean hasPermission = relationshipService.checkPermission(
                                PermissionRegistry.SYSTEM_RESOURCE_TYPE,
                                PermissionRegistry.SYSTEM_RESOURCE_ID,
                                permission, "user", userId);
                        if (hasPermission) {
                            String key = PermissionRegistry.SYSTEM_RESOURCE_TYPE + ":"
                                    + PermissionRegistry.SYSTEM_RESOURCE_ID;
                            synchronized (aggregated) {
                                aggregated.computeIfAbsent(key, k -> new ArrayList<>()).add(permission);
                            }
                        }
                    }));
                }
            } else {
                for (String permission : permissions) {
                    futures.add(CompletableFuture.runAsync(() -> {
                        List<String> resourceIds = relationshipService.lookupResources(
                                resourceType, permission, "user", userId);
                        for (String resourceId : resourceIds) {
                            String key = resourceType + ":" + resourceId;
                            synchronized (aggregated) {
                                aggregated.computeIfAbsent(key, k -> new ArrayList<>()).add(permission);
                            }
                        }
                    }));
                }
            }
        }

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();

        return aggregated.entrySet().stream()
                .map(e -> {
                    String[] parts = e.getKey().split(":", 2);
                    return new ResourcePermission(parts[0], parts[1], e.getValue());
                })
                .toList();
    }
}
