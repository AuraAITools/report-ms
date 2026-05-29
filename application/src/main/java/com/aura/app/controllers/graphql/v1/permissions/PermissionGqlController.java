package com.aura.app.controllers.graphql.v1.permissions;

import com.aura.app.codegen.types.GrantUserGroupRoleInput;
import com.aura.app.codegen.types.ResourcePermissions;
import com.aura.app.codegen.types.RevokeUserGroupRoleInput;
import com.aura.app.codegen.types.UserPermissions;
import com.aura.core.auth.User;
import com.aura.core.usecases.authorization.ResourcePermission;
import com.aura.details.services.AuthorizationService;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

/**
 * Controller for permission-related GraphQL operations.
 *
 * <p>Authorization for the grant/revoke mutations is enforced via
 * {@code @PreAuthorize} that delegates to {@link com.aura.app.security.UserGroupRoleAuthorizer}.
 * The authorizer computes the dynamic SpiceDB permission name
 * ({@code group_add_<role>} / {@code group_remove_<role>}) and resource type
 * ({@code <resource>_user_group}) at request time, then performs a SpiceDB
 * CheckPermission. A denial throws Spring's {@code AccessDeniedException}
 * before the controller body runs.
 *
 * <p>The {@code lookupPermissions} query returns the caller's own permissions —
 * no per-resource gate is appropriate. This controller is therefore exempted
 * from {@code PreAuthorizeConventionTest}'s rules (the convention test enforces
 * literal-string {@code hasPermission(...)}; the bean-method SpEL form used here
 * is intentionally different).
 *
 * <p>Caution: any new {@code @MutationMapping} or {@code @QueryMapping} added to
 * this controller is exempted from the convention test. If a new method's
 * authorization can be expressed statically as
 * {@code hasPermission(targetId, 'resource', 'scope')}, prefer to put it in a
 * dedicated controller so the convention test catches mistakes.
 */
@Controller
@RequiredArgsConstructor
public class PermissionGqlController {

    private final AuthorizationService authorizationService;

    @QueryMapping
    public UserPermissions lookupPermissions(@ContextValue(name = "user") User user) {
        List<ResourcePermission> permissions = authorizationService.lookupPermissions(user.getUserId());

        List<ResourcePermissions> resourcePermissions = permissions.stream()
                .map(rp -> ResourcePermissions.newBuilder()
                        .resourceType(rp.resourceType())
                        .resourceId(rp.resourceId())
                        .permissions(rp.permissions())
                        .build())
                .toList();

        return UserPermissions.newBuilder()
                .permissions(resourcePermissions)
                .build();
    }

    @MutationMapping
    @PreAuthorize("@userGroupRoleAuthorizer.canGrantRole(#input, authentication)")
    public Boolean grantUserGroupRole(@Argument GrantUserGroupRoleInput input) {
        authorizationService.grantUserGroupRole(
                input.getUserGroupType().name().toLowerCase(Locale.ROOT),
                input.getUserGroupId(),
                input.getRole().name().toLowerCase(Locale.ROOT),
                input.getUserId());
        return true;
    }

    @MutationMapping
    @PreAuthorize("@userGroupRoleAuthorizer.canRevokeRole(#input, authentication)")
    public Boolean revokeUserGroupRole(@Argument RevokeUserGroupRoleInput input) {
        authorizationService.revokeUserGroupRole(
                input.getUserGroupType().name().toLowerCase(Locale.ROOT),
                input.getUserGroupId(),
                input.getRole().name().toLowerCase(Locale.ROOT),
                input.getUserId());
        return true;
    }
}
