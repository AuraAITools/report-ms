package com.aura.core.usecases.schools;

import com.aura.core.entities.schools.School;
import java.util.List;

public interface SchoolQueryUseCase {
    List<School> findAllByTenantId(String tenantId);
}
