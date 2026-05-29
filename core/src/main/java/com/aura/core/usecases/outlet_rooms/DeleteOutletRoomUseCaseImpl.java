package com.aura.core.usecases.outlet_rooms;

import com.aura.core.repositories.OutletRoomRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteOutletRoomUseCaseImpl implements DeleteOutletRoomUseCase {

    private final OutletRoomRepository outletRoomRepository;

    @Override
    public void deleteById(String id) {
        outletRoomRepository.deleteById(id);
    }
}
