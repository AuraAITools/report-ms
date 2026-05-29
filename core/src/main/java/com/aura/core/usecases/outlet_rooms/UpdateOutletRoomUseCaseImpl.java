package com.aura.core.usecases.outlet_rooms;

import com.aura.core.entities.outlets.OutletRoom;
import com.aura.core.repositories.OutletRoomRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class UpdateOutletRoomUseCaseImpl implements UpdateOutletRoomUseCase {

    private final OutletRoomRepository outletRoomRepository;

    @Override
    public @Nullable OutletRoom updateOutletRoom(OutletRoom outletRoom) {
        OutletRoom existing = outletRoomRepository.findById(outletRoom.getId());
        if (existing == null || !existing.getTenantId().equals(outletRoom.getTenantId())) {
            return null;
        }
        existing.setName(outletRoom.getName());
        existing.setSize(outletRoom.getSize());
        existing.setDetails(outletRoom.getDetails());
        existing.setFileUrl(outletRoom.getFileUrl());
        existing.setType(outletRoom.getType());
        existing.setUrl(outletRoom.getUrl());
        existing.setPasscode(outletRoom.getPasscode());
        existing.setDescription(outletRoom.getDescription());
        return outletRoomRepository.save(existing);
    }
}
