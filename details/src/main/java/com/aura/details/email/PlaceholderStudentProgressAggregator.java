package com.aura.details.email;

import com.aura.core.repositories.AccountRepository;
import com.aura.core.entities.accounts.Account;
import com.aura.core.usecases.email.StudentProgressAggregator;
import com.aura.core.usecases.email.contexts.WeeklyProgressContext;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

/**
 * Placeholder implementation while the real aggregation logic is designed.
 * Returns hardcoded student summaries so the cron + email pipeline can be
 * exercised end to end. Replace with the real implementation as part of the
 * follow-up "weekly progress aggregation" spec.
 */
@RequiredArgsConstructor
public class PlaceholderStudentProgressAggregator implements StudentProgressAggregator {

    private final AccountRepository accountRepository;

    @Override
    public @Nullable WeeklyProgressContext aggregateForParent(String parentAccountId,
                                                              String tenantId,
                                                              String weekLabel) {
        Account parent = accountRepository.findById(parentAccountId);
        if (parent == null) {
            return null;
        }
        return WeeklyProgressContext.builder()
                .recipientEmail(parent.getEmail())
                .recipientUserId(parent.getUserId())
                .tenantId(tenantId)
                .parentFirstName(Optional.ofNullable(parent.getFirstName()).orElse("there"))
                .institutionName(tenantId)
                .weekLabel(weekLabel)
                .students(List.of(
                        WeeklyProgressContext.StudentSummary.builder()
                                .name("Alex")
                                .lessonsCompleted(3)
                                .highlight("Strong improvement on long division.")
                                .build()
                ))
                .build();
    }
}
