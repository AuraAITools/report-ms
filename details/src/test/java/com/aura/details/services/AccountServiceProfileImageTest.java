package com.aura.details.services;

import com.aura.core.clients.IdentityService;
import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.accounts.Account;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.ParentRepository;
import com.aura.core.usecases.accounts.AccountQueryUseCase;
import com.aura.core.usecases.accounts.CreateAccountUseCase;
import com.aura.core.usecases.accounts.CreateEducatorInAccountUseCase;
import com.aura.core.usecases.accounts.CreateStudentsInParentAccountUseCase;
import com.aura.core.usecases.courses.EnrollStudentToCourseUseCase;
import com.aura.core.usecases.courses.TagEducatorToCourseUseCase;
import com.aura.core.usecases.educators.EnrollEducatorToOutletUseCase;
import com.aura.core.usecases.educators.TagEducatorToLevelUseCase;
import com.aura.core.usecases.educators.TagEducatorToSubjectUseCase;
import com.aura.core.usecases.students.TagStudentToLevelUseCase;
import com.aura.core.usecases.students.TagStudentToSchoolUseCase;
import com.aura.details.services.ProfileImageValidator.ProfileImageRejectedException;
import com.aura.details.services.ProfileImageValidator.ValidationResult;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Account service — profile image upload")
@ExtendWith(MockitoExtension.class)
class AccountServiceProfileImageTest {

    @Mock
    private CreateAccountUseCase createAccountUseCase;
    @Mock
    private CreateStudentsInParentAccountUseCase createStudentsInParentAccountUseCase;
    @Mock
    private TagStudentToLevelUseCase tagStudentToLevelUseCase;
    @Mock
    private TagStudentToSchoolUseCase tagStudentToSchoolUseCase;
    @Mock
    private EnrollStudentToCourseUseCase enrollStudentToCourseUseCase;
    @Mock
    private CreateEducatorInAccountUseCase createEducatorInAccountUseCase;
    @Mock
    private TagEducatorToCourseUseCase tagEducatorToCourseUseCase;
    @Mock
    private TagEducatorToLevelUseCase tagEducatorToLevelUseCase;
    @Mock
    private TagEducatorToSubjectUseCase tagEducatorToSubjectUseCase;
    @Mock
    private EnrollEducatorToOutletUseCase enrollEducatorToOutletUseCase;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private IdentityService identityService;
    @Mock
    private RelationshipAuthorizationService relationshipService;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @Mock
    private ParentRepository parentRepository;
    @Mock
    private AccountQueryUseCase accountQueryUseCase;
    @Mock
    private AssetUrlService assetUrlService;
    @Mock
    private ProfileImageValidator profileImageValidator;

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(
                createAccountUseCase,
                createStudentsInParentAccountUseCase,
                tagStudentToLevelUseCase,
                tagStudentToSchoolUseCase,
                enrollStudentToCourseUseCase,
                createEducatorInAccountUseCase,
                tagEducatorToCourseUseCase,
                tagEducatorToLevelUseCase,
                tagEducatorToSubjectUseCase,
                enrollEducatorToOutletUseCase,
                accountRepository,
                identityService,
                relationshipService,
                applicationEventPublisher,
                "https://example/redirect",
                parentRepository,
                accountQueryUseCase,
                assetUrlService,
                profileImageValidator);
    }

    @DisplayName("Should presign PUT URL with the institution/account scoped key for image/png")
    @Test
    void requestProfileImageUpload_pngBuildsKeyWithPngExt() {
        when(assetUrlService.presignPut(anyString(), eq("image/png"), any(Duration.class)))
                .thenReturn("https://signed.example/upload");

        AccountService.PresignedProfileImageUpload result =
                accountService.requestProfileImageUpload("inst-1", "acc-1", "image/png");

        assertThat(result.uploadUrl()).isEqualTo("https://signed.example/upload");
        assertThat(result.fileKey()).isEqualTo("institutions/inst-1/accounts/acc-1/profile.png");
    }

    @DisplayName("Should presign PUT URL with .jpg extension for image/jpeg")
    @Test
    void requestProfileImageUpload_jpegUsesJpgExt() {
        when(assetUrlService.presignPut(anyString(), eq("image/jpeg"), any(Duration.class)))
                .thenReturn("https://signed.example/upload");

        AccountService.PresignedProfileImageUpload result =
                accountService.requestProfileImageUpload("inst-1", "acc-1", "image/jpeg");

        assertThat(result.fileKey()).isEqualTo("institutions/inst-1/accounts/acc-1/profile.jpg");
    }

    @DisplayName("Should reject content type application/pdf at request-upload step")
    @Test
    void requestProfileImageUpload_rejectsDisallowedContentType() {
        assertThatThrownBy(() ->
                accountService.requestProfileImageUpload("inst-1", "acc-1", "application/pdf"))
                .isInstanceOf(ProfileImageRejectedException.class)
                .hasMessageContaining("content type");
        verify(assetUrlService, never()).presignPut(anyString(), anyString(), any());
    }

    @DisplayName("Should reject fileKey that does not match expected institutions/<id>/accounts/<id>/profile.<ext> scheme")
    @Test
    void confirm_rejectsMalformedKey() {
        assertThatThrownBy(() -> accountService.confirmProfileImageUpload(
                "inst-1", "acc-1", "totally/different/path.png"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("fileKey");
        verify(accountRepository, never()).updateProfileImageKeyById(anyString(), anyString());
    }

    @DisplayName("Should reject when account.tenantId does not equal claimed institutionId")
    @Test
    void confirm_rejectsTenantMismatch() {
        Account stored = Account.builder().id("acc-1").tenantId("OTHER").build();
        when(accountQueryUseCase.findById("acc-1")).thenReturn(stored);

        assertThatThrownBy(() -> accountService.confirmProfileImageUpload(
                "inst-1", "acc-1", "institutions/inst-1/accounts/acc-1/profile.png"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Should throw IllegalStateException when uploaded object is missing in S3")
    @Test
    void confirm_throwsWhenObjectMissing() {
        Account stored = Account.builder().id("acc-1").tenantId("inst-1").build();
        when(accountQueryUseCase.findById("acc-1")).thenReturn(stored);
        when(assetUrlService.objectExists("institutions/inst-1/accounts/acc-1/profile.png"))
                .thenReturn(false);

        assertThatThrownBy(() -> accountService.confirmProfileImageUpload(
                "inst-1", "acc-1", "institutions/inst-1/accounts/acc-1/profile.png"))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("Should delete the uploaded object and re-throw when validator rejects it")
    @Test
    void confirm_deletesAndRethrowsOnValidatorRejection() {
        String key = "institutions/inst-1/accounts/acc-1/profile.png";
        Account stored = Account.builder().id("acc-1").tenantId("inst-1").build();
        when(accountQueryUseCase.findById("acc-1")).thenReturn(stored);
        when(assetUrlService.objectExists(key)).thenReturn(true);
        when(assetUrlService.getObjectContentType(key)).thenReturn("image/png");
        when(assetUrlService.getObjectSizeBytes(key)).thenReturn(1000L);
        when(assetUrlService.getObjectBytes(key)).thenReturn(new byte[]{1, 2, 3});
        when(profileImageValidator.validate(any(), eq("image/png"), eq(1000L)))
                .thenThrow(new ProfileImageRejectedException("aspect"));

        assertThatThrownBy(() -> accountService.confirmProfileImageUpload("inst-1", "acc-1", key))
                .isInstanceOf(ProfileImageRejectedException.class)
                .hasMessageContaining("aspect");

        verify(assetUrlService).deleteObject(key);
        verify(accountRepository, never()).updateProfileImageKeyById(anyString(), anyString());
    }

    @DisplayName("Should persist new key and delete previous key on extension change")
    @Test
    void confirm_persistsAndDeletesOldKeyOnExtChange() {
        String oldKey = "institutions/inst-1/accounts/acc-1/profile.jpg";
        String newKey = "institutions/inst-1/accounts/acc-1/profile.png";
        Account stored = Account.builder().id("acc-1").tenantId("inst-1").profileImageKey(oldKey).build();
        Account refreshed = Account.builder().id("acc-1").tenantId("inst-1").profileImageKey(newKey).build();
        when(accountQueryUseCase.findById("acc-1")).thenReturn(stored, refreshed);
        when(assetUrlService.objectExists(newKey)).thenReturn(true);
        when(assetUrlService.getObjectContentType(newKey)).thenReturn("image/png");
        when(assetUrlService.getObjectSizeBytes(newKey)).thenReturn(2000L);
        when(assetUrlService.getObjectBytes(newKey)).thenReturn(new byte[]{1, 2, 3});
        when(profileImageValidator.validate(any(), anyString(), anyLong()))
                .thenReturn(new ValidationResult(400, 400));

        Account result = accountService.confirmProfileImageUpload("inst-1", "acc-1", newKey);

        verify(assetUrlService).deleteObject(oldKey);
        verify(accountRepository).updateProfileImageKeyById("acc-1", newKey);
        assertThat(result.getProfileImageKey()).isEqualTo(newKey);
    }

    @DisplayName("Should return account carrying the new profile image key even though the bulk update bypasses the persistence context")
    @Test
    void confirm_returnsEntityWithNewKeyWithoutStaleReFetch() {
        String newKey = "institutions/inst-1/accounts/acc-1/profile.png";
        // The account loaded at the start of confirm still has a null key; a re-fetch in the same
        // transaction would return this same stale instance from the first-level cache.
        Account stored = Account.builder().id("acc-1").tenantId("inst-1").profileImageKey(null).build();
        when(accountQueryUseCase.findById("acc-1")).thenReturn(stored);
        when(assetUrlService.objectExists(newKey)).thenReturn(true);
        when(assetUrlService.getObjectContentType(newKey)).thenReturn("image/png");
        when(assetUrlService.getObjectSizeBytes(newKey)).thenReturn(2000L);
        when(assetUrlService.getObjectBytes(newKey)).thenReturn(new byte[]{1, 2, 3});
        when(profileImageValidator.validate(any(), anyString(), anyLong()))
                .thenReturn(new ValidationResult(400, 400));

        Account result = accountService.confirmProfileImageUpload("inst-1", "acc-1", newKey);

        assertThat(result.getProfileImageKey()).isEqualTo(newKey);
        verify(accountRepository).updateProfileImageKeyById("acc-1", newKey);
    }

    @DisplayName("Should not attempt to delete previous key when same key is re-confirmed")
    @Test
    void confirm_doesNotDeletePreviousWhenSameKey() {
        String key = "institutions/inst-1/accounts/acc-1/profile.png";
        Account stored = Account.builder().id("acc-1").tenantId("inst-1").profileImageKey(key).build();
        when(accountQueryUseCase.findById("acc-1")).thenReturn(stored, stored);
        when(assetUrlService.objectExists(key)).thenReturn(true);
        when(assetUrlService.getObjectContentType(key)).thenReturn("image/png");
        when(assetUrlService.getObjectSizeBytes(key)).thenReturn(2000L);
        when(assetUrlService.getObjectBytes(key)).thenReturn(new byte[]{1, 2, 3});
        when(profileImageValidator.validate(any(), anyString(), anyLong()))
                .thenReturn(new ValidationResult(400, 400));

        accountService.confirmProfileImageUpload("inst-1", "acc-1", key);

        verify(assetUrlService, never()).deleteObject(key);
        verify(accountRepository).updateProfileImageKeyById("acc-1", key);
    }
}
