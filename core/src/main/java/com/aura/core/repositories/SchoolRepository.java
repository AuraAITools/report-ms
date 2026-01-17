package com.aura.core.repositories;

import com.aura.core.entities.schools.School;
import org.jspecify.annotations.Nullable;

public interface SchoolRepository {
    @Nullable School findByNameAndTenantId(String name, String tenantId);
}
