package com.aura.core.usecases.authorization;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.clients.RelationshipAuthorizationService.RelationshipTuple;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Lookup user group members use case")
class LookupUserGroupMembersUseCaseImplTest {

    @Test
    @DisplayName("Should aggregate multiple relations per user into one member entry")
    void aggregatesByUser() {
        var stub = new StubRelationshipAuthorizationService();
        stub.addRelationships("outlet_user_group", "outlet-1",
                new RelationshipTuple("outlet-1", "admin", "user-1"),
                new RelationshipTuple("outlet-1", "group_admin", "user-1"),
                new RelationshipTuple("outlet-1", "viewer", "user-2"));

        var useCase = new LookupUserGroupMembersUseCaseImpl(stub);
        List<UserGroupMember> result = useCase.lookupUserGroupMembers(
                "outlet_user_group", "outlet-1", null);

        assertThat(result).hasSize(2);
        UserGroupMember alice = result.stream().filter(m -> m.userId().equals("user-1")).findFirst().orElseThrow();
        assertThat(alice.relations()).containsExactlyInAnyOrder("admin", "group_admin");
        UserGroupMember bob = result.stream().filter(m -> m.userId().equals("user-2")).findFirst().orElseThrow();
        assertThat(bob.relations()).containsExactly("viewer");
    }

    @Test
    @DisplayName("Should pass relation filter through to readRelationships")
    void filtersByRelation() {
        var stub = new StubRelationshipAuthorizationService();
        stub.addRelationships("outlet_user_group", "outlet-1",
                new RelationshipTuple("outlet-1", "admin", "user-1"));

        var useCase = new LookupUserGroupMembersUseCaseImpl(stub);
        useCase.lookupUserGroupMembers("outlet_user_group", "outlet-1", "admin");

        assertThat(stub.lastRelationFilter).isEqualTo("admin");
    }

    @Test
    @DisplayName("Should return empty when resource type is not in the registry")
    void rejectsUnknownTypes() {
        var stub = new StubRelationshipAuthorizationService();
        stub.addRelationships("topic_user_group", "topic-1",
                new RelationshipTuple("topic-1", "admin", "user-1"));

        var useCase = new LookupUserGroupMembersUseCaseImpl(stub);
        List<UserGroupMember> result = useCase.lookupUserGroupMembers(
                "topic_user_group", "topic-1", null);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return empty when no members match")
    void emptyWhenNoMatches() {
        var stub = new StubRelationshipAuthorizationService();

        var useCase = new LookupUserGroupMembersUseCaseImpl(stub);
        List<UserGroupMember> result = useCase.lookupUserGroupMembers(
                "outlet_user_group", "outlet-1", "admin");

        assertThat(result).isEmpty();
    }

    private static class StubRelationshipAuthorizationService implements RelationshipAuthorizationService {
        private final Map<String, List<RelationshipTuple>> tuples = new HashMap<>();
        String lastRelationFilter;

        void addRelationships(String resourceType, String resourceId, RelationshipTuple... ts) {
            tuples.put(resourceType + ":" + resourceId, List.of(ts));
        }

        @Override
        public List<RelationshipTuple> readRelationships(String resourceType,
                                                         String resourceId,
                                                         String relation,
                                                         String subjectType, String subjectId) {
            lastRelationFilter = relation;
            List<RelationshipTuple> all = tuples.getOrDefault(resourceType + ":" + resourceId, List.of());
            if (relation == null) {
                return all;
            }
            return all.stream().filter(t -> relation.equals(t.relation())).toList();
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
