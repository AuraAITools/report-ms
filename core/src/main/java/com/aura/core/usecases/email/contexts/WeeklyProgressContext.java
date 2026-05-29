package com.aura.core.usecases.email.contexts;

import java.util.List;
import lombok.Builder;

@Builder
public record WeeklyProgressContext(
        String recipientEmail,
        String recipientUserId,
        String tenantId,
        String parentFirstName,
        String institutionName,
        String weekLabel,
        List<StudentSummary> students
) {
    @Builder
    public record StudentSummary(
            String name,
            int lessonsCompleted,
            String highlight
    ) {
    }
}
