package com.aura.core.usecases.outlets;

import com.aura.core.repositories.OutletRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.verify;

@DisplayName("Delete outlet use case")
@ExtendWith(MockitoExtension.class)
class DeleteOutletUseCaseImplTest {

    @Mock
    private OutletRepository outletRepository;

    @InjectMocks
    private DeleteOutletUseCaseImpl useCase;

    @Test
    @DisplayName("Should delegate deleteById to the outlet repository")
    void deletesViaRepository() {
        useCase.deleteById("outlet-1");

        verify(outletRepository).deleteById("outlet-1");
    }
}
