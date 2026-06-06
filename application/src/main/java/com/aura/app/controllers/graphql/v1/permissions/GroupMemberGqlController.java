package com.aura.app.controllers.graphql.v1.permissions;

import com.aura.app.codegen.types.AccountResponse;
import com.aura.app.codegen.types.UserGroupMember;
import com.aura.app.codegen.types.UserGroupType;
import com.aura.app.mappers.AccountMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.accounts.Account;
import com.aura.core.repositories.AccountRepository;
import com.aura.details.services.AuthorizationService;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GroupMemberGqlController {

    private final AuthorizationService authorizationService;
    private final AccountRepository accountRepository;

    /**
     * Returns a list of users in the user group of a specified relation(role). if relation is omitted,
     * user is returned with all roles it has in the user group
     * @param institutionId
     * @param userGroupType
     * @param userGroupId
     * @param relation
     * @return
     */
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

    @BatchMapping(typeName = "UserGroupMember", field = "account")
    public Map<UserGroupMember, AccountResponse> account(List<UserGroupMember> members,
                                                         @ContextValue("user") User user) {
        List<String> userIds = members.stream().map(UserGroupMember::getUserId).toList();
        Map<String, Account> accountsByUserId =
                accountRepository.findAllByUserIdsAndTenantId(userIds, user.getCurrentTenantAccessed());

        return members.stream()
                .filter(m -> accountsByUserId.containsKey(m.getUserId()))
                .collect(Collectors.toMap(
                        Function.identity(),
                        m -> AccountMapper.INSTANCE.toDto(accountsByUserId.get(m.getUserId()))));
    }
}
