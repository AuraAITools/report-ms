package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.materials.Material;
import com.aura.core.usecases.materials.CreateMaterialInInstitutionUseCase;
import com.aura.core.usecases.materials.MaterialQueryUseCase;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Material service")
@ExtendWith(MockitoExtension.class)
class MaterialServiceTest {

    @Mock
    private CreateMaterialInInstitutionUseCase createUseCase;
    @Mock
    private MaterialQueryUseCase queryUseCase;
    @Mock
    private AssetUrlService assetUrlService;
    @Mock
    private RelationshipAuthorizationService relationshipService;
    @InjectMocks
    private MaterialService materialService;

    @DisplayName("Should reserve a unique S3 key under institutions/<id>/materials and presign a PUT URL")
    @Test
    void requestMaterialUpload_buildsKeyAndPresignsPut() {
        when(assetUrlService.presignPut(anyString(), eq("application/pdf"), any(Duration.class)))
                .thenReturn("https://signed.example/upload");

        MaterialService.PresignedUpload result =
                materialService.requestMaterialUpload("inst-1", "application/pdf");

        assertThat(result.uploadUrl()).isEqualTo("https://signed.example/upload");
        assertThat(result.fileKey()).startsWith("institutions/inst-1/materials/");
    }

    @DisplayName("Should persist the material and write parent SpiceDB relationship when no fileKey is set")
    @Test
    void create_persistsAndWritesRelationshipWithoutFileKey() {
        Material toCreate = Material.builder().name("Algebra").tenantId("tenant-1").build();
        Material persisted = Material.builder().id("mat-1").name("Algebra").tenantId("tenant-1").build();
        when(createUseCase.create(toCreate, List.of())).thenReturn(persisted);

        Material result = materialService.createMaterialInInstitution(toCreate, List.of());

        assertThat(result).isSameAs(persisted);
        verify(assetUrlService, never()).objectExists(anyString());
        verify(relationshipService, times(1))
                .writeRelationship("material", "mat-1", "parent", "institution", "tenant-1");
    }

    @DisplayName("Should verify file exists in S3 then persist the material when fileKey is set")
    @Test
    void create_verifiesFileExistsWhenFileKeyProvided() {
        Material toCreate = Material.builder()
                .name("Algebra").tenantId("tenant-1")
                .fileKey("institutions/tenant-1/materials/abc").build();
        Material persisted = Material.builder().id("mat-1").name("Algebra").tenantId("tenant-1")
                .fileKey("institutions/tenant-1/materials/abc").build();
        when(assetUrlService.objectExists("institutions/tenant-1/materials/abc")).thenReturn(true);
        when(createUseCase.create(toCreate, List.of())).thenReturn(persisted);

        Material result = materialService.createMaterialInInstitution(toCreate, List.of());

        assertThat(result).isSameAs(persisted);
        verify(assetUrlService, times(1)).objectExists("institutions/tenant-1/materials/abc");
    }

    @DisplayName("Should throw IllegalStateException when fileKey is set but the object does not exist in S3")
    @Test
    void create_throwsWhenFileMissing() {
        Material toCreate = Material.builder()
                .name("Algebra").tenantId("tenant-1")
                .fileKey("missing/key").build();
        when(assetUrlService.objectExists("missing/key")).thenReturn(false);

        assertThatThrownBy(() -> materialService.createMaterialInInstitution(toCreate, List.of()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("missing/key");

        verify(createUseCase, never()).create(any(), anyList());
        verify(relationshipService, never())
                .writeRelationship(anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @DisplayName("Should delegate findById to the query use case")
    @Test
    void findById_delegates() {
        Material expected = Material.builder().id("mat-1").build();
        when(queryUseCase.findById("mat-1")).thenReturn(expected);

        assertThat(materialService.findById("mat-1")).isSameAs(expected);
    }
}
