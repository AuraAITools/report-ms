package com.aura.core.usecases.authorization;

import java.util.List;

public interface LookupPermissionsUseCase {
    List<ResourcePermission> lookupPermissions(String userId);
}
