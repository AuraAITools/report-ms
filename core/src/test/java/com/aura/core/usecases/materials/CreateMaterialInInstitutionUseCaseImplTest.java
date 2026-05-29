package com.aura.core.usecases.materials;

import com.aura.core.entities.materials.Material;
import com.aura.core.repositories.MaterialRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Create material in institution use case")
@ExtendWith(MockitoExtension.class)
class CreateMaterialInInstitutionUseCaseImplTest {

    @Mock
    private MaterialRepository materialRepository;
    @InjectMocks
    private CreateMaterialInInstitutionUseCaseImpl useCase;

    @DisplayName("Should save material then link topics when topic IDs are provided")
    @Test
    void create_savesMaterialAndLinksTopics() {
        Material toCreate = Material.builder().name("Algebra").tenantId("tenant-1").build();
        Material persisted = Material.builder().id("mat-1").name("Algebra").tenantId("tenant-1").build();
        when(materialRepository.save(toCreate)).thenReturn(persisted);

        Material result = useCase.create(toCreate, List.of("topic-1", "topic-2"));

        assertThat(result).isSameAs(persisted);
        verify(materialRepository, times(1)).linkTopicsToMaterial("mat-1", List.of("topic-1", "topic-2"), "tenant-1");
    }

    @DisplayName("Should save material and skip linking topics when topic ID list is empty")
    @Test
    void create_skipsLinkingWhenNoTopicIds() {
        Material toCreate = Material.builder().name("Algebra").tenantId("tenant-1").build();
        Material persisted = Material.builder().id("mat-2").name("Algebra").tenantId("tenant-1").build();
        when(materialRepository.save(any())).thenReturn(persisted);

        Material result = useCase.create(toCreate, List.of());

        assertThat(result).isSameAs(persisted);
        verify(materialRepository, never()).linkTopicsToMaterial(anyString(), anyList(), anyString());
    }
}
