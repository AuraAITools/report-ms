package com.aura.core.usecases.authorization;

import java.util.List;

public record ResourceRelations(String resourceType, String resourceId, List<String> relations) {
}
