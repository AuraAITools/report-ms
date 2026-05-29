package com.aura.core.usecases.authorization;

import com.aura.core.clients.RelationshipAuthorizationService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Lookup permissions use case")
class LookupPermissionsUseCaseImplTest {

    @Test
    @DisplayName("Should aggregate permissions across resource types")
    void shouldAggregatePermissions() {
        var stub = new StubRelationshipAuthorizationService();
        // system:global — user has "view" and "manage"
        stub.addCheckPermissionResult("system", "global", "view", true);
        stub.addCheckPermissionResult("system", "global", "manage", true);
        stub.addCheckPermissionResult("system", "global", "create_institution", false);
        stub.addCheckPermissionResult("system", "global", "create_account", false);
        stub.addCheckPermissionResult("system", "global", "delegate", false);
        // outlet — user can view outlet-1
        stub.addLookupResult("outlet", "view", List.of("outlet-1"));
        stub.addLookupResult("outlet", "manage", List.of());
        stub.addLookupResult("outlet", "create_course", List.of());
        stub.addLookupResult("outlet", "create_lesson", List.of());
        stub.addLookupResult("outlet", "view_all", List.of());
        stub.addLookupResult("outlet", "delegate", List.of());

        var useCase = new LookupPermissionsUseCaseImpl(stub);
        List<ResourcePermission> result = useCase.lookupPermissions("user-1");

        // Should contain system:global with [view, manage]
        var systemPerms = result.stream()
                .filter(rp -> rp.resourceType().equals("system") && rp.resourceId().equals("global"))
                .findFirst();
        assertThat(systemPerms).isPresent();
        assertThat(systemPerms.get().permissions()).containsExactlyInAnyOrder("view", "manage");

        // Should contain outlet:outlet-1 with [view]
        var outletPerms = result.stream()
                .filter(rp -> rp.resourceType().equals("outlet") && rp.resourceId().equals("outlet-1"))
                .findFirst();
        assertThat(outletPerms).isPresent();
        assertThat(outletPerms.get().permissions()).containsExactly("view");
    }

    @Test
    @DisplayName("Should return empty list when user has no permissions")
    void shouldReturnEmptyWhenNoPermissions() {
        var stub = new StubRelationshipAuthorizationService();
        // All system checks return false
        stub.setDefaultCheckResult(false);

        var useCase = new LookupPermissionsUseCaseImpl(stub);
        List<ResourcePermission> result = useCase.lookupPermissions("user-99");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should merge permissions for same resource from different lookups")
    void shouldMergePermissionsForSameResource() {
        var stub = new StubRelationshipAuthorizationService();
        stub.setDefaultCheckResult(false);
        stub.addLookupResult("institution", "view", List.of("inst-1"));
        stub.addLookupResult("institution", "manage", List.of("inst-1"));
        stub.addLookupResult("institution", "create_outlet", List.of("inst-1"));

        var useCase = new LookupPermissionsUseCaseImpl(stub);
        List<ResourcePermission> result = useCase.lookupPermissions("user-1");

        var instPerms = result.stream()
                .filter(rp -> rp.resourceType().equals("institution") && rp.resourceId().equals("inst-1"))
                .findFirst();
        assertThat(instPerms).isPresent();
        assertThat(instPerms.get().permissions()).containsExactlyInAnyOrder("view", "manage", "create_outlet");
    }

    // --- Stub ---

    private static class StubRelationshipAuthorizationService implements RelationshipAuthorizationService {
        private final Map<String, List<String>> lookupResults = new java.util.HashMap<>();
        private final Map<String, Boolean> checkResults = new java.util.HashMap<>();
        private boolean defaultCheckResult = false;

        void addLookupResult(String resourceType, String permission, List<String> resourceIds) {
            lookupResults.put(resourceType + ":" + permission, resourceIds);
        }

        void addCheckPermissionResult(String resourceType, String resourceId, String permission, boolean result) {
            checkResults.put(resourceType + ":" + resourceId + ":" + permission, result);
        }

        void setDefaultCheckResult(boolean result) {
            defaultCheckResult = result;
        }

        @Override
        public List<String> lookupResources(String resourceType, String permission,
                                            String subjectType, String subjectId) {
            return lookupResults.getOrDefault(resourceType + ":" + permission, List.of());
        }

        @Override
        public boolean checkPermission(String resourceType, String resourceId,
                                       String permission, String subjectType, String subjectId) {
            return checkResults.getOrDefault(resourceType + ":" + resourceId + ":" + permission, defaultCheckResult);
        }

        @Override
        public void writeRelationship(String resourceType, String resourceId,
                                      String relation, String subjectType, String subjectId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteRelationship(String resourceType, String resourceId,
                                       String relation, String subjectType, String subjectId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeRelationships(List<Relationship> relationships) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllRelationships(String resourceType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllRelationshipsForResource(String resourceType, String resourceId) {
            throw new UnsupportedOperationException();
        }
    }
}
