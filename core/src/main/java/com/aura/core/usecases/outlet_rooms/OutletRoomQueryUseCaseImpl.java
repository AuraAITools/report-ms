package com.aura.core.usecases.outlet_rooms;

import com.aura.core.entities.outlets.OutletRoom;
import com.aura.core.repositories.OutletRoomRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class OutletRoomQueryUseCaseImpl implements OutletRoomQueryUseCase {

    private final OutletRoomRepository outletRoomRepository;

    @Override
    public @Nullable OutletRoom findById(String id) {
        return outletRoomRepository.findById(id);
    }

    @Override
    public List<OutletRoom> findAllByIds(List<UUID> ids) {
        return outletRoomRepository.findAllByIds(ids);
    }

    @Override
    public List<OutletRoom> findAllByTenantId(String tenantId) {
        return outletRoomRepository.findAllByTenantId(tenantId);
    }

    @Override
    public List<OutletRoom> findAllByOutletId(String outletId) {
        return outletRoomRepository.findAllByOutletId(outletId);
    }
}
