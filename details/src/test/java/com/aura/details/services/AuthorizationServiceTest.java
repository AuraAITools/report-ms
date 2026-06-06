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
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@DisplayName("Authorization service")
@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @Mock
    private LookupPermissionsUseCase lookupPermissionsUseCase;

    @Mock
    private LookupGroupPermissionsUseCase lookupGroupPermissionsUseCase;

    @Mock
    private LookupUserGroupRelationsUseCase lookupUserGroupRelationsUseCase;

    @Mock
    private LookupUserGroupMembersUseCase lookupUserGroupMembersUseCase;

    @Mock
    GrantUserGroupRoleUseCase grantUserGroupRoleUseCase;

    @Mock
    RevokeUserGroupRoleUseCase revokeUserGroupRoleUseCase;

    @Mock
    UserGroupResourceResolver userGroupResourceResolver;

    @Mock
    InstitutionRepository institutionRepository;

    @Mock
    OutletRepository outletRepository;

    @Mock
    CourseRepository courseRepository;

    @Mock
    LessonRepository lessonRepository;

    @Mock
    MaterialRepository materialRepository;

    @Mock
    AccountRepository accountRepository;

    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private AuthorizationService authorizationService;

    @Test
    @DisplayName("Should delegate to LookupPermissionsUseCase and return result")
    void shouldDelegateToUseCase() {
        var expected = List.of(
                new ResourcePermission("outlet", "outlet-1", List.of("view", "manage"))
        );
        when(lookupPermissionsUseCase.lookupPermissions("user-1")).thenReturn(expected);

        List<ResourcePermission> result = authorizationService.lookupPermissions("user-1");

        assertThat(result).isEqualTo(expected);
        verify(lookupPermissionsUseCase).lookupPermissions("user-1");
    }

    @Test
    @DisplayName("Should delegate group permissions lookup to use case + resolver")
    void shouldDelegateGroupPermissions() {
        var raw = List.of(new ResourcePermission("outlet_user_group", "outlet-1", List.of("view")));
        var resolved = List.of(new GroupResourcePermission(
                "outlet_user_group", "outlet-1", "Main Outlet", List.of("view")));
        when(lookupGroupPermissionsUseCase.lookupGroupPermissions("user-1")).thenReturn(raw);
        when(userGroupResourceResolver.resolveAndFilter(raw, "inst-1")).thenReturn(resolved);

        List<GroupResourcePermission> result = authorizationService.lookupGroupPermissions("user-1", "inst-1");

        assertThat(result).isEqualTo(resolved);
        verify(lookupGroupPermissionsUseCase).lookupGroupPermissions("user-1");
        verify(userGroupResourceResolver).resolveAndFilter(raw, "inst-1");
    }

    @Test
    @DisplayName("Should return institution relation with display name when institution exists and user has relations")
    void resolvesInstitutionRelation() {
        Institution institution = Institution.builder().id("inst-1").name("Acme Institution").build();
        when(institutionRepository.findById("inst-1")).thenReturn(institution);
        when(lookupUserGroupRelationsUseCase.lookupUserGroupRelations(
                org.mockito.ArgumentMatchers.eq("user-1"),
                org.mockito.ArgumentMatchers.eq(Map.of("institution_user_group", Set.of("inst-1")))))
                .thenReturn(List.of(new ResourceRelations("institution_user_group", "inst-1", List.of("admin"))));

        var result = authorizationService.lookupInstitutionGroupRelation("user-1", "inst-1");

        assertThat(result).contains(
                new UserGroupRelation("institution_user_group", "inst-1", "Acme Institution", List.of("admin")));
    }

    @Test
    @DisplayName("Should return empty institution relation when the institution does not exist")
    void emptyInstitutionRelationWhenMissing() {
        when(institutionRepository.findById("inst-1")).thenReturn(null);

        var result = authorizationService.lookupInstitutionGroupRelation("user-1", "inst-1");

        assertThat(result).isEmpty();
        org.mockito.Mockito.verifyNoInteractions(lookupUserGroupRelationsUseCase);
    }

    @Test
    @DisplayName("Should return empty institution relation when user has no SpiceDB relations on the institution")
    void emptyInstitutionRelationWhenNoSpiceDbTuples() {
        Institution institution = Institution.builder().id("inst-1").name("Acme").build();
        when(institutionRepository.findById("inst-1")).thenReturn(institution);
        when(lookupUserGroupRelationsUseCase.lookupUserGroupRelations(
                org.mockito.ArgumentMatchers.eq("user-1"),
                org.mockito.ArgumentMatchers.<Map<String, Set<String>>>any()))
                .thenReturn(List.of());

        var result = authorizationService.lookupInstitutionGroupRelation("user-1", "inst-1");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should resolve outlet relations across all institution outlets with display names")
    void resolvesOutletRelations() {
        Outlet outlet1 = Outlet.builder().id("outlet-1").name("Main").tenantId("inst-1").build();
        Outlet outlet2 = Outlet.builder().id("outlet-2").name("North").tenantId("inst-1").build();
        when(outletRepository.findAllByTenantId("inst-1")).thenReturn(List.of(outlet1, outlet2));
        when(lookupUserGroupRelationsUseCase.lookupUserGroupRelations(
                org.mockito.ArgumentMatchers.eq("user-1"),
                org.mockito.ArgumentMatchers.eq(Map.of("outlet_user_group", Set.of("outlet-1", "outlet-2")))))
                .thenReturn(List.of(
                        new ResourceRelations("outlet_user_group", "outlet-1", List.of("admin")),
                        new ResourceRelations("outlet_user_group", "outlet-2", List.of("viewer"))));

        List<UserGroupRelation> result = authorizationService.lookupOutletGroupRelations("user-1", "inst-1");

        assertThat(result).containsExactlyInAnyOrder(
                new UserGroupRelation("outlet_user_group", "outlet-1", "Main", List.of("admin")),
                new UserGroupRelation("outlet_user_group", "outlet-2", "North", List.of("viewer")));
    }

    @Test
    @DisplayName("Should return empty outlet relations when institution has no outlets")
    void emptyOutletRelationsWhenNoOutlets() {
        when(outletRepository.findAllByTenantId("inst-1")).thenReturn(List.of());

        List<UserGroupRelation> result = authorizationService.lookupOutletGroupRelations("user-1", "inst-1");

        assertThat(result).isEmpty();
        org.mockito.Mockito.verifyNoInteractions(lookupUserGroupRelationsUseCase);
    }

    @Test
    @DisplayName("Should resolve course relations across all institution courses")
    void resolvesCourseRelations() {
        Course course = Course.builder().id("course-1").name("Algebra 101").build();
        when(courseRepository.findAllByTenantId("inst-1")).thenReturn(List.of(course));
        when(lookupUserGroupRelationsUseCase.lookupUserGroupRelations(
                org.mockito.ArgumentMatchers.eq("user-1"),
                org.mockito.ArgumentMatchers.eq(Map.of("course_user_group", Set.of("course-1")))))
                .thenReturn(List.of(new ResourceRelations("course_user_group", "course-1", List.of("modifier"))));

        List<UserGroupRelation> result = authorizationService.lookupCourseGroupRelations("user-1", "inst-1");

        assertThat(result).containsExactly(
                new UserGroupRelation("course_user_group", "course-1", "Algebra 101", List.of("modifier")));
    }

    @Test
    @DisplayName("Should resolve lesson relations across all institution lessons")
    void resolvesLessonRelations() {
        Lesson lesson = Lesson.builder().id("lesson-1").name("Quadratics").build();
        when(lessonRepository.findAllByTenantId("inst-1")).thenReturn(List.of(lesson));
        when(lookupUserGroupRelationsUseCase.lookupUserGroupRelations(
                org.mockito.ArgumentMatchers.eq("user-1"),
                org.mockito.ArgumentMatchers.eq(Map.of("lesson_user_group", Set.of("lesson-1")))))
                .thenReturn(List.of(new ResourceRelations("lesson_user_group", "lesson-1", List.of("viewer"))));

        List<UserGroupRelation> result = authorizationService.lookupLessonGroupRelations("user-1", "inst-1");

        assertThat(result).containsExactly(
                new UserGroupRelation("lesson_user_group", "lesson-1", "Quadratics", List.of("viewer")));
    }

    @Test
    @DisplayName("Should resolve material relations across all institution materials")
    void resolvesMaterialRelations() {
        Material material = Material.builder().id("mat-1").name("Workbook").build();
        when(materialRepository.findAllByTenantId("inst-1")).thenReturn(List.of(material));
        when(lookupUserGroupRelationsUseCase.lookupUserGroupRelations(
                org.mockito.ArgumentMatchers.eq("user-1"),
                org.mockito.ArgumentMatchers.eq(Map.of("material_user_group", Set.of("mat-1")))))
                .thenReturn(List.of(new ResourceRelations("material_user_group", "mat-1", List.of("admin"))));

        List<UserGroupRelation> result = authorizationService.lookupMaterialGroupRelations("user-1", "inst-1");

        assertThat(result).containsExactly(
                new UserGroupRelation("material_user_group", "mat-1", "Workbook", List.of("admin")));
    }

    @Test
    @DisplayName("Should resolve account relations with composed display name")
    void resolvesAccountRelations() {
        Account account = Account.builder().id("acc-1").firstName("Alice").lastName("Doe").build();
        when(accountRepository.findAllByTenantId("inst-1")).thenReturn(List.of(account));
        when(lookupUserGroupRelationsUseCase.lookupUserGroupRelations(
                org.mockito.ArgumentMatchers.eq("user-1"),
                org.mockito.ArgumentMatchers.eq(Map.of("account_user_group", Set.of("acc-1")))))
                .thenReturn(List.of(new ResourceRelations("account_user_group", "acc-1", List.of("admin"))));

        List<UserGroupRelation> result = authorizationService.lookupAccountGroupRelations("user-1", "inst-1");

        assertThat(result).containsExactly(
                new UserGroupRelation("account_user_group", "acc-1", "Alice Doe", List.of("admin")));
    }

    @Test
    @DisplayName("Should drop relations whose resourceId is not in the institution's enumerated entities")
    void dropsRelationsForUnknownResourceIds() {
        Outlet outlet = Outlet.builder().id("outlet-1").name("Main").tenantId("inst-1").build();
        when(outletRepository.findAllByTenantId("inst-1")).thenReturn(List.of(outlet));
        when(lookupUserGroupRelationsUseCase.lookupUserGroupRelations(
                org.mockito.ArgumentMatchers.eq("user-1"),
                org.mockito.ArgumentMatchers.<Map<String, Set<String>>>any()))
                .thenReturn(List.of(
                        new ResourceRelations("outlet_user_group", "outlet-1", List.of("admin")),
                        new ResourceRelations("outlet_user_group", "ghost-outlet", List.of("admin"))));

        List<UserGroupRelation> result = authorizationService.lookupOutletGroupRelations("user-1", "inst-1");

        assertThat(result).containsExactly(
                new UserGroupRelation("outlet_user_group", "outlet-1", "Main", List.of("admin")));
    }

    @Test
    @DisplayName("Should delegate user group members lookup when the user_group belongs to the institution")
    void delegatesMembersLookup() {
        var members = List.of(new UserGroupMember("user-1", List.of("admin")));
        when(userGroupResourceResolver.belongsToInstitution("outlet_user_group", "outlet-1", "inst-1"))
                .thenReturn(true);
        when(lookupUserGroupMembersUseCase.lookupUserGroupMembers("outlet_user_group", "outlet-1", "admin"))
                .thenReturn(members);

        List<UserGroupMember> result = authorizationService.lookupUserGroupMembers(
                "outlet_user_group", "outlet-1", "admin", "inst-1");

        assertThat(result).isEqualTo(members);
    }

    @Test
    @DisplayName("Should return empty members when the user_group does not belong to the institution")
    void returnsEmptyWhenGroupOutsideInstitution() {
        when(userGroupResourceResolver.belongsToInstitution("outlet_user_group", "outlet-99", "inst-1"))
                .thenReturn(false);

        List<UserGroupMember> result = authorizationService.lookupUserGroupMembers(
                "outlet_user_group", "outlet-99", null, "inst-1");

        assertThat(result).isEmpty();
        org.mockito.Mockito.verifyNoInteractions(lookupUserGroupMembersUseCase);
    }

    @Test
    @DisplayName("Should delegate grantUserGroupRole to GrantUserGroupRoleUseCase")
    void delegates_grant() {
        authorizationService.grantUserGroupRole("institution_user_group", "inst-1", "admin", "target");

        verify(grantUserGroupRoleUseCase).grant("institution_user_group", "inst-1", "admin", "target");
        verifyNoMoreInteractions(grantUserGroupRoleUseCase, revokeUserGroupRoleUseCase);
    }

    @Test
    @DisplayName("Should delegate revokeUserGroupRole to RevokeUserGroupRoleUseCase")
    void delegates_revoke() {
        authorizationService.revokeUserGroupRole("outlet_user_group", "outlet-1", "modifier", "target");

        verify(revokeUserGroupRoleUseCase).revoke("outlet_user_group", "outlet-1", "modifier", "target");
        verifyNoMoreInteractions(grantUserGroupRoleUseCase, revokeUserGroupRoleUseCase);
    }

    @Test
    @DisplayName("Should publish OutletAdminAssignedEvent when granting outlet admin")
    void publishesEventForOutletAdmin() {
        Outlet outlet = Outlet.builder().id("outlet-1").name("Main Outlet").tenantId("tenant-1").build();
        Account account = Account.builder().userId("user-1").email("a@b.com").firstName("Alice").build();
        when(outletRepository.findById("outlet-1")).thenReturn(outlet);
        when(accountRepository.findByUserIdAndTenantId("user-1", "tenant-1")).thenReturn(account);

        authorizationService.grantUserGroupRole("outlet", "outlet-1", "admin", "user-1");

        ArgumentCaptor<OutletAdminAssignedEvent> captor = ArgumentCaptor.forClass(OutletAdminAssignedEvent.class);
        verify(applicationEventPublisher).publishEvent(captor.capture());
        OutletAdminAssignedEvent event = captor.getValue();
        assertThat(event.outletId()).isEqualTo("outlet-1");
        assertThat(event.outletName()).isEqualTo("Main Outlet");
        assertThat(event.userId()).isEqualTo("user-1");
        assertThat(event.userEmail()).isEqualTo("a@b.com");
        assertThat(event.userFirstName()).isEqualTo("Alice");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should not publish event when granting non-admin role on outlet")
    void doesNotPublishForNonAdminRole() {
        authorizationService.grantUserGroupRole("outlet", "outlet-1", "viewer", "user-1");

        verify(applicationEventPublisher, never()).publishEvent(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Should not publish event when granting admin on a non-outlet user_group")
    void doesNotPublishForNonOutletType() {
        authorizationService.grantUserGroupRole("institution", "inst-1", "admin", "user-1");

        verify(applicationEventPublisher, never()).publishEvent(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Should skip event when outlet is missing")
    void skipsWhenOutletMissing() {
        when(outletRepository.findById("missing")).thenReturn(null);

        authorizationService.grantUserGroupRole("outlet", "missing", "admin", "user-1");

        verify(applicationEventPublisher, never()).publishEvent(org.mockito.ArgumentMatchers.any());
    }
}
