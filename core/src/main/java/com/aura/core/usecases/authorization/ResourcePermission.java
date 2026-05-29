package com.aura.core.usecases.authorization;

import java.util.List;

public record ResourcePermission(String resourceType, String resourceId, List<String> permissions) {
}
