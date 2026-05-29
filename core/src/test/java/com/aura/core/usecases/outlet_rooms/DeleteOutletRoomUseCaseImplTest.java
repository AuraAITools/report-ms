package com.aura.core.usecases.outlet_rooms;

import com.aura.core.repositories.OutletRoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@DisplayName("Delete outlet-room use case")
@ExtendWith(MockitoExtension.class)
class DeleteOutletRoomUseCaseImplTest {

    @Mock
    private OutletRoomRepository outletRoomRepository;
    @InjectMocks
    private DeleteOutletRoomUseCaseImpl useCase;

    @DisplayName("Should delegate deletion to the repository by id")
    @Test
    void deleteById_delegatesToRepository() {
        useCase.deleteById("room-1");

        verify(outletRoomRepository).deleteById("room-1");
        verifyNoMoreInteractions(outletRoomRepository);
    }
}
