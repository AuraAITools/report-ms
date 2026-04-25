package com.aura.app.security;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RPT permission evaluator")
class RptPermissionEvaluatorTest {

    private RptPermissionEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new RptPermissionEvaluator();
    }

    @Test
    @DisplayName("Should grant type-level permission when resource and scope match")
    void hasPermission_typeLevelMatch_returnsTrue() {
        Jwt jwt = buildJwt(List.of(
                Map.of("rsname", "outlet", "scopes", List.of("create"))
        ));
        Authentication auth = new TestingAuthenticationToken(jwt, null);
        boolean result = evaluator.hasPermission(auth, null, "outlet", "create");
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should grant instance-level permission when resource ID and scope match")
    void hasPermission_instanceLevelMatch_returnsTrue() {
        Jwt jwt = buildJwt(List.of(
                Map.of("rsname", "outlet:outlet-001", "scopes", List.of("view", "manage"))
        ));
        Authentication auth = new TestingAuthenticationToken(jwt, null);
        boolean result = evaluator.hasPermission(auth, "outlet-001", "outlet", "view");
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should deny when no matching resource found")
    void hasPermission_noMatchingResource_returnsFalse() {
        Jwt jwt = buildJwt(List.of(
                Map.of("rsname", "outlet:outlet-001", "scopes", List.of("view"))
        ));
        Authentication auth = new TestingAuthenticationToken(jwt, null);
        boolean result = evaluator.hasPermission(auth, "outlet-999", "outlet", "view");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should deny when scope does not match")
    void hasPermission_wrongScope_returnsFalse() {
        Jwt jwt = buildJwt(List.of(
                Map.of("rsname", "outlet", "scopes", List.of("view"))
        ));
        Authentication auth = new TestingAuthenticationToken(jwt, null);
        boolean result = evaluator.hasPermission(auth, null, "outlet", "create");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should deny when authorization claim is missing")
    void hasPermission_noAuthorizationClaim_returnsFalse() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("sub", "user-1")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(300))
                .build();
        Authentication auth = new TestingAuthenticationToken(jwt, null);
        boolean result = evaluator.hasPermission(auth, null, "outlet", "create");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should deny when principal is not a JWT")
    void hasPermission_nonJwtPrincipal_returnsFalse() {
        Authentication auth = new TestingAuthenticationToken("not-a-jwt", null);
        boolean result = evaluator.hasPermission(auth, null, "outlet", "create");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should deny when permissions list is empty")
    void hasPermission_emptyPermissions_returnsFalse() {
        Jwt jwt = buildJwt(List.of());
        Authentication auth = new TestingAuthenticationToken(jwt, null);
        boolean result = evaluator.hasPermission(auth, null, "outlet", "create");
        assertThat(result).isFalse();
    }

    private Jwt buildJwt(List<Map<String, Object>> permissions) {
        return Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("sub", "user-1")
                .claim("authorization", Map.of("permissions", permissions))
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(300))
                .build();
    }
}
