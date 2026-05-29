package com.aura.core.usecases.outlet_rooms;

import com.aura.core.entities.outlets.OutletRoom;
import com.aura.core.entities.outlets.OutletRoom.OUTLET_ROOM_TYPE;
import com.aura.core.repositories.OutletRoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Create outlet-room use case")
@ExtendWith(MockitoExtension.class)
class CreateOutletRoomUseCaseImplTest {

    @Mock
    private OutletRoomRepository outletRoomRepository;
    @InjectMocks
    private CreateOutletRoomUseCaseImpl useCase;

    @DisplayName("Should delegate save to the repository and return the saved room")
    @Test
    void createOutletRoom_delegatesToRepository() {
        OutletRoom input = OutletRoom.builder().name("Studio A").type(OUTLET_ROOM_TYPE.PHYSICAL).build();
        OutletRoom saved = OutletRoom.builder().id("room-1").name("Studio A").type(OUTLET_ROOM_TYPE.PHYSICAL).build();
        when(outletRoomRepository.save(input)).thenReturn(saved);

        assertThat(useCase.createOutletRoom(input)).isSameAs(saved);
    }
}
