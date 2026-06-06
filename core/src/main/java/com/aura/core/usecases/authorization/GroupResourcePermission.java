package com.aura.core.usecases.authorization;

import java.util.List;

public record GroupResourcePermission(
        String resourceType,
        String resourceId,
        String displayName,
        List<String> permissions
) {
}
