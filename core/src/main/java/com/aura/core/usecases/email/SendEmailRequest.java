package com.aura.core.usecases.email;

import java.util.Map;
import lombok.Builder;

@Builder
public record SendEmailRequest(
        String templateName,
        String subject,
        String dedupKey,
        String recipientEmail,
        String recipientUserId,
        String tenantId,
        Map<String, Object> variables,
        boolean recipientOptedOut
) {
}
