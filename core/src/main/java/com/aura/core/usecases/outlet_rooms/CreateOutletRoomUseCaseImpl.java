package com.aura.core.usecases.outlet_rooms;

import com.aura.core.entities.outlets.OutletRoom;
import com.aura.core.repositories.OutletRoomRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateOutletRoomUseCaseImpl implements CreateOutletRoomUseCase {

    private final OutletRoomRepository outletRoomRepository;

    @Override
    public OutletRoom createOutletRoom(OutletRoom outletRoom) {
        return outletRoomRepository.save(outletRoom);
    }
}
