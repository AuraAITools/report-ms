package com.aura.core.usecases.outlets;

import com.aura.core.entities.outlets.Outlet;
import com.aura.core.usecases.commons.TenantAwareQueryUseCase;
import java.util.List;
import java.util.Set;

public interface OutletQueryUseCase extends TenantAwareQueryUseCase<Outlet> {
    List<Outlet> findAllByTenantIds(Set<String> tenantIds);
}
