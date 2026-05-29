package com.aura.core.usecases.levels;

import com.aura.core.repositories.LevelRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.verify;

@DisplayName("Delete level use case")
@ExtendWith(MockitoExtension.class)
class DeleteLevelUseCaseImplTest {

    @Mock
    private LevelRepository levelRepository;

    @InjectMocks
    private DeleteLevelUseCaseImpl useCase;

    @Test
    @DisplayName("Should delegate deleteById to the level repository")
    void deletesViaRepository() {
        useCase.deleteById("level-1");

        verify(levelRepository).deleteById("level-1");
    }
}
