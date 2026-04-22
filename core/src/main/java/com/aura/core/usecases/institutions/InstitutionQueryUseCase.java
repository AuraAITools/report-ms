package com.aura.core.usecases.institutions;

import com.aura.core.entities.institutions.Institution;
import com.aura.core.usecases.commons.TenantAwareQueryUseCase;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;

public interface InstitutionQueryUseCase extends TenantAwareQueryUseCase<Institution> {
    @Nullable Institution findById(String id);

    List<Institution> findAllByIds(Set<String> ids);
}
