package com.aura.core.usecases.outlet_rooms;

import com.aura.core.entities.outlets.OutletRoom;
import com.aura.core.repositories.OutletRoomRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("OutletRoom query use case")
@ExtendWith(MockitoExtension.class)
class OutletRoomQueryUseCaseImplTest {

    @Mock
    private OutletRoomRepository outletRoomRepository;
    @InjectMocks
    private OutletRoomQueryUseCaseImpl useCase;

    @DisplayName("Should delegate findById to the repository")
    @Test
    void findById_delegatesToRepository() {
        OutletRoom room = OutletRoom.builder().id("room-1").build();
        when(outletRoomRepository.findById("room-1")).thenReturn(room);

        assertThat(useCase.findById("room-1")).isEqualTo(room);
    }

    @DisplayName("Should return null when the room is not found")
    @Test
    void findById_returnsNullWhenMissing() {
        when(outletRoomRepository.findById("missing")).thenReturn(null);

        assertThat(useCase.findById("missing")).isNull();
    }

    @DisplayName("Should delegate findAllByIds to the repository")
    @Test
    void findAllByIds_delegatesToRepository() {
        UUID id = UUID.randomUUID();
        OutletRoom room = OutletRoom.builder().id(id.toString()).build();
        when(outletRoomRepository.findAllByIds(List.of(id))).thenReturn(List.of(room));

        assertThat(useCase.findAllByIds(List.of(id))).containsExactly(room);
    }

    @DisplayName("Should delegate findAllByTenantId to the repository")
    @Test
    void findAllByTenantId_delegatesToRepository() {
        OutletRoom r1 = OutletRoom.builder().id("r1").build();
        OutletRoom r2 = OutletRoom.builder().id("r2").build();
        when(outletRoomRepository.findAllByTenantId("tenant-1")).thenReturn(List.of(r1, r2));

        assertThat(useCase.findAllByTenantId("tenant-1")).containsExactly(r1, r2);
    }

    @DisplayName("Should delegate findAllByOutletId to the repository")
    @Test
    void findAllByOutletId_delegatesToRepository() {
        OutletRoom r1 = OutletRoom.builder().id("r1").build();
        when(outletRoomRepository.findAllByOutletId("outlet-1")).thenReturn(List.of(r1));

        assertThat(useCase.findAllByOutletId("outlet-1")).containsExactly(r1);
    }
}
