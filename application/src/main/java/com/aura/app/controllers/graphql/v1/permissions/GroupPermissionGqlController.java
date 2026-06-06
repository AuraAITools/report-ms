package com.aura.app.controllers.graphql.v1.permissions;

import com.aura.app.codegen.types.GroupResourcePermissions;
import com.aura.core.auth.User;
import com.aura.core.usecases.authorization.GroupResourcePermission;
import com.aura.details.services.AuthorizationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GroupPermissionGqlController {

    private final AuthorizationService authorizationService;

    @QueryMapping
    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    public List<GroupResourcePermissions> lookupGroupPermissions(
            @Argument String institutionId,
            @ContextValue(name = "user") User user) {
        List<GroupResourcePermission> permissions =
                authorizationService.lookupGroupPermissions(user.getUserId(), institutionId);

        return permissions.stream()
                .map(p -> GroupResourcePermissions.newBuilder()
                        .resourceType(p.resourceType())
                        .resourceId(p.resourceId())
                        .displayName(p.displayName())
                        .permissions(p.permissions())
                        .build())
                .toList();
    }
}
