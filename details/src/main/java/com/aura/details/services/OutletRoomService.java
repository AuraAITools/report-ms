package com.aura.details.services;

import com.aura.core.entities.outlets.OutletRoom;
import com.aura.core.repositories.OutletRoomRepository;
import com.aura.core.usecases.outlet_rooms.CreateOutletRoomUseCase;
import com.aura.core.usecases.outlet_rooms.DeleteOutletRoomUseCase;
import com.aura.core.usecases.outlet_rooms.OutletRoomQueryUseCase;
import com.aura.core.usecases.outlet_rooms.UpdateOutletRoomUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class OutletRoomService {

    private final CreateOutletRoomUseCase createOutletRoomUseCase;
    private final UpdateOutletRoomUseCase updateOutletRoomUseCase;
    private final DeleteOutletRoomUseCase deleteOutletRoomUseCase;
    private final OutletRoomQueryUseCase outletRoomQueryUseCase;
    private final OutletRoomRepository outletRoomRepository;

    @Transactional
    public OutletRoom createOutletRoom(OutletRoom outletRoom) {
        return createOutletRoomUseCase.createOutletRoom(outletRoom);
    }

    @Transactional
    public @Nullable OutletRoom updateOutletRoom(OutletRoom outletRoom) {
        return updateOutletRoomUseCase.updateOutletRoom(outletRoom);
    }

    @Transactional
    public void deleteById(String id) {
        deleteOutletRoomUseCase.deleteById(id);
    }

    public @Nullable OutletRoom findById(String id) {
        return outletRoomQueryUseCase.findById(id);
    }

    public List<OutletRoom> findAllByIds(List<UUID> ids) {
        return outletRoomQueryUseCase.findAllByIds(ids);
    }

    public List<OutletRoom> findAllByTenantId(String tenantId) {
        return outletRoomQueryUseCase.findAllByTenantId(tenantId);
    }

    public List<OutletRoom> findAllByOutletId(String outletId) {
        return outletRoomQueryUseCase.findAllByOutletId(outletId);
    }
}
