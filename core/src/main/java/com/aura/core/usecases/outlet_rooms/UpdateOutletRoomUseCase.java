package com.aura.core.usecases.outlet_rooms;

import com.aura.core.entities.outlets.OutletRoom;
import org.jspecify.annotations.Nullable;

public interface UpdateOutletRoomUseCase {
    @Nullable OutletRoom updateOutletRoom(OutletRoom outletRoom);
}
