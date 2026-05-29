package com.aura.details.services.email.listeners;

import com.aura.core.events.EducatorAccountCreatedEvent;
import com.aura.core.events.OutletAdminAssignedEvent;
import com.aura.core.events.ParentAccountCreatedEvent;
import com.aura.core.events.WeeklyProgressDigestEvent;
import com.aura.core.usecases.email.StudentProgressAggregator;
import com.aura.core.usecases.email.contexts.OutletAdminAssignedContext;
import com.aura.core.usecases.email.contexts.WeeklyProgressContext;
import com.aura.details.services.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
public class EmailEventListeners {

    private final EmailService emailService;
    private final StudentProgressAggregator studentProgressAggregator;

    @Async("emailExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onEducatorAccountCreated(EducatorAccountCreatedEvent event) {
        emailService.sendEducatorWelcome(event.account());
    }

    @Async("emailExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onParentAccountCreated(ParentAccountCreatedEvent event) {
        emailService.sendParentWelcome(event.account());
    }

    @Async("emailExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOutletAdminAssigned(OutletAdminAssignedEvent event) {
        emailService.sendOutletAdminAssigned(OutletAdminAssignedContext.builder()
                .recipientEmail(event.userEmail())
                .recipientUserId(event.userId())
                .tenantId(event.tenantId())
                .firstName(event.userFirstName())
                .outletId(event.outletId())
                .outletName(event.outletName())
                .institutionName(event.institutionName())
                .build());
    }

    @Async("emailExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onWeeklyProgressDigest(WeeklyProgressDigestEvent event) {
        WeeklyProgressContext ctx = studentProgressAggregator.aggregateForParent(
                event.parentAccountId(), event.tenantId(), event.weekLabel());
        if (ctx == null) {
            log.info("Skipping weekly digest for parent={} — aggregator returned null", event.parentAccountId());
            return;
        }
        emailService.sendWeeklyProgressDigest(ctx);
    }
}
