package com.aura.core.repositories;

import com.aura.core.entities.outlets.OutletRoom;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface OutletRoomRepository {
    OutletRoom save(OutletRoom outletRoom);

    @Nullable OutletRoom findById(String id);

    List<OutletRoom> findAllByIds(List<UUID> ids);

    List<OutletRoom> findAllByTenantId(String tenantId);

    List<OutletRoom> findAllByOutletId(String outletId);

    void deleteById(String id);
}
