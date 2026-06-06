package com.aura.app.controllers.graphql.v1.permissions;

import com.aura.app.codegen.types.UserGroupRelations;
import com.aura.app.codegen.types.UserGroupRelationsInInstitution;
import com.aura.core.auth.User;
import com.aura.core.usecases.authorization.UserGroupRelation;
import com.aura.details.services.AuthorizationService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GroupRelationGqlController {

    private static final String TYPE = "UserGroupRelationsInInstitution";

    private final AuthorizationService authorizationService;

    /**
     * Returns a thin parent carrying {@code institutionId}. Each subfield (institution,
     * outlets, courses, lessons, materials, accounts) is resolved by its own
     * @BatchMapping below, so heavy buckets (e.g. lessons in a large institution) are
     * only loaded when the client selects them.
     */
    @QueryMapping
    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    public UserGroupRelationsInInstitution lookupUserGroupRelationsOfUser(@Argument String institutionId) {
        return UserGroupRelationsInInstitution.newBuilder()
                .institutionId(institutionId)
                .build();
    }

    @BatchMapping(typeName = TYPE, field = "institution")
    public Map<UserGroupRelationsInInstitution, UserGroupRelations> institution(
            List<UserGroupRelationsInInstitution> parents,
            @ContextValue("user") User user) {
        Map<UserGroupRelationsInInstitution, UserGroupRelations> result = new HashMap<>();
        for (UserGroupRelationsInInstitution parent : parents) {
            authorizationService.lookupInstitutionGroupRelation(user.getUserId(), parent.getInstitutionId())
                    .map(this::toDto)
                    .ifPresent(dto -> result.put(parent, dto));
        }
        return result;
    }

    @BatchMapping(typeName = TYPE, field = "outlets")
    public Map<UserGroupRelationsInInstitution, List<UserGroupRelations>> outlets(
            List<UserGroupRelationsInInstitution> parents,
            @ContextValue("user") User user) {
        return resolveList(parents, p -> authorizationService.lookupOutletGroupRelations(
                user.getUserId(), p.getInstitutionId()));
    }

    @BatchMapping(typeName = TYPE, field = "courses")
    public Map<UserGroupRelationsInInstitution, List<UserGroupRelations>> courses(
            List<UserGroupRelationsInInstitution> parents,
            @ContextValue("user") User user) {
        return resolveList(parents, p -> authorizationService.lookupCourseGroupRelations(
                user.getUserId(), p.getInstitutionId()));
    }

    @BatchMapping(typeName = TYPE, field = "lessons")
    public Map<UserGroupRelationsInInstitution, List<UserGroupRelations>> lessons(
            List<UserGroupRelationsInInstitution> parents,
            @ContextValue("user") User user) {
        return resolveList(parents, p -> authorizationService.lookupLessonGroupRelations(
                user.getUserId(), p.getInstitutionId()));
    }

    @BatchMapping(typeName = TYPE, field = "materials")
    public Map<UserGroupRelationsInInstitution, List<UserGroupRelations>> materials(
            List<UserGroupRelationsInInstitution> parents,
            @ContextValue("user") User user) {
        return resolveList(parents, p -> authorizationService.lookupMaterialGroupRelations(
                user.getUserId(), p.getInstitutionId()));
    }

    @BatchMapping(typeName = TYPE, field = "accounts")
    public Map<UserGroupRelationsInInstitution, List<UserGroupRelations>> accounts(
            List<UserGroupRelationsInInstitution> parents,
            @ContextValue("user") User user) {
        return resolveList(parents, p -> authorizationService.lookupAccountGroupRelations(
                user.getUserId(), p.getInstitutionId()));
    }

    private Map<UserGroupRelationsInInstitution, List<UserGroupRelations>> resolveList(
            List<UserGroupRelationsInInstitution> parents,
            java.util.function.Function<UserGroupRelationsInInstitution, List<UserGroupRelation>> lookup) {
        Map<UserGroupRelationsInInstitution, List<UserGroupRelations>> result = new HashMap<>();
        for (UserGroupRelationsInInstitution parent : parents) {
            result.put(parent, lookup.apply(parent).stream().map(this::toDto).toList());
        }
        return result;
    }

    private UserGroupRelations toDto(UserGroupRelation r) {
        return UserGroupRelations.newBuilder()
                .resourceType(r.resourceType())
                .resourceId(r.resourceId())
                .displayName(r.displayName())
                .relations(r.relations())
                .build();
    }
}
