package com.aura.core.repositories;

import com.aura.core.entities.outlets.Outlet;
import java.util.Optional;

public interface OutletRepository {
    Optional<Outlet> findById(String id);

    Outlet save(Outlet outlet);
}
