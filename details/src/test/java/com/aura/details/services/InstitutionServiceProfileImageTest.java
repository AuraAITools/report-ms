package com.aura.details.services;

import com.aura.core.entities.institutions.Institution;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.usecases.institutions.InstitutionQueryUseCase;
import com.aura.details.services.ProfileImageValidator.ProfileImageRejectedException;
import com.aura.details.services.ProfileImageValidator.ValidationResult;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Institution service — profile image upload")
@ExtendWith(MockitoExtension.class)
class InstitutionServiceProfileImageTest {

    @Mock
    private InstitutionRepository institutionRepository;
    @Mock
    private InstitutionQueryUseCase institutionQueryUseCase;
    @Mock
    private AssetUrlService assetUrlService;
    @Mock
    private ProfileImageValidator profileImageValidator;

    private InstitutionService institutionService;

    @BeforeEach
    void setUp() {
        institutionService = new InstitutionService(
                institutionRepository, institutionQueryUseCase, assetUrlService, profileImageValidator);
    }

    @DisplayName("Should presign PUT URL with institutions/<id>/profile.png for image/png")
    @Test
    void requestProfileImageUpload_pngBuildsKeyWithPngExt() {
        when(assetUrlService.presignPut(anyString(), eq("image/png"), any(Duration.class)))
                .thenReturn("https://signed.example/upload");

        InstitutionService.PresignedProfileImageUpload result =
                institutionService.requestProfileImageUpload("inst-1", "image/png");

        assertThat(result.uploadUrl()).isEqualTo("https://signed.example/upload");
        assertThat(result.fileKey()).isEqualTo("institutions/inst-1/profile.png");
    }

    @DisplayName("Should use .jpg extension for image/jpeg and .webp for image/webp")
    @Test
    void requestProfileImageUpload_jpegAndWebpExtensions() {
        when(assetUrlService.presignPut(anyString(), anyString(), any(Duration.class)))
                .thenReturn("https://signed.example/upload");

        assertThat(institutionService.requestProfileImageUpload("inst-1", "image/jpeg").fileKey())
                .isEqualTo("institutions/inst-1/profile.jpg");
        assertThat(institutionService.requestProfileImageUpload("inst-1", "image/webp").fileKey())
                .isEqualTo("institutions/inst-1/profile.webp");
    }

    @DisplayName("Should reject content type application/pdf at request-upload step")
    @Test
    void requestProfileImageUpload_rejectsDisallowedContentType() {
        assertThatThrownBy(() ->
                institutionService.requestProfileImageUpload("inst-1", "application/pdf"))
                .isInstanceOf(ProfileImageRejectedException.class)
                .hasMessageContaining("content type");
        verify(assetUrlService, never()).presignPut(anyString(), anyString(), any());
    }

    @DisplayName("Should reject fileKey that does not match expected institutions/<id>/profile.<ext> scheme")
    @Test
    void confirm_rejectsMalformedKey() {
        assertThatThrownBy(() -> institutionService.confirmProfileImageUpload(
                "inst-1", "totally/different/path.png"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("fileKey");
        verify(institutionRepository, never()).updateProfileImageKeyById(anyString(), anyString());
    }

    @DisplayName("Should reject fileKey whose institutionId segment does not match the call's institutionId")
    @Test
    void confirm_rejectsKeyForDifferentInstitution() {
        assertThatThrownBy(() -> institutionService.confirmProfileImageUpload(
                "inst-1", "institutions/OTHER/profile.png"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("fileKey");
    }

    @DisplayName("Should reject when institution does not exist")
    @Test
    void confirm_rejectsWhenInstitutionMissing() {
        when(institutionQueryUseCase.findById("inst-1")).thenReturn(null);

        assertThatThrownBy(() -> institutionService.confirmProfileImageUpload(
                "inst-1", "institutions/inst-1/profile.png"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found");
    }

    @DisplayName("Should throw IllegalStateException when uploaded object is missing in S3")
    @Test
    void confirm_throwsWhenObjectMissing() {
        Institution stored = Institution.builder().id("inst-1").build();
        when(institutionQueryUseCase.findById("inst-1")).thenReturn(stored);
        when(assetUrlService.objectExists("institutions/inst-1/profile.png")).thenReturn(false);

        assertThatThrownBy(() -> institutionService.confirmProfileImageUpload(
                "inst-1", "institutions/inst-1/profile.png"))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("Should delete the uploaded object and re-throw when validator rejects it")
    @Test
    void confirm_deletesAndRethrowsOnValidatorRejection() {
        String key = "institutions/inst-1/profile.png";
        Institution stored = Institution.builder().id("inst-1").build();
        when(institutionQueryUseCase.findById("inst-1")).thenReturn(stored);
        when(assetUrlService.objectExists(key)).thenReturn(true);
        when(assetUrlService.getObjectContentType(key)).thenReturn("image/png");
        when(assetUrlService.getObjectSizeBytes(key)).thenReturn(1000L);
        when(assetUrlService.getObjectBytes(key)).thenReturn(new byte[]{1, 2, 3});
        when(profileImageValidator.validate(any(), eq("image/png"), eq(1000L)))
                .thenThrow(new ProfileImageRejectedException("aspect"));

        assertThatThrownBy(() -> institutionService.confirmProfileImageUpload("inst-1", key))
                .isInstanceOf(ProfileImageRejectedException.class)
                .hasMessageContaining("aspect");

        verify(assetUrlService).deleteObject(key);
        verify(institutionRepository, never()).updateProfileImageKeyById(anyString(), anyString());
    }

    @DisplayName("Should persist new key and delete previous key on extension change")
    @Test
    void confirm_persistsAndDeletesOldKeyOnExtChange() {
        String oldKey = "institutions/inst-1/profile.jpg";
        String newKey = "institutions/inst-1/profile.png";
        Institution stored = Institution.builder().id("inst-1").profileImageKey(oldKey).build();
        Institution refreshed = Institution.builder().id("inst-1").profileImageKey(newKey).build();
        when(institutionQueryUseCase.findById("inst-1")).thenReturn(stored, refreshed);
        when(assetUrlService.objectExists(newKey)).thenReturn(true);
        when(assetUrlService.getObjectContentType(newKey)).thenReturn("image/png");
        when(assetUrlService.getObjectSizeBytes(newKey)).thenReturn(2000L);
        when(assetUrlService.getObjectBytes(newKey)).thenReturn(new byte[]{1, 2, 3});
        when(profileImageValidator.validate(any(), anyString(), anyLong()))
                .thenReturn(new ValidationResult(400, 400));

        Institution result = institutionService.confirmProfileImageUpload("inst-1", newKey);

        verify(assetUrlService).deleteObject(oldKey);
        verify(institutionRepository).updateProfileImageKeyById("inst-1", newKey);
        assertThat(result.getProfileImageKey()).isEqualTo(newKey);
    }

    @DisplayName("Should not attempt to delete previous key when same key is re-confirmed")
    @Test
    void confirm_doesNotDeletePreviousWhenSameKey() {
        String key = "institutions/inst-1/profile.png";
        Institution stored = Institution.builder().id("inst-1").profileImageKey(key).build();
        when(institutionQueryUseCase.findById("inst-1")).thenReturn(stored, stored);
        when(assetUrlService.objectExists(key)).thenReturn(true);
        when(assetUrlService.getObjectContentType(key)).thenReturn("image/png");
        when(assetUrlService.getObjectSizeBytes(key)).thenReturn(2000L);
        when(assetUrlService.getObjectBytes(key)).thenReturn(new byte[]{1, 2, 3});
        when(profileImageValidator.validate(any(), anyString(), anyLong()))
                .thenReturn(new ValidationResult(400, 400));

        institutionService.confirmProfileImageUpload("inst-1", key);

        verify(assetUrlService, never()).deleteObject(key);
        verify(institutionRepository).updateProfileImageKeyById("inst-1", key);
    }
}
