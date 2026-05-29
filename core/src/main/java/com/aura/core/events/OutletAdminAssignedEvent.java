package com.aura.core.events;

import lombok.Builder;

@Builder
public record OutletAdminAssignedEvent(
        String outletId,
        String outletName,
        String institutionName,
        String userId,
        String userEmail,
        String userFirstName,
        String tenantId
) {
}
