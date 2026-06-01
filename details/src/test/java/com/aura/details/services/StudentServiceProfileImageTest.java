package com.aura.details.services;

import com.aura.core.entities.students.Student;
import com.aura.core.repositories.StudentRepository;
import com.aura.core.usecases.students.DeleteStudentUseCase;
import com.aura.core.usecases.students.StudentQueryUseCase;
import com.aura.core.usecases.students.UpdateStudentUseCase;
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

@DisplayName("Student service — profile image upload")
@ExtendWith(MockitoExtension.class)
class StudentServiceProfileImageTest {

    @Mock
    private StudentQueryUseCase studentQueryUseCase;
    @Mock
    private UpdateStudentUseCase updateStudentUseCase;
    @Mock
    private DeleteStudentUseCase deleteStudentUseCase;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private AssetUrlService assetUrlService;
    @Mock
    private ProfileImageValidator profileImageValidator;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentQueryUseCase, updateStudentUseCase, deleteStudentUseCase,
                studentRepository, assetUrlService, profileImageValidator);
    }

    @DisplayName("Should presign PUT URL with institutions/<id>/students/<id>/profile.png")
    @Test
    void requestProfileImageUpload_pngBuildsKey() {
        when(assetUrlService.presignPut(anyString(), eq("image/png"), any(Duration.class)))
                .thenReturn("https://signed.example/upload");

        StudentService.PresignedProfileImageUpload result =
                studentService.requestProfileImageUpload("inst-1", "stu-1", "image/png");

        assertThat(result.uploadUrl()).isEqualTo("https://signed.example/upload");
        assertThat(result.fileKey()).isEqualTo("institutions/inst-1/students/stu-1/profile.png");
    }

    @DisplayName("Should reject content type application/pdf at request-upload step")
    @Test
    void requestProfileImageUpload_rejectsDisallowedContentType() {
        assertThatThrownBy(() ->
                studentService.requestProfileImageUpload("inst-1", "stu-1", "application/pdf"))
                .isInstanceOf(ProfileImageRejectedException.class)
                .hasMessageContaining("content type");
        verify(assetUrlService, never()).presignPut(anyString(), anyString(), any());
    }

    @DisplayName("Should reject fileKey that does not match expected scheme")
    @Test
    void confirm_rejectsMalformedKey() {
        assertThatThrownBy(() -> studentService.confirmProfileImageUpload(
                "inst-1", "stu-1", "wrong/path/profile.png"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("fileKey");
        verify(studentRepository, never()).updateProfileImageKeyById(anyString(), anyString());
    }

    @DisplayName("Should reject when student does not belong to claimed institution")
    @Test
    void confirm_rejectsTenantMismatch() {
        Student stored = Student.builder().id("stu-1").tenantId("OTHER").build();
        when(studentQueryUseCase.findById("stu-1")).thenReturn(stored);

        assertThatThrownBy(() -> studentService.confirmProfileImageUpload(
                "inst-1", "stu-1", "institutions/inst-1/students/stu-1/profile.png"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Should throw IllegalStateException when uploaded object is missing in S3")
    @Test
    void confirm_throwsWhenObjectMissing() {
        Student stored = Student.builder().id("stu-1").tenantId("inst-1").build();
        String key = "institutions/inst-1/students/stu-1/profile.png";
        when(studentQueryUseCase.findById("stu-1")).thenReturn(stored);
        when(assetUrlService.objectExists(key)).thenReturn(false);

        assertThatThrownBy(() -> studentService.confirmProfileImageUpload("inst-1", "stu-1", key))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("Should delete the uploaded object and re-throw when validator rejects it")
    @Test
    void confirm_deletesAndRethrowsOnValidatorRejection() {
        String key = "institutions/inst-1/students/stu-1/profile.png";
        Student stored = Student.builder().id("stu-1").tenantId("inst-1").build();
        when(studentQueryUseCase.findById("stu-1")).thenReturn(stored);
        when(assetUrlService.objectExists(key)).thenReturn(true);
        when(assetUrlService.getObjectContentType(key)).thenReturn("image/png");
        when(assetUrlService.getObjectSizeBytes(key)).thenReturn(1000L);
        when(assetUrlService.getObjectBytes(key)).thenReturn(new byte[]{1, 2, 3});
        when(profileImageValidator.validate(any(), eq("image/png"), eq(1000L)))
                .thenThrow(new ProfileImageRejectedException("aspect"));

        assertThatThrownBy(() -> studentService.confirmProfileImageUpload("inst-1", "stu-1", key))
                .isInstanceOf(ProfileImageRejectedException.class)
                .hasMessageContaining("aspect");

        verify(assetUrlService).deleteObject(key);
        verify(studentRepository, never()).updateProfileImageKeyById(anyString(), anyString());
    }

    @DisplayName("Should persist new key and delete previous key on extension change")
    @Test
    void confirm_persistsAndDeletesOldKeyOnExtChange() {
        String oldKey = "institutions/inst-1/students/stu-1/profile.jpg";
        String newKey = "institutions/inst-1/students/stu-1/profile.png";
        Student stored = Student.builder().id("stu-1").tenantId("inst-1").profileImageKey(oldKey).build();
        Student refreshed = Student.builder().id("stu-1").tenantId("inst-1").profileImageKey(newKey).build();
        when(studentQueryUseCase.findById("stu-1")).thenReturn(stored, refreshed);
        when(assetUrlService.objectExists(newKey)).thenReturn(true);
        when(assetUrlService.getObjectContentType(newKey)).thenReturn("image/png");
        when(assetUrlService.getObjectSizeBytes(newKey)).thenReturn(2000L);
        when(assetUrlService.getObjectBytes(newKey)).thenReturn(new byte[]{1, 2, 3});
        when(profileImageValidator.validate(any(), anyString(), anyLong()))
                .thenReturn(new ValidationResult(400, 400));

        Student result = studentService.confirmProfileImageUpload("inst-1", "stu-1", newKey);

        verify(assetUrlService).deleteObject(oldKey);
        verify(studentRepository).updateProfileImageKeyById("stu-1", newKey);
        assertThat(result.getProfileImageKey()).isEqualTo(newKey);
    }
}
