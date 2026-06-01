package com.aura.app.security;

import com.aura.core.clients.RelationshipAuthorizationService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("SpiceDB permission evaluator")
@ExtendWith(MockitoExtension.class)
class SpiceDbPermissionEvaluatorTest {

    @Mock
    private RelationshipAuthorizationService relationshipService;

    private SpiceDbPermissionEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new SpiceDbPermissionEvaluator(relationshipService);
    }

    @Test
    @DisplayName("Should return false for unsupported hasPermission(Object, Object) overload")
    void hasPermission_objectOverload_returnsFalse() {
        Authentication auth = new TestingAuthenticationToken("user", null);
        boolean result = evaluator.hasPermission(auth, new Object(), "create");
        assertThat(result).isFalse();
        verifyNoInteractions(relationshipService);
    }

    @Test
    @DisplayName("Should return false when principal is not a JWT")
    void hasPermission_nonJwtPrincipal_returnsFalse() {
        Authentication auth = new TestingAuthenticationToken("not-a-jwt", null);
        boolean result = evaluator.hasPermission(auth, null, "outlet", "create");
        assertThat(result).isFalse();
        verifyNoInteractions(relationshipService);
    }

    @Test
    @DisplayName("Should check create_institution against system:aura when explicitly targeted")
    void hasPermission_systemAuraTarget_passesThrough() {
        Jwt jwt = buildJwt("user-123");
        Authentication auth = new TestingAuthenticationToken(jwt, null);
        when(relationshipService.checkPermission("system", "aura", "create_institution", "user", "user-123"))
                .thenReturn(true);

        boolean result = evaluator.hasPermission(auth, "aura", "system", "create_institution");

        assertThat(result).isTrue();
        verify(relationshipService).checkPermission("system", "aura", "create_institution", "user", "user-123");
    }

    @Test
    @DisplayName("Should pass targetId as resourceId when targetId is non-null")
    void hasPermission_nonNullTargetId_passesTargetIdAsResourceId() {
        Jwt jwt = buildJwt("user-456");
        Authentication auth = new TestingAuthenticationToken(jwt, null);
        when(relationshipService.checkPermission("institution", "inst-001", "create_outlet", "user", "user-456"))
                .thenReturn(true);

        boolean result = evaluator.hasPermission(auth, "inst-001", "institution", "create_outlet");

        assertThat(result).isTrue();
        verify(relationshipService).checkPermission("institution", "inst-001", "create_outlet", "user", "user-456");
    }

    @Test
    @DisplayName("Should return false when SpiceDB denies permission")
    void hasPermission_denied_returnsFalse() {
        Jwt jwt = buildJwt("user-789");
        Authentication auth = new TestingAuthenticationToken(jwt, null);
        when(relationshipService.checkPermission("institution", "inst-001", "create_outlet", "user", "user-789"))
                .thenReturn(false);

        boolean result = evaluator.hasPermission(auth, "inst-001", "institution", "create_outlet");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should grant collection check when user has permission on every id")
    void hasPermission_collectionAllGranted_returnsTrue() {
        Jwt jwt = buildJwt("user-100");
        Authentication auth = new TestingAuthenticationToken(jwt, null);
        ArrayList<String> ids = new ArrayList<>(List.of("inst-1", "inst-2"));
        when(relationshipService.checkPermission("institution", "inst-1", "view", "user", "user-100"))
                .thenReturn(true);
        when(relationshipService.checkPermission("institution", "inst-2", "view", "user", "user-100"))
                .thenReturn(true);

        boolean result = evaluator.hasPermission(auth, ids, "institution", "view");

        assertThat(result).isTrue();
        verify(relationshipService).checkPermission("institution", "inst-1", "view", "user", "user-100");
        verify(relationshipService).checkPermission("institution", "inst-2", "view", "user", "user-100");
    }

    @Test
    @DisplayName("Should deny collection check and short-circuit when any id is denied")
    void hasPermission_collectionOneDenied_returnsFalseAndShortCircuits() {
        Jwt jwt = buildJwt("user-200");
        Authentication auth = new TestingAuthenticationToken(jwt, null);
        ArrayList<String> ids = new ArrayList<>(List.of("inst-1", "inst-2", "inst-3"));
        when(relationshipService.checkPermission("institution", "inst-1", "view", "user", "user-200"))
                .thenReturn(true);
        when(relationshipService.checkPermission("institution", "inst-2", "view", "user", "user-200"))
                .thenReturn(false);

        boolean result = evaluator.hasPermission(auth, ids, "institution", "view");

        assertThat(result).isFalse();
        verify(relationshipService).checkPermission("institution", "inst-1", "view", "user", "user-200");
        verify(relationshipService).checkPermission("institution", "inst-2", "view", "user", "user-200");
        verify(relationshipService, never()).checkPermission("institution", "inst-3", "view", "user", "user-200");
    }

    @Test
    @DisplayName("Should grant empty collection without invoking SpiceDB")
    void hasPermission_emptyCollection_returnsTrueWithoutCheck() {
        Jwt jwt = buildJwt("user-300");
        Authentication auth = new TestingAuthenticationToken(jwt, null);
        ArrayList<String> ids = new ArrayList<>();

        boolean result = evaluator.hasPermission(auth, ids, "institution", "view");

        assertThat(result).isTrue();
        verifyNoInteractions(relationshipService);
    }

    private Jwt buildJwt(String subject) {
        return Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("sub", subject)
                .subject(subject)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(300))
                .build();
    }
}
