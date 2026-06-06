package com.aura.core.usecases.authorization;

import com.aura.core.clients.RelationshipAuthorizationService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Lookup group permissions use case")
class LookupGroupPermissionsUseCaseImplTest {

    @Test
    @DisplayName("Should aggregate user_group permissions across types")
    void shouldAggregateUserGroupPermissions() {
        var stub = new StubRelationshipAuthorizationService();
        stub.addLookupResult("outlet_user_group", "view", List.of("outlet-1"));
        stub.addLookupResult("outlet_user_group", "update", List.of("outlet-1"));
        stub.addLookupResult("outlet_user_group", "delete", List.of());
        stub.addLookupResult("course_user_group", "group_view", List.of("course-7"));
        stub.addLookupResult("course_user_group", "view", List.of("course-7"));

        var useCase = new LookupGroupPermissionsUseCaseImpl(stub);
        List<ResourcePermission> result = useCase.lookupGroupPermissions("user-1");

        var outletGroup = result.stream()
                .filter(rp -> rp.resourceType().equals("outlet_user_group") && rp.resourceId().equals("outlet-1"))
                .findFirst();
        assertThat(outletGroup).isPresent();
        assertThat(outletGroup.get().permissions()).containsExactlyInAnyOrder("view", "update");

        var courseGroup = result.stream()
                .filter(rp -> rp.resourceType().equals("course_user_group") && rp.resourceId().equals("course-7"))
                .findFirst();
        assertThat(courseGroup).isPresent();
        assertThat(courseGroup.get().permissions()).containsExactlyInAnyOrder("group_view", "view");
    }

    @Test
    @DisplayName("Should return empty when user has no group permissions")
    void shouldReturnEmptyWhenNoGroupPermissions() {
        var stub = new StubRelationshipAuthorizationService();

        var useCase = new LookupGroupPermissionsUseCaseImpl(stub);
        List<ResourcePermission> result = useCase.lookupGroupPermissions("nobody");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should not include non-user_group resource types")
    void shouldOnlyReturnUserGroupTypes() {
        var stub = new StubRelationshipAuthorizationService();
        stub.addLookupResult("outlet_user_group", "view", List.of("outlet-1"));
        // Plain resource types should never be queried, but if they were, they shouldn't appear.
        stub.addLookupResult("outlet", "view", List.of("outlet-1"));
        stub.addLookupResult("institution", "view", List.of("inst-1"));

        var useCase = new LookupGroupPermissionsUseCaseImpl(stub);
        List<ResourcePermission> result = useCase.lookupGroupPermissions("user-1");

        assertThat(result).allMatch(rp -> rp.resourceType().endsWith("_user_group"));
    }

    private static class StubRelationshipAuthorizationService implements RelationshipAuthorizationService {
        private final Map<String, List<String>> lookupResults = new HashMap<>();

        void addLookupResult(String resourceType, String permission, List<String> resourceIds) {
            lookupResults.put(resourceType + ":" + permission, resourceIds);
        }

        @Override
        public List<String> lookupResources(String resourceType, String permission,
                                            String subjectType, String subjectId) {
            return lookupResults.getOrDefault(resourceType + ":" + permission, List.of());
        }

        @Override
        public boolean checkPermission(String resourceType, String resourceId,
                                       String permission, String subjectType, String subjectId) {
            return false;
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

        @Override
        public List<RelationshipTuple> readRelationships(String resourceType,
                                                         String resourceId,
                                                         String relation,
                                                         String subjectType, String subjectId) {
            throw new UnsupportedOperationException();
        }
    }
}
