package com.aura.core.repositories;

import com.aura.core.entities.schools.School;

public interface SchoolRepository {
    School findByNameAndTenantId(String name, String tenantId);
}
