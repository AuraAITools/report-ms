package com.aura.details.services;

import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.events.OutletAdminAssignedEvent;
import com.aura.core.repositories.AccountRepository;
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
    OutletRepository outletRepository;

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
    @DisplayName("Should delegate user group relations lookup to use case + resolver")
    void shouldDelegateUserGroupRelations() {
        var types = List.of("outlet_user_group");
        var raw = List.of(new ResourceRelations("outlet_user_group", "outlet-1", List.of("admin")));
        var resolved = List.of(new UserGroupRelation(
                "outlet_user_group", "outlet-1", "Main Outlet", List.of("admin")));
        when(lookupUserGroupRelationsUseCase.lookupUserGroupRelations("user-1", types, null)).thenReturn(raw);
        when(userGroupResourceResolver.resolveAndFilterRelations(raw, "inst-1")).thenReturn(resolved);

        List<UserGroupRelation> result = authorizationService.lookupUserGroupRelations("user-1", types, null, "inst-1");

        assertThat(result).isEqualTo(resolved);
        verify(lookupUserGroupRelationsUseCase).lookupUserGroupRelations("user-1", types, null);
        verify(userGroupResourceResolver).resolveAndFilterRelations(raw, "inst-1");
    }

    @Test
    @DisplayName("Should pass user_group id filter through to use case")
    void shouldPassResourceIdFilter() {
        var types = List.of("outlet_user_group");
        var raw = List.of(new ResourceRelations("outlet_user_group", "outlet-1", List.of("admin")));
        var resolved = List.of(new UserGroupRelation(
                "outlet_user_group", "outlet-1", "Main", List.of("admin")));
        when(lookupUserGroupRelationsUseCase.lookupUserGroupRelations("user-1", types, "outlet-1")).thenReturn(raw);
        when(userGroupResourceResolver.resolveAndFilterRelations(raw, "inst-1")).thenReturn(resolved);

        List<UserGroupRelation> result = authorizationService.lookupUserGroupRelations(
                "user-1", types, "outlet-1", "inst-1");

        assertThat(result).isEqualTo(resolved);
        verify(lookupUserGroupRelationsUseCase).lookupUserGroupRelations("user-1", types, "outlet-1");
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
