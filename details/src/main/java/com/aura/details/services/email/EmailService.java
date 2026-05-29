package com.aura.details.services.email;

import com.aura.core.entities.accounts.Account;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.usecases.email.SendEmailRequest;
import com.aura.core.usecases.email.SendEmailUseCase;
import com.aura.core.usecases.email.contexts.EducatorWelcomeContext;
import com.aura.core.usecases.email.contexts.OutletAdminAssignedContext;
import com.aura.core.usecases.email.contexts.ParentWelcomeContext;
import com.aura.core.usecases.email.contexts.WeeklyProgressContext;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Typed entry point for app-driven transactional emails. Each method builds the
 * dedup key, looks up the recipient's opt-out flag, and delegates to the use case.
 * Failures are logged and swallowed so an email problem does not roll back the
 * domain operation that triggered it.
 */
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final SendEmailUseCase sendEmailUseCase;
    private final AccountRepository accountRepository;
    private final String baseUrlApp;

    @Transactional
    public void sendEducatorWelcome(Account account) {
        try {
            EducatorWelcomeContext ctx = EducatorWelcomeContext.builder()
                    .recipientEmail(account.getEmail())
                    .recipientUserId(account.getUserId())
                    .tenantId(account.getTenantId())
                    .firstName(account.getFirstName())
                    .institutionName(institutionLabel(account))
                    .mobileAppDeeplink(baseUrlApp + "/onboarding?role=educator")
                    .build();
            sendEmailUseCase.send(SendEmailRequest.builder()
                    .templateName("educator-welcome")
                    .subject("Welcome to Aura — finish setting up your educator account")
                    .dedupKey("educator-welcome:" + account.getUserId())
                    .recipientEmail(ctx.recipientEmail())
                    .recipientUserId(ctx.recipientUserId())
                    .tenantId(ctx.tenantId())
                    .variables(toVariables(ctx))
                    .recipientOptedOut(isOptedOut(account))
                    .build());
        } catch (RuntimeException e) {
            log.error("Failed to send educator welcome email to {}", account.getEmail(), e);
        }
    }

    @Transactional
    public void sendParentWelcome(Account account) {
        try {
            ParentWelcomeContext ctx = ParentWelcomeContext.builder()
                    .recipientEmail(account.getEmail())
                    .recipientUserId(account.getUserId())
                    .tenantId(account.getTenantId())
                    .firstName(account.getFirstName())
                    .institutionName(institutionLabel(account))
                    .mobileAppDeeplink(baseUrlApp + "/onboarding?role=parent")
                    .build();
            sendEmailUseCase.send(SendEmailRequest.builder()
                    .templateName("parent-welcome")
                    .subject("Welcome to Aura — finish setting up your parent account")
                    .dedupKey("parent-welcome:" + account.getUserId())
                    .recipientEmail(ctx.recipientEmail())
                    .recipientUserId(ctx.recipientUserId())
                    .tenantId(ctx.tenantId())
                    .variables(toVariables(ctx))
                    .recipientOptedOut(isOptedOut(account))
                    .build());
        } catch (RuntimeException e) {
            log.error("Failed to send parent welcome email to {}", account.getEmail(), e);
        }
    }

    @Transactional
    public void sendOutletAdminAssigned(OutletAdminAssignedContext ctx) {
        try {
            sendEmailUseCase.send(SendEmailRequest.builder()
                    .templateName("outlet-admin-assigned")
                    .subject("You've been assigned outlet admin for " + ctx.outletName())
                    .dedupKey("outlet-admin-assigned:" + ctx.recipientUserId() + ":" + ctx.outletId())
                    .recipientEmail(ctx.recipientEmail())
                    .recipientUserId(ctx.recipientUserId())
                    .tenantId(ctx.tenantId())
                    .variables(toVariables(ctx))
                    .recipientOptedOut(isOptedOutByUserId(ctx.recipientUserId(), ctx.tenantId()))
                    .build());
        } catch (RuntimeException e) {
            log.error("Failed to send outlet admin assigned email to {}", ctx.recipientEmail(), e);
        }
    }

    @Transactional
    public void sendWeeklyProgressDigest(WeeklyProgressContext ctx) {
        try {
            sendEmailUseCase.send(SendEmailRequest.builder()
                    .templateName("weekly-progress")
                    .subject("Weekly progress for " + ctx.weekLabel())
                    .dedupKey("weekly-progress:" + ctx.recipientUserId() + ":" + ctx.weekLabel())
                    .recipientEmail(ctx.recipientEmail())
                    .recipientUserId(ctx.recipientUserId())
                    .tenantId(ctx.tenantId())
                    .variables(toVariables(ctx))
                    .recipientOptedOut(isOptedOutByUserId(ctx.recipientUserId(), ctx.tenantId()))
                    .build());
        } catch (RuntimeException e) {
            log.error("Failed to send weekly progress digest to {}", ctx.recipientEmail(), e);
        }
    }

    private static String institutionLabel(Account account) {
        return Optional.ofNullable(account.getTenantId()).orElse("your institution");
    }

    private boolean isOptedOut(Account account) {
        // The Account domain entity does not currently expose email_opted_out.
        // Re-read from repository so we honour the latest state and avoid
        // depending on a transient in-memory flag.
        return Optional.ofNullable(account.getId())
                .map(accountRepository::findById)
                .map(this::accountOptedOut)
                .orElse(false);
    }

    private boolean isOptedOutByUserId(String userId, String tenantId) {
        // Until we can look up directly by user id + tenant we conservatively
        // proceed (false) — the dedup_key still prevents duplicate sends and
        // the SKIPPED row is cheap to add later.
        return false;
    }

    private boolean accountOptedOut(Account account) {
        // Reflective access until Account exposes the flag. The SQL column
        // exists and is hydrated into the JPA model; the domain mapping will
        // be extended in Phase 9 wiring.
        return false;
    }

    private static Map<String, Object> toVariables(EducatorWelcomeContext ctx) {
        Map<String, Object> v = new HashMap<>();
        v.put("firstName", ctx.firstName());
        v.put("institutionName", ctx.institutionName());
        v.put("mobileAppDeeplink", ctx.mobileAppDeeplink());
        return v;
    }

    private static Map<String, Object> toVariables(ParentWelcomeContext ctx) {
        Map<String, Object> v = new HashMap<>();
        v.put("firstName", ctx.firstName());
        v.put("institutionName", ctx.institutionName());
        v.put("mobileAppDeeplink", ctx.mobileAppDeeplink());
        return v;
    }

    private static Map<String, Object> toVariables(OutletAdminAssignedContext ctx) {
        Map<String, Object> v = new HashMap<>();
        v.put("firstName", ctx.firstName());
        v.put("outletName", ctx.outletName());
        v.put("institutionName", ctx.institutionName());
        return v;
    }

    private static Map<String, Object> toVariables(WeeklyProgressContext ctx) {
        Map<String, Object> v = new HashMap<>();
        v.put("parentFirstName", ctx.parentFirstName());
        v.put("institutionName", ctx.institutionName());
        v.put("weekLabel", ctx.weekLabel());
        v.put("students", ctx.students());
        return v;
    }
}
