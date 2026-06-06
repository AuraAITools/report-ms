package com.aura.core.usecases.authorization;

import java.util.List;
import org.jspecify.annotations.Nullable;

public interface LookupUserGroupRelationsUseCase {
    List<ResourceRelations> lookupUserGroupRelations(String userId,
                                                     List<String> resourceTypes,
                                                     @Nullable String resourceId);
}
