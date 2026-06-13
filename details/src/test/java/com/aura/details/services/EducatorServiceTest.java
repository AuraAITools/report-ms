package com.aura.details.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.aura.core.entities.educators.Educator;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.usecases.educators.DeleteEducatorUseCase;
import com.aura.core.usecases.educators.EducatorQueryUseCase;
import com.aura.core.usecases.educators.UpdateEducatorUseCase;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("EducatorService")
class EducatorServiceTest {

    @Mock
    private EducatorQueryUseCase queryUseCase;
    @Mock
    private UpdateEducatorUseCase updateUseCase;
    @Mock
    private DeleteEducatorUseCase deleteUseCase;
    @Mock
    private EducatorRepository educatorRepository;
    @Mock
    private AssetUrlService assetUrlService;
    @Mock
    private ProfileImageValidator profileImageValidator;
    @InjectMocks
    private EducatorService service;

    @Test
    @DisplayName("Should delegate findById to the query use case")
    void findByIdDelegates() {
        Educator e = Educator.builder().id("a").build();
        when(queryUseCase.findById("a")).thenReturn(e);
        assertThat(service.findById("a")).isEqualTo(e);
    }

    @Test
    @DisplayName("Should delegate findAllByTenantId to the query use case")
    void findAllByTenantIdDelegates() {
        when(queryUseCase.findAllByTenantId("t")).thenReturn(List.of());
        assertThat(service.findAllByTenantId("t")).isEmpty();
    }

    @Test
    @DisplayName("Should delegate updateById to the update use case")
    void updateByIdDelegates() {
        Educator e = Educator.builder().id("a").name("New").build();
        when(updateUseCase.update("a", "New", null, null, null, null)).thenReturn(e);
        assertThat(service.updateById("a", "New", null, null, null, null)).isEqualTo(e);
    }

    @Test
    @DisplayName("Should clean all link rows before calling delete use case on deleteById")
    void deleteByIdClearsLinksThenDeletes() {
        service.deleteById("eid");

        InOrder order = inOrder(educatorRepository, deleteUseCase);
        order.verify(educatorRepository).deleteAllEducatorCourseLinksByEducatorId("eid");
        order.verify(educatorRepository).deleteAllEducatorLessonLinksByEducatorId("eid");
        order.verify(educatorRepository).deleteAllLevelEducatorLinksByEducatorId("eid");
        order.verify(educatorRepository).deleteAllOutletEducatorLinksByEducatorId("eid");
        order.verify(educatorRepository).deleteAllSubjectEducatorLinksByEducatorId("eid");
        order.verify(educatorRepository).deleteAllAccountEducatorLinksByEducatorId("eid");
        order.verify(deleteUseCase).deleteById("eid");
    }

    @Test
    @DisplayName("Should return educator carrying the new profile image key even though the bulk update bypasses the persistence context")
    void confirmProfileImageUploadReturnsEntityWithNewKey() {
        String newKey = "institutions/inst-1/educators/edu-1/profile.png";
        // The educator loaded at the start of confirm still has a null key; a re-fetch in the same
        // transaction would return this same stale instance from the first-level cache.
        Educator stored = Educator.builder().id("edu-1").tenantId("inst-1").profileImageKey(null).build();
        when(queryUseCase.findById("edu-1")).thenReturn(stored);
        when(assetUrlService.objectExists(newKey)).thenReturn(true);
        when(assetUrlService.getObjectContentType(newKey)).thenReturn("image/png");
        when(assetUrlService.getObjectSizeBytes(newKey)).thenReturn(2000L);
        when(assetUrlService.getObjectBytes(newKey)).thenReturn(new byte[]{1, 2, 3});
        when(profileImageValidator.validate(any(), anyString(), anyLong()))
                .thenReturn(new ProfileImageValidator.ValidationResult(400, 400));

        Educator result = service.confirmProfileImageUpload("inst-1", "edu-1", newKey);

        assertThat(result.getProfileImageKey()).isEqualTo(newKey);
        verify(educatorRepository).updateProfileImageKeyById("edu-1", newKey);
    }

    @Test
    @DisplayName("Should delete the previous profile image object when confirming a key with a different extension")
    void confirmProfileImageUploadDeletesPreviousKeyOnChange() {
        String oldKey = "institutions/inst-1/educators/edu-1/profile.jpg";
        String newKey = "institutions/inst-1/educators/edu-1/profile.png";
        Educator stored = Educator.builder().id("edu-1").tenantId("inst-1").profileImageKey(oldKey).build();
        when(queryUseCase.findById("edu-1")).thenReturn(stored);
        when(assetUrlService.objectExists(newKey)).thenReturn(true);
        when(assetUrlService.getObjectContentType(newKey)).thenReturn("image/png");
        when(assetUrlService.getObjectSizeBytes(newKey)).thenReturn(2000L);
        when(assetUrlService.getObjectBytes(newKey)).thenReturn(new byte[]{1, 2, 3});
        when(profileImageValidator.validate(any(), anyString(), anyLong()))
                .thenReturn(new ProfileImageValidator.ValidationResult(400, 400));

        Educator result = service.confirmProfileImageUpload("inst-1", "edu-1", newKey);

        assertThat(result.getProfileImageKey()).isEqualTo(newKey);
        verify(assetUrlService).deleteObject(oldKey);
        verify(educatorRepository).updateProfileImageKeyById("edu-1", newKey);
    }
}
