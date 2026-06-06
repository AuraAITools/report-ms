package com.aura.app.controllers.graphql.v1.permissions;

import com.aura.app.codegen.types.UserGroupMember;
import com.aura.app.codegen.types.UserGroupType;
import com.aura.details.services.AuthorizationService;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GroupMemberGqlController {

    private final AuthorizationService authorizationService;

    @QueryMapping
    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    public List<UserGroupMember> lookupUserGroupMembers(
            @Argument String institutionId,
            @Argument UserGroupType userGroupType,
            @Argument String userGroupId,
            @Argument @Nullable String relation) {
        String resourceType = userGroupType.name().toLowerCase(Locale.ROOT);

        List<com.aura.core.usecases.authorization.UserGroupMember> members =
                authorizationService.lookupUserGroupMembers(resourceType, userGroupId, relation, institutionId);

        return members.stream()
                .map(m -> UserGroupMember.newBuilder()
                        .userId(m.userId())
                        .relations(m.relations())
                        .build())
                .toList();
    }
}
