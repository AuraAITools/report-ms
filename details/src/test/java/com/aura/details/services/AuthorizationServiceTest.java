package com.aura.details.services;

import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.events.OutletAdminAssignedEvent;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.OutletRepository;
import com.aura.core.usecases.authorization.GrantUserGroupRoleUseCase;
import com.aura.core.usecases.authorization.LookupPermissionsUseCase;
import com.aura.core.usecases.authorization.ResourcePermission;
import com.aura.core.usecases.authorization.RevokeUserGroupRoleUseCase;
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
    GrantUserGroupRoleUseCase grantUserGroupRoleUseCase;

    @Mock
    RevokeUserGroupRoleUseCase revokeUserGroupRoleUseCase;

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
