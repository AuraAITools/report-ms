package com.aura.core.usecases.levels;

import com.aura.core.entities.levels.Level;
import com.aura.core.entities.schools.SchoolCategory;
import com.aura.core.repositories.LevelRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Create level use case")
@ExtendWith(MockitoExtension.class)
class CreateLevelUseCaseImplTest {

    @Mock
    private LevelRepository levelRepository;

    @InjectMocks
    private CreateLevelUseCaseImpl useCase;

    @Test
    @DisplayName("Should persist the level via the repository and return the saved result")
    void createLevel_persistsAndReturns() {
        Level input = Level.builder().name("P1").category(SchoolCategory.PRIMARY).tenantId("tenant-1").build();
        Level saved = Level.builder().id("level-1").name("P1").category(SchoolCategory.PRIMARY).tenantId("tenant-1").build();
        when(levelRepository.save(input)).thenReturn(saved);

        Level result = useCase.createLevel(input);

        assertThat(result).isEqualTo(saved);
        verify(levelRepository).save(input);
    }
}
