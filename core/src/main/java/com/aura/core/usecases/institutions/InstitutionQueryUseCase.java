package com.aura.core.usecases.institutions;

import com.aura.core.entities.institutions.Institution;
import com.aura.core.usecases.commons.QueryUseCase;
import com.aura.core.usecases.commons.TenantAwareQueryUseCase;

public interface InstitutionQueryUseCase extends QueryUseCase<Institution>, TenantAwareQueryUseCase<Institution> {
}
