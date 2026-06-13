package com.aura.details.services;

import com.aura.core.entities.outlets.OutletRoom;
import com.aura.core.entities.outlets.OutletRoom.OUTLET_ROOM_TYPE;
import com.aura.core.repositories.OutletRoomRepository;
import com.aura.core.usecases.outlet_rooms.CreateOutletRoomUseCase;
import com.aura.core.usecases.outlet_rooms.DeleteOutletRoomUseCase;
import com.aura.core.usecases.outlet_rooms.OutletRoomQueryUseCase;
import com.aura.core.usecases.outlet_rooms.UpdateOutletRoomUseCase;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("OutletRoom service")
@ExtendWith(MockitoExtension.class)
class OutletRoomServiceTest {

    @Mock
    private CreateOutletRoomUseCase createOutletRoomUseCase;
    @Mock
    private UpdateOutletRoomUseCase updateOutletRoomUseCase;
    @Mock
    private DeleteOutletRoomUseCase deleteOutletRoomUseCase;
    @Mock
    private OutletRoomQueryUseCase outletRoomQueryUseCase;
    @Mock
    private OutletRoomRepository outletRoomRepository;

    @InjectMocks
    private OutletRoomService outletRoomService;

    @DisplayName("Should create outlet room by delegating to the use case")
    @Test
    void createOutletRoom_delegatesToUseCase() {
        OutletRoom input = OutletRoom.builder().name("Studio A").type(OUTLET_ROOM_TYPE.PHYSICAL).build();
        OutletRoom created = OutletRoom.builder()
                .id("room-1").outletId("outlet-1").tenantId("tenant-1")
                .name("Studio A").type(OUTLET_ROOM_TYPE.PHYSICAL).build();
        when(createOutletRoomUseCase.createOutletRoom(input)).thenReturn(created);

        assertThat(outletRoomService.createOutletRoom(input)).isSameAs(created);
    }

    @DisplayName("Should delegate updateOutletRoom to the use case and return its result")
    @Test
    void updateOutletRoom_delegatesToUseCase() {
        OutletRoom input = OutletRoom.builder().id("room-1").name("Renamed").build();
        OutletRoom updated = OutletRoom.builder().id("room-1").name("Renamed").build();
        when(updateOutletRoomUseCase.updateOutletRoom(input)).thenReturn(updated);

        assertThat(outletRoomService.updateOutletRoom(input)).isEqualTo(updated);
    }

    @DisplayName("Should return null from updateOutletRoom when use case returns null")
    @Test
    void updateOutletRoom_returnsNullWhenUseCaseReturnsNull() {
        OutletRoom input = OutletRoom.builder().id("missing").build();
        when(updateOutletRoomUseCase.updateOutletRoom(input)).thenReturn(null);

        assertThat(outletRoomService.updateOutletRoom(input)).isNull();
    }

    @DisplayName("Should delete the outlet room by delegating to the use case")
    @Test
    void deleteById_delegatesToUseCase() {
        outletRoomService.deleteById("room-1");
        verify(deleteOutletRoomUseCase).deleteById("room-1");
    }

    @DisplayName("Should delegate findById to the query use case")
    @Test
    void findById_delegatesToQueryUseCase() {
        OutletRoom room = OutletRoom.builder().id("room-1").build();
        when(outletRoomQueryUseCase.findById("room-1")).thenReturn(room);

        assertThat(outletRoomService.findById("room-1")).isEqualTo(room);
    }

    @DisplayName("Should delegate findAllByIds to the query use case")
    @Test
    void findAllByIds_delegatesToQueryUseCase() {
        UUID id = UUID.randomUUID();
        OutletRoom room = OutletRoom.builder().id(id.toString()).build();
        when(outletRoomQueryUseCase.findAllByIds(List.of(id))).thenReturn(List.of(room));

        assertThat(outletRoomService.findAllByIds(List.of(id))).containsExactly(room);
    }

    @DisplayName("Should delegate findAllByTenantId to the query use case")
    @Test
    void findAllByTenantId_delegatesToQueryUseCase() {
        OutletRoom r1 = OutletRoom.builder().id("r1").build();
        OutletRoom r2 = OutletRoom.builder().id("r2").build();
        when(outletRoomQueryUseCase.findAllByTenantId("tenant-1")).thenReturn(List.of(r1, r2));

        assertThat(outletRoomService.findAllByTenantId("tenant-1")).containsExactly(r1, r2);
    }

    @DisplayName("Should delegate findAllByOutletId to the query use case")
    @Test
    void findAllByOutletId_delegatesToQueryUseCase() {
        OutletRoom r1 = OutletRoom.builder().id("r1").outletId("outlet-1").build();
        when(outletRoomQueryUseCase.findAllByOutletId("outlet-1")).thenReturn(List.of(r1));

        assertThat(outletRoomService.findAllByOutletId("outlet-1")).containsExactly(r1);
    }
}
