package com.aura.core.entities.email;

import lombok.Builder;

@Builder
public record EmailMessage(
        String recipientEmail,
        String subject,
        String htmlBody,
        String textBody,
        String dedupKey,
        String templateName,
        String recipientUserId,
        String tenantId
) {
}
