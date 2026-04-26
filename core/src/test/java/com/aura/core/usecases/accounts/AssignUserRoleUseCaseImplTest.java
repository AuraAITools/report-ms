package com.aura.core.usecases.accounts;

import com.aura.core.clients.IdentityService;
import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.clients.UserIdentity;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Assign user role use case")
class AssignUserRoleUseCaseImplTest {

    @Test
    @DisplayName("Should write relationship when user is found by email")
    void shouldWriteRelationshipWhenUserFound() {
        var stubIdentityService = new StubIdentityService(
                List.of(UserIdentity.builder().id("user-123").email("test@example.com").build())
        );
        var stubRelationshipService = new StubRelationshipAuthorizationService();
        var useCase = new AssignUserRoleUseCaseImpl(stubIdentityService, stubRelationshipService);

        useCase.assignRole("test@example.com", "institution", "inst-456", "admin");

        assertEquals(1, stubRelationshipService.writtenRelationships.size());
        var written = stubRelationshipService.writtenRelationships.getFirst();
        assertEquals("institution", written.resourceType());
        assertEquals("inst-456", written.resourceId());
        assertEquals("admin", written.relation());
        assertEquals("user", written.subjectType());
        assertEquals("user-123", written.subjectId());
    }

    @Test
    @DisplayName("Should throw IllegalStateException when user is not found by email")
    void shouldThrowWhenUserNotFound() {
        var stubIdentityService = new StubIdentityService(List.of());
        var stubRelationshipService = new StubRelationshipAuthorizationService();
        var useCase = new AssignUserRoleUseCaseImpl(stubIdentityService, stubRelationshipService);

        var exception = assertThrows(IllegalStateException.class,
                () -> useCase.assignRole("missing@example.com", "institution", "inst-456", "admin"));

        assertTrue(exception.getMessage().contains("missing@example.com"));
        assertTrue(stubRelationshipService.writtenRelationships.isEmpty());
    }

    @Test
    @DisplayName("Should use the first user when multiple users match email")
    void shouldUseFirstUserWhenMultipleMatch() {
        var stubIdentityService = new StubIdentityService(List.of(
                UserIdentity.builder().id("first-user").email("dup@example.com").build(),
                UserIdentity.builder().id("second-user").email("dup@example.com").build()
        ));
        var stubRelationshipService = new StubRelationshipAuthorizationService();
        var useCase = new AssignUserRoleUseCaseImpl(stubIdentityService, stubRelationshipService);

        useCase.assignRole("dup@example.com", "outlet", "outlet-789", "viewer");

        assertEquals(1, stubRelationshipService.writtenRelationships.size());
        assertEquals("first-user", stubRelationshipService.writtenRelationships.getFirst().subjectId());
    }

    // --- Stubs ---

    private static class StubIdentityService implements IdentityService {
        private final List<UserIdentity> usersToReturn;

        StubIdentityService(List<UserIdentity> usersToReturn) {
            this.usersToReturn = usersToReturn;
        }

        @Override
        public List<UserIdentity> findByEmail(String email) {
            return usersToReturn;
        }

        @Override
        public UserIdentity createUserIdentityAndTagToTenant(UserIdentity userIdentity, String tenantId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void tagTenantToUser(String userId, String tenantId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void assignRolesToUser(String userId, String... roles) {
            throw new UnsupportedOperationException();
        }
    }

    private static class StubRelationshipAuthorizationService implements RelationshipAuthorizationService {
        final List<Relationship> writtenRelationships = new ArrayList<>();

        @Override
        public void writeRelationship(String resourceType, String resourceId,
                                      String relation, String subjectType, String subjectId) {
            writtenRelationships.add(new Relationship(resourceType, resourceId, relation, subjectType, subjectId));
        }

        @Override
        public void deleteRelationship(String resourceType, String resourceId,
                                       String relation, String subjectType, String subjectId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllRelationships(String resourceType) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void writeRelationships(List<Relationship> relationships) {
            writtenRelationships.addAll(relationships);
        }

        @Override
        public boolean checkPermission(String resourceType, String resourceId,
                                       String permission, String subjectType, String subjectId) {
            throw new UnsupportedOperationException();
        }
    }
}
