package com.aura.app.security;

import com.aura.core.clients.RelationshipAuthorizationService;
import java.time.Instant;
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
    @DisplayName("Should pass empty resourceId when targetId is null")
    void hasPermission_nullTargetId_passesEmptyResourceId() {
        Jwt jwt = buildJwt("user-123");
        Authentication auth = new TestingAuthenticationToken(jwt, null);
        when(relationshipService.checkPermission("outlet", "", "create", "user", "user-123"))
                .thenReturn(true);

        boolean result = evaluator.hasPermission(auth, null, "outlet", "create");

        assertThat(result).isTrue();
        verify(relationshipService).checkPermission("outlet", "", "create", "user", "user-123");
    }

    @Test
    @DisplayName("Should pass targetId as resourceId when targetId is non-null")
    void hasPermission_nonNullTargetId_passesTargetIdAsResourceId() {
        Jwt jwt = buildJwt("user-456");
        Authentication auth = new TestingAuthenticationToken(jwt, null);
        when(relationshipService.checkPermission("outlet", "outlet-001", "view", "user", "user-456"))
                .thenReturn(true);

        boolean result = evaluator.hasPermission(auth, "outlet-001", "outlet", "view");

        assertThat(result).isTrue();
        verify(relationshipService).checkPermission("outlet", "outlet-001", "view", "user", "user-456");
    }

    @Test
    @DisplayName("Should return false when SpiceDB denies permission")
    void hasPermission_denied_returnsFalse() {
        Jwt jwt = buildJwt("user-789");
        Authentication auth = new TestingAuthenticationToken(jwt, null);
        when(relationshipService.checkPermission("outlet", "", "create", "user", "user-789"))
                .thenReturn(false);

        boolean result = evaluator.hasPermission(auth, null, "outlet", "create");

        assertThat(result).isFalse();
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
