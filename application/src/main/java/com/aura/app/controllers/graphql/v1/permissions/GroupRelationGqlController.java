package com.aura.app.controllers.graphql.v1.permissions;

import com.aura.app.codegen.types.UserGroupRelations;
import com.aura.app.codegen.types.UserGroupType;
import com.aura.core.auth.User;
import com.aura.core.usecases.authorization.UserGroupRelation;
import com.aura.details.services.AuthorizationService;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GroupRelationGqlController {

    private final AuthorizationService authorizationService;

    @QueryMapping
    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    public List<UserGroupRelations> lookupUserGroupRelations(
            @Argument String institutionId,
            @Argument List<UserGroupType> userGroupTypes,
            @Argument String userGroupId,
            @ContextValue(name = "user") User user) {
        List<String> resourceTypes = userGroupTypes.stream()
                .map(t -> t.name().toLowerCase(Locale.ROOT))
                .toList();

        List<UserGroupRelation> relations =
                authorizationService.lookupUserGroupRelations(user.getUserId(), resourceTypes, userGroupId, institutionId);

        return relations.stream()
                .map(r -> UserGroupRelations.newBuilder()
                        .resourceType(r.resourceType())
                        .resourceId(r.resourceId())
                        .displayName(r.displayName())
                        .relations(r.relations())
                        .build())
                .toList();
    }
}
