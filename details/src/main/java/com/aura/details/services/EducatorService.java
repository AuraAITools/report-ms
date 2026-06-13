package com.aura.details.services;

import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.educators.Educator.EMPLOYMENT_TYPE;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.usecases.educators.DeleteEducatorUseCase;
import com.aura.core.usecases.educators.EducatorQueryUseCase;
import com.aura.core.usecases.educators.UpdateEducatorUseCase;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class EducatorService {

    private static final Duration PROFILE_IMAGE_UPLOAD_TTL = Duration.ofMinutes(5);
    private static final Pattern PROFILE_IMAGE_KEY_PATTERN =
            Pattern.compile("^institutions/([^/]+)/educators/([^/]+)/profile\\.(png|jpg|webp)$");

    private final EducatorQueryUseCase educatorQueryUseCase;
    private final UpdateEducatorUseCase updateEducatorUseCase;
    private final DeleteEducatorUseCase deleteEducatorUseCase;
    private final EducatorRepository educatorRepository;
    private final AssetUrlService assetUrlService;
    private final ProfileImageValidator profileImageValidator;

    public @Nullable Educator findById(String id) {
        return educatorQueryUseCase.findById(id);
    }

    public List<Educator> findAllByTenantId(String tenantId) {
        return educatorQueryUseCase.findAllByTenantId(tenantId);
    }

    @Transactional
    public Educator updateById(String id,
                               @Nullable String name,
                               @Nullable LocalDate startDate,
                               @Nullable LocalDate dateOfBirth,
                               @Nullable String email,
                               @Nullable EMPLOYMENT_TYPE employmentType) {
        return updateEducatorUseCase.update(id, name, startDate, dateOfBirth, email, employmentType);
    }

    @Transactional
    public void deleteById(String id) {
        educatorRepository.deleteAllEducatorCourseLinksByEducatorId(id);
        educatorRepository.deleteAllEducatorLessonLinksByEducatorId(id);
        educatorRepository.deleteAllLevelEducatorLinksByEducatorId(id);
        educatorRepository.deleteAllOutletEducatorLinksByEducatorId(id);
        educatorRepository.deleteAllSubjectEducatorLinksByEducatorId(id);
        educatorRepository.deleteAllAccountEducatorLinksByEducatorId(id);
        deleteEducatorUseCase.deleteById(id);
    }

    public PresignedProfileImageUpload requestProfileImageUpload(
            String institutionId, String educatorId, String contentType) {
        if (!ProfileImageValidator.ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new ProfileImageValidator.ProfileImageRejectedException(
                    "content type '" + contentType + "' is not allowed; must be one of "
                            + ProfileImageValidator.ALLOWED_CONTENT_TYPES);
        }
        String ext = ProfileImageValidator.extensionFor(contentType);
        String fileKey = "institutions/" + institutionId + "/educators/" + educatorId + "/profile." + ext;
        String uploadUrl = assetUrlService.presignPut(fileKey, contentType, PROFILE_IMAGE_UPLOAD_TTL);
        return new PresignedProfileImageUpload(uploadUrl, fileKey);
    }

    @Transactional
    public Educator confirmProfileImageUpload(String institutionId, String educatorId, String fileKey) {
        Matcher matcher = PROFILE_IMAGE_KEY_PATTERN.matcher(fileKey);
        if (!matcher.matches()
                || !matcher.group(1).equals(institutionId)
                || !matcher.group(2).equals(educatorId)) {
            throw new IllegalArgumentException("fileKey does not match expected scheme for this educator: " + fileKey);
        }
        Educator educator = educatorQueryUseCase.findById(educatorId);
        if (educator == null || !institutionId.equals(educator.getTenantId())) {
            throw new IllegalArgumentException("educator " + educatorId + " not in institution " + institutionId);
        }
        if (!assetUrlService.objectExists(fileKey)) {
            throw new IllegalStateException("uploaded file not found at key " + fileKey);
        }
        String actualContentType = assetUrlService.getObjectContentType(fileKey);
        long sizeBytes = assetUrlService.getObjectSizeBytes(fileKey);
        byte[] bytes = assetUrlService.getObjectBytes(fileKey);
        try {
            profileImageValidator.validate(bytes, actualContentType, sizeBytes);
        } catch (ProfileImageValidator.ProfileImageRejectedException e) {
            assetUrlService.deleteObject(fileKey);
            throw e;
        }
        String previousKey = educator.getProfileImageKey();
        if (previousKey != null && !previousKey.equals(fileKey)) {
            assetUrlService.deleteObject(previousKey);
        }
        educatorRepository.updateProfileImageKeyById(educatorId, fileKey);
        // The bulk update bypasses the persistence context, so a re-fetch inside this transaction
        // would return the stale first-level-cached model with the old key. Carry the new key on
        // the already-loaded domain entity instead.
        educator.setProfileImageKey(fileKey);
        return educator;
    }

    public record PresignedProfileImageUpload(String uploadUrl, String fileKey) {
    }
}
