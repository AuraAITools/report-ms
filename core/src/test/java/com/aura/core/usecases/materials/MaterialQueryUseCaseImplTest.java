package com.aura.core.usecases.materials;

import com.aura.core.entities.materials.Material;
import com.aura.core.repositories.MaterialRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Material query use case")
@ExtendWith(MockitoExtension.class)
class MaterialQueryUseCaseImplTest {

    @Mock
    private MaterialRepository materialRepository;
    @InjectMocks
    private MaterialQueryUseCaseImpl useCase;

    @DisplayName("Should delegate findById to the repository and return its result")
    @Test
    void findById_delegatesToRepository() {
        Material expected = Material.builder().id("mat-1").name("Algebra").build();
        when(materialRepository.findById("mat-1")).thenReturn(expected);

        assertThat(useCase.findById("mat-1")).isSameAs(expected);
    }

    @DisplayName("Should return null when the repository has no material for the id")
    @Test
    void findById_returnsNullWhenAbsent() {
        when(materialRepository.findById("missing")).thenReturn(null);

        assertThat(useCase.findById("missing")).isNull();
    }
}
