package com.aura.core.usecases.outlets;

import com.aura.core.entities.outlets.Outlet;
import org.jspecify.annotations.Nullable;

public interface UpdateOutletUseCase {
    @Nullable Outlet updateOutlet(Outlet outlet);
}
