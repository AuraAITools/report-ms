package com.aura.details.services;

import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.courses.Course;
import com.aura.core.entities.institutions.Institution;
import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.materials.Material;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.events.OutletAdminAssignedEvent;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.MaterialRepository;
import com.aura.core.repositories.OutletRepository;
import com.aura.core.usecases.authorization.GrantUserGroupRoleUseCase;
import com.aura.core.usecases.authorization.GroupResourcePermission;
import com.aura.core.usecases.authorization.LookupGroupPermissionsUseCase;
import com.aura.core.usecases.authorization.LookupPermissionsUseCase;
import com.aura.core.usecases.authorization.LookupUserGroupMembersUseCase;
import com.aura.core.usecases.authorization.LookupUserGroupRelationsUseCase;
import com.aura.core.usecases.authorization.ResourcePermission;
import com.aura.core.usecases.authorization.ResourceRelations;
import com.aura.core.usecases.authorization.RevokeUserGroupRoleUseCase;
import com.aura.core.usecases.authorization.UserGroupMember;
import com.aura.core.usecases.authorization.UserGroupRelation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class AuthorizationService {

    private static final String OUTLET_USER_GROUP = "outlet";
    private static final Set<String> OUTLET_ADMIN_ROLES = Set.of("admin", "group_admin");

    private static final String INSTITUTION_USER_GROUP_TYPE = "institution_user_group";
    private static final String OUTLET_USER_GROUP_TYPE = "outlet_user_group";
    private static final String COURSE_USER_GROUP_TYPE = "course_user_group";
    private static final String LESSON_USER_GROUP_TYPE = "lesson_user_group";
    private static final String MATERIAL_USER_GROUP_TYPE = "material_user_group";
    private static final String ACCOUNT_USER_GROUP_TYPE = "account_user_group";

    private final LookupPermissionsUseCase lookupPermissionsUseCase;
    private final LookupGroupPermissionsUseCase lookupGroupPermissionsUseCase;
    private final LookupUserGroupRelationsUseCase lookupUserGroupRelationsUseCase;
    private final LookupUserGroupMembersUseCase lookupUserGroupMembersUseCase;
    private final GrantUserGroupRoleUseCase grantUserGroupRoleUseCase;
    private final RevokeUserGroupRoleUseCase revokeUserGroupRoleUseCase;
    private final UserGroupResourceResolver userGroupResourceResolver;
    private final InstitutionRepository institutionRepository;
    private final OutletRepository outletRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final MaterialRepository materialRepository;
    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public List<ResourcePermission> lookupPermissions(String userId) {
        return lookupPermissionsUseCase.lookupPermissions(userId);
    }

    public List<GroupResourcePermission> lookupGroupPermissions(String userId, String institutionId) {
        List<ResourcePermission> raw = lookupGroupPermissionsUseCase.lookupGroupPermissions(userId);
        return userGroupResourceResolver.resolveAndFilter(raw, institutionId);
    }

    /**
     * Returns the caller's relations on the institution_user_group, or empty if the institution
     * does not exist or the caller has no relations on it.
     */
    public Optional<UserGroupRelation> lookupInstitutionGroupRelation(String userId, String institutionId) {
        Institution institution = institutionRepository.findById(institutionId);
        if (institution == null) {
            return Optional.empty();
        }
        List<ResourceRelations> raw = lookupUserGroupRelationsUseCase.lookupUserGroupRelations(
                userId, Map.of(INSTITUTION_USER_GROUP_TYPE, Set.of(institutionId)));
        return raw.stream().findFirst()
                .map(rr -> new UserGroupRelation(rr.resourceType(), rr.resourceId(),
                        institution.getName(), rr.relations()));
    }

    public List<UserGroupRelation> lookupOutletGroupRelations(String userId, String institutionId) {
        return resolveRelationsForType(
                userId, OUTLET_USER_GROUP_TYPE,
                outletRepository.findAllByTenantId(institutionId),
                Outlet::getId, Outlet::getName);
    }

    public List<UserGroupRelation> lookupCourseGroupRelations(String userId, String institutionId) {
        return resolveRelationsForType(
                userId, COURSE_USER_GROUP_TYPE,
                courseRepository.findAllByTenantId(institutionId),
                Course::getId, Course::getName);
    }

    public List<UserGroupRelation> lookupLessonGroupRelations(String userId, String institutionId) {
        return resolveRelationsForType(
                userId, LESSON_USER_GROUP_TYPE,
                lessonRepository.findAllByTenantId(institutionId),
                Lesson::getId, Lesson::getName);
    }

    public List<UserGroupRelation> lookupMaterialGroupRelations(String userId, String institutionId) {
        return resolveRelationsForType(
                userId, MATERIAL_USER_GROUP_TYPE,
                materialRepository.findAllByTenantId(institutionId),
                Material::getId, Material::getName);
    }

    public List<UserGroupRelation> lookupAccountGroupRelations(String userId, String institutionId) {
        return resolveRelationsForType(
                userId, ACCOUNT_USER_GROUP_TYPE,
                accountRepository.findAllByTenantId(institutionId),
                Account::getId, this::accountDisplayName);
    }

    public List<UserGroupMember> lookupUserGroupMembers(String resourceType,
                                                        String resourceId,
                                                        @Nullable String relation,
                                                        String institutionId) {
        if (!userGroupResourceResolver.belongsToInstitution(resourceType, resourceId, institutionId)) {
            return List.of();
        }
        return lookupUserGroupMembersUseCase.lookupUserGroupMembers(resourceType, resourceId, relation);
    }

    @Transactional
    public void grantUserGroupRole(String userGroupType, String userGroupId, String role, String targetUserId) {
        grantUserGroupRoleUseCase.grant(userGroupType, userGroupId, role, targetUserId);
        if (OUTLET_USER_GROUP.equals(userGroupType) && OUTLET_ADMIN_ROLES.contains(role)) {
            publishOutletAdminAssigned(userGroupId, targetUserId);
        }
    }

    @Transactional
    public void revokeUserGroupRole(String userGroupType, String userGroupId, String role, String targetUserId) {
        revokeUserGroupRoleUseCase.revoke(userGroupType, userGroupId, role, targetUserId);
    }

    private <T> List<UserGroupRelation> resolveRelationsForType(String userId,
                                                                String resourceType,
                                                                List<T> entities,
                                                                Function<T, String> idFn,
                                                                Function<T, String> nameFn) {
        if (entities.isEmpty()) {
            return List.of();
        }
        Set<String> ids = new HashSet<>(entities.size());
        Map<String, String> displayNames = new HashMap<>(entities.size());
        for (T entity : entities) {
            String id = idFn.apply(entity);
            ids.add(id);
            displayNames.put(id, nameFn.apply(entity));
        }
        List<ResourceRelations> raw = lookupUserGroupRelationsUseCase.lookupUserGroupRelations(
                userId, Map.of(resourceType, ids));
        return raw.stream()
                .map(rr -> {
                    String displayName = displayNames.get(rr.resourceId());
                    if (displayName == null) {
                        log.warn("No display name resolved for {}:{} — skipping", rr.resourceType(), rr.resourceId());
                        return null;
                    }
                    return new UserGroupRelation(rr.resourceType(), rr.resourceId(), displayName, rr.relations());
                })
                .filter(java.util.Objects::nonNull)
                .toList();
    }

    private String accountDisplayName(Account account) {
        String first = Optional.ofNullable(account.getFirstName()).orElse("");
        String last = Optional.ofNullable(account.getLastName()).orElse("");
        String combined = (first + " " + last).trim();
        return combined.isEmpty() ? account.getId() : combined;
    }

    private void publishOutletAdminAssigned(String outletId, String targetUserId) {
        try {
            Outlet outlet = outletRepository.findById(outletId);
            if (outlet == null) {
                log.warn("Skipping OutletAdminAssignedEvent — outlet {} not found", outletId);
                return;
            }
            Account account = accountRepository.findByUserIdAndTenantId(targetUserId, outlet.getTenantId());
            if (account == null) {
                log.warn("Skipping OutletAdminAssignedEvent — no account for userId={} in tenant={}",
                        targetUserId, outlet.getTenantId());
                return;
            }
            applicationEventPublisher.publishEvent(OutletAdminAssignedEvent.builder()
                    .outletId(outlet.getId())
                    .outletName(outlet.getName())
                    .institutionName(outlet.getTenantId())
                    .userId(account.getUserId())
                    .userEmail(account.getEmail())
                    .userFirstName(account.getFirstName())
                    .tenantId(outlet.getTenantId())
                    .build());
        } catch (RuntimeException e) {
            log.error("Failed to publish OutletAdminAssignedEvent for outlet={} userId={}",
                    outletId, targetUserId, e);
        }
    }
}
