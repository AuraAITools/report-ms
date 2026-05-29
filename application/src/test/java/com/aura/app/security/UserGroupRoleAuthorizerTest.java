package com.aura.app.security;

import com.aura.app.codegen.types.GrantUserGroupRoleInput;
import com.aura.app.codegen.types.RevokeUserGroupRoleInput;
import com.aura.app.codegen.types.UserGroupRole;
import com.aura.app.codegen.types.UserGroupType;
import com.aura.core.clients.RelationshipAuthorizationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.List;
import java.util.Map;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserGroupRoleAuthorizer")
class UserGroupRoleAuthorizerTest {

    @Mock
    RelationshipAuthorizationService relationshipService;

    @InjectMocks
    UserGroupRoleAuthorizer authorizer;

    @Test
    @DisplayName("Should grant when SpiceDB has group_add_<role> permission")
    void canGrantRole_returns_true_when_permitted() {
        GrantUserGroupRoleInput input = GrantUserGroupRoleInput.newBuilder()
                .userGroupType(UserGroupType.INSTITUTION_USER_GROUP)
                .userGroupId("inst-1")
                .role(UserGroupRole.ADMIN)
                .userId("target")
                .build();
        Authentication auth = jwtAuth("acting-user");

        when(relationshipService.checkPermission(
                "institution_user_group", "inst-1", "group_add_admin", "user", "acting-user"))
                .thenReturn(true);

        assertThat(authorizer.canGrantRole(input, auth)).isTrue();
    }

    @Test
    @DisplayName("Should deny when SpiceDB rejects the group_add_<role> permission")
    void canGrantRole_returns_false_when_denied() {
        GrantUserGroupRoleInput input = GrantUserGroupRoleInput.newBuilder()
                .userGroupType(UserGroupType.INSTITUTION_USER_GROUP)
                .userGroupId("inst-1")
                .role(UserGroupRole.ADMIN)
                .userId("target")
                .build();
        Authentication auth = jwtAuth("acting-user");

        when(relationshipService.checkPermission(
                "institution_user_group", "inst-1", "group_add_admin", "user", "acting-user"))
                .thenReturn(false);

        assertThat(authorizer.canGrantRole(input, auth)).isFalse();
    }

    @Test
    @DisplayName("Should compose group_remove_<role> for revoke and call SpiceDB with the meta-role permission")
    void canRevokeRole_returns_true_for_group_admin_meta_role() {
        RevokeUserGroupRoleInput input = RevokeUserGroupRoleInput.newBuilder()
                .userGroupType(UserGroupType.SYSTEM_USER_GROUP)
                .userGroupId("aura")
                .role(UserGroupRole.GROUP_ADMIN)
                .userId("target")
                .build();
        Authentication auth = jwtAuth("acting-user");

        when(relationshipService.checkPermission(
                "system_user_group", "aura", "group_remove_group_admin", "user", "acting-user"))
                .thenReturn(true);

        assertThat(authorizer.canRevokeRole(input, auth)).isTrue();
    }

    @Test
    @DisplayName("Should deny and skip the SpiceDB check when the principal is not a JWT")
    void returns_false_for_non_jwt_principal() {
        GrantUserGroupRoleInput input = GrantUserGroupRoleInput.newBuilder()
                .userGroupType(UserGroupType.INSTITUTION_USER_GROUP)
                .userGroupId("inst-1")
                .role(UserGroupRole.ADMIN)
                .userId("target")
                .build();
        Authentication auth = new AnonymousAuthenticationToken(
                "key",
                new User("anon", "", List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))),
                List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));

        assertThat(authorizer.canGrantRole(input, auth)).isFalse();
        verifyNoInteractions(relationshipService);
    }

    private static Authentication jwtAuth(String userId) {
        Jwt jwt = new Jwt(
                "tok",
                Instant.now(),
                Instant.now().plusSeconds(60),
                Map.of("alg", "RS256"),
                Map.of("sub", userId));
        return new JwtAuthenticationToken(jwt);
    }
}
