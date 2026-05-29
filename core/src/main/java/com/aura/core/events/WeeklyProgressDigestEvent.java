package com.aura.core.events;

public record WeeklyProgressDigestEvent(
        String parentAccountId,
        String tenantId,
        String weekLabel
) {
}
