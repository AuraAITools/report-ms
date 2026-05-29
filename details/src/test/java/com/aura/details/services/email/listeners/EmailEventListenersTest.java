package com.aura.details.services.email.listeners;

import com.aura.core.entities.accounts.Account;
import com.aura.core.events.EducatorAccountCreatedEvent;
import com.aura.core.events.OutletAdminAssignedEvent;
import com.aura.core.events.ParentAccountCreatedEvent;
import com.aura.core.events.WeeklyProgressDigestEvent;
import com.aura.core.usecases.email.StudentProgressAggregator;
import com.aura.core.usecases.email.contexts.OutletAdminAssignedContext;
import com.aura.core.usecases.email.contexts.WeeklyProgressContext;
import com.aura.details.services.email.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Email event listeners")
@ExtendWith(MockitoExtension.class)
class EmailEventListenersTest {

    @Mock
    private EmailService emailService;
    @Mock
    private StudentProgressAggregator aggregator;
    @InjectMocks
    private EmailEventListeners listeners;

    @DisplayName("Should send educator welcome on EducatorAccountCreatedEvent")
    @Test
    void onEducatorAccountCreated() {
        Account a = Account.builder().userId("u").email("e@a.com").build();
        listeners.onEducatorAccountCreated(new EducatorAccountCreatedEvent(a));
        verify(emailService).sendEducatorWelcome(a);
    }

    @DisplayName("Should send parent welcome on ParentAccountCreatedEvent")
    @Test
    void onParentAccountCreated() {
        Account a = Account.builder().userId("u").email("e@a.com").build();
        listeners.onParentAccountCreated(new ParentAccountCreatedEvent(a));
        verify(emailService).sendParentWelcome(a);
    }

    @DisplayName("Should map fields and dispatch on OutletAdminAssignedEvent")
    @Test
    void onOutletAdminAssigned() {
        OutletAdminAssignedEvent e = OutletAdminAssignedEvent.builder()
                .outletId("o-1").outletName("Main Outlet").institutionName("Aura Academy")
                .userId("u-1").userEmail("admin@aura.com").userFirstName("Sam")
                .tenantId("t-1").build();

        listeners.onOutletAdminAssigned(e);

        ArgumentCaptor<OutletAdminAssignedContext> ctx = ArgumentCaptor.forClass(OutletAdminAssignedContext.class);
        verify(emailService).sendOutletAdminAssigned(ctx.capture());
        assertThat(ctx.getValue().outletId()).isEqualTo("o-1");
        assertThat(ctx.getValue().outletName()).isEqualTo("Main Outlet");
        assertThat(ctx.getValue().recipientEmail()).isEqualTo("admin@aura.com");
        assertThat(ctx.getValue().recipientUserId()).isEqualTo("u-1");
        assertThat(ctx.getValue().firstName()).isEqualTo("Sam");
    }

    @DisplayName("Should aggregate and send on WeeklyProgressDigestEvent")
    @Test
    void onWeeklyProgressDigest() {
        WeeklyProgressContext built = WeeklyProgressContext.builder()
                .recipientEmail("p@a.com").recipientUserId("u").tenantId("t").parentFirstName("P")
                .institutionName("I").weekLabel("W").students(java.util.List.of()).build();
        WeeklyProgressDigestEvent e = new WeeklyProgressDigestEvent("acc-1", "tenant-1", "2026-W19");
        when(aggregator.aggregateForParent("acc-1", "tenant-1", "2026-W19")).thenReturn(built);

        listeners.onWeeklyProgressDigest(e);

        verify(emailService).sendWeeklyProgressDigest(built);
    }

    @DisplayName("Should skip send when aggregator returns null")
    @Test
    void onWeeklyProgressDigestSkipsWhenNull() {
        WeeklyProgressDigestEvent e = new WeeklyProgressDigestEvent("acc-1", "tenant-1", "2026-W19");
        when(aggregator.aggregateForParent("acc-1", "tenant-1", "2026-W19")).thenReturn(null);

        listeners.onWeeklyProgressDigest(e);

        verify(emailService, never()).sendWeeklyProgressDigest(org.mockito.ArgumentMatchers.any());
    }
}
