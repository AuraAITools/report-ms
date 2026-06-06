package com.aura.core.usecases.authorization;

import com.aura.core.clients.RelationshipAuthorizationService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Lookup user group relations use case")
class LookupUserGroupRelationsUseCaseImplTest {

    @Test
    @DisplayName("Should aggregate multiple relations on the same resource into one entry")
    void aggregatesMultipleRelationsOnSameResource() {
        var stub = new StubRelationshipAuthorizationService();
        stub.addRelationships("outlet_user_group",
                new RelationshipAuthorizationService.RelationshipTuple("outlet-1", "admin", "user-1"),
                new RelationshipAuthorizationService.RelationshipTuple("outlet-1", "group_admin", "user-1"));

        var useCase = new LookupUserGroupRelationsUseCaseImpl(stub);
        List<ResourceRelations> result = useCase.lookupUserGroupRelations(
                "user-1", List.of("outlet_user_group"), null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).resourceId()).isEqualTo("outlet-1");
        assertThat(result.get(0).relations()).containsExactlyInAnyOrder("admin", "group_admin");
    }

    @Test
    @DisplayName("Should fan out across multiple requested types")
    void fansOutAcrossTypes() {
        var stub = new StubRelationshipAuthorizationService();
        stub.addRelationships("outlet_user_group",
                new RelationshipAuthorizationService.RelationshipTuple("outlet-1", "viewer", "user-1"));
        stub.addRelationships("course_user_group",
                new RelationshipAuthorizationService.RelationshipTuple("course-1", "modifier", "user-1"));

        var useCase = new LookupUserGroupRelationsUseCaseImpl(stub);
        List<ResourceRelations> result = useCase.lookupUserGroupRelations(
                "user-1", List.of("outlet_user_group", "course_user_group"), null);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(ResourceRelations::resourceType)
                .containsExactlyInAnyOrder("outlet_user_group", "course_user_group");
    }

    @Test
    @DisplayName("Should skip resource types not in the registry")
    void skipsUnknownTypes() {
        var stub = new StubRelationshipAuthorizationService();
        stub.addRelationships("topic_user_group",
                new RelationshipAuthorizationService.RelationshipTuple("topic-1", "admin", "user-1"));

        var useCase = new LookupUserGroupRelationsUseCaseImpl(stub);
        List<ResourceRelations> result = useCase.lookupUserGroupRelations(
                "user-1", List.of("topic_user_group"), null);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return empty when user has no direct relationships")
    void emptyWhenNoRelationships() {
        var stub = new StubRelationshipAuthorizationService();

        var useCase = new LookupUserGroupRelationsUseCaseImpl(stub);
        List<ResourceRelations> result = useCase.lookupUserGroupRelations(
                "ghost-user", List.of("outlet_user_group", "lesson_user_group"), null);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should narrow to a specific resource id when provided")
    void narrowsByResourceId() {
        var stub = new StubRelationshipAuthorizationService();
        stub.addRelationships("outlet_user_group",
                new RelationshipAuthorizationService.RelationshipTuple("outlet-1", "admin", "user-1"),
                new RelationshipAuthorizationService.RelationshipTuple("outlet-2", "viewer", "user-1"));

        var useCase = new LookupUserGroupRelationsUseCaseImpl(stub);
        List<ResourceRelations> result = useCase.lookupUserGroupRelations(
                "user-1", List.of("outlet_user_group"), "outlet-1");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).resourceId()).isEqualTo("outlet-1");
        assertThat(result.get(0).relations()).containsExactly("admin");
        assertThat(stub.lastResourceIdFilter).isEqualTo("outlet-1");
    }

    private static class StubRelationshipAuthorizationService implements RelationshipAuthorizationService {
        private final Map<String, List<RelationshipTuple>> tuples = new HashMap<>();
        String lastResourceIdFilter;

        void addRelationships(String resourceType, RelationshipTuple... ts) {
            tuples.put(resourceType, List.of(ts));
        }

        @Override
        public List<RelationshipTuple> readRelationships(String resourceType,
                                                         String resourceId,
                                                         String relation,
                                                         String subjectType, String subjectId) {
            lastResourceIdFilter = resourceId;
            List<RelationshipTuple> all = tuples.getOrDefault(resourceType, List.of());
            if (resourceId == null) {
                return all;
            }
            return all.stream().filter(t -> resourceId.equals(t.resourceId())).toList();
        }

        @Override
        public List<String> lookupResources(String resourceType, String permission,
                                            String subjectType, String subjectId) {
            return List.of();
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
    }
}
