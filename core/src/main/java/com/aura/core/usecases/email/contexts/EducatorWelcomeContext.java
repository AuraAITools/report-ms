package com.aura.core.usecases.email.contexts;

import lombok.Builder;

@Builder
public record EducatorWelcomeContext(
        String recipientEmail,
        String recipientUserId,
        String tenantId,
        String firstName,
        String institutionName,
        String mobileAppDeeplink
) {
}
