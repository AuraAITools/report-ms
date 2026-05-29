package com.aura.app.security;

import com.aura.app.codegen.types.GrantUserGroupRoleInput;
import com.aura.app.codegen.types.RevokeUserGroupRoleInput;
import com.aura.app.codegen.types.UserGroupRole;
import com.aura.app.codegen.types.UserGroupType;
import com.aura.core.clients.RelationshipAuthorizationService;
import java.util.Locale;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * SpEL-callable authorizer used by {@code @PreAuthorize} on the user-group role
 * grant/revoke mutations. Computes the SpiceDB permission name dynamically from
 * the input ({@code group_add_<role>} or {@code group_remove_<role>}) and checks
 * it on the corresponding {@code <resource>_user_group} resource.
 *
 * <p>Spring Security calls these methods via SpEL like:
 * {@code @userGroupRoleAuthorizer.canGrantRole(#input, authentication)}.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserGroupRoleAuthorizer {

    private static final String USER_SUBJECT_TYPE = "user";

    private final RelationshipAuthorizationService relationshipService;

    public boolean canGrantRole(GrantUserGroupRoleInput input, Authentication authentication) {
        return check(input.getUserGroupType(), input.getUserGroupId(),
                permissionName("group_add_", input.getRole()), authentication);
    }

    public boolean canRevokeRole(RevokeUserGroupRoleInput input, Authentication authentication) {
        return check(input.getUserGroupType(), input.getUserGroupId(),
                permissionName("group_remove_", input.getRole()), authentication);
    }

    private boolean check(UserGroupType userGroupType, String userGroupId,
                          String permission, Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof Jwt jwt)) {
            log.warn("UserGroupRoleAuthorizer rejected non-JWT principal: {}",
                    Objects.requireNonNull(authentication.getPrincipal()).getClass().getName());
            return false;
        }
        String userId = jwt.getSubject();
        String resourceType = userGroupType.name().toLowerCase(Locale.ROOT);
        boolean granted = relationshipService.checkPermission(
                resourceType, userGroupId, permission, USER_SUBJECT_TYPE, userId);
        log.info("UserGroupRoleAuthorizer check: {}:{}#{}@user:{} = {}",
                resourceType, userGroupId, permission, userId, granted);
        return granted;
    }

    private static String permissionName(String prefix, UserGroupRole role) {
        return prefix + role.name().toLowerCase(Locale.ROOT);
    }
}