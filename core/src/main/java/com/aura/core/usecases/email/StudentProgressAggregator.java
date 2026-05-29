package com.aura.core.usecases.email;

import com.aura.core.usecases.email.contexts.WeeklyProgressContext;

public interface StudentProgressAggregator {
    /**
     * Builds the WeeklyProgressContext for a single parent account. Returns null
     * if the parent has no relevant activity for the given week — the caller
     * skips the email send when null.
     */
    WeeklyProgressContext aggregateForParent(String parentAccountId, String tenantId, String weekLabel);
}
