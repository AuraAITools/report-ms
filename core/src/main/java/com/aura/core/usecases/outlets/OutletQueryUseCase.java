package com.aura.core.usecases.outlets;

import com.aura.core.entities.outlets.Outlet;
import com.aura.core.usecases.commons.QueryUseCase;
import com.aura.core.usecases.commons.TenantAwareQueryUseCase;

public interface OutletQueryUseCase extends QueryUseCase<Outlet>, TenantAwareQueryUseCase<Outlet> {
}
