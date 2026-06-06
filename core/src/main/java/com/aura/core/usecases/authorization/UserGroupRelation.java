package com.aura.core.usecases.authorization;

import java.util.List;

public record UserGroupRelation(
        String resourceType,
        String resourceId,
        String displayName,
        List<String> relations
) {
}
