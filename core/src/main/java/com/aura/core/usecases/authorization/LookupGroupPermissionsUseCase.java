package com.aura.core.usecases.authorization;

import java.util.List;

public interface LookupGroupPermissionsUseCase {
    List<ResourcePermission> lookupGroupPermissions(String userId);
}
