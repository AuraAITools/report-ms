package com.aura.details.services;

import com.aura.core.entities.institutions.Institution;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.usecases.institutions.InstitutionQueryUseCase;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Institution-scoped operations that aren't part of the onboarding flow.
 * Onboarding-specific writes live in {@link OnboardingService}.
 *
 * <p>Profile image upload mirrors {@link AccountService}'s pattern (see ADR-005):
 * presigned PUT for the upload, server-validated confirm step that persists the
 * key only after {@link ProfileImageValidator} accepts the bytes.
 */
@Slf4j
@RequiredArgsConstructor
public class InstitutionService {

    private static final Duration PROFILE_IMAGE_UPLOAD_TTL = Duration.ofMinutes(5);
    private static final Pattern PROFILE_IMAGE_KEY_PATTERN =
            Pattern.compile("^institutions/([^/]+)/profile\\.(png|jpg|webp)$");

    private final InstitutionRepository institutionRepository;
    private final InstitutionQueryUseCase institutionQueryUseCase;
    private final AssetUrlService assetUrlService;
    private final ProfileImageValidator profileImageValidator;

    public PresignedProfileImageUpload requestProfileImageUpload(String institutionId, String contentType) {
        if (!ProfileImageValidator.ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new ProfileImageValidator.ProfileImageRejectedException(
                    "content type '" + contentType + "' is not allowed; must be one of "
                            + ProfileImageValidator.ALLOWED_CONTENT_TYPES);
        }
        String ext = ProfileImageValidator.extensionFor(contentType);
        String fileKey = "institutions/" + institutionId + "/profile." + ext;
        String uploadUrl = assetUrlService.presignPut(fileKey, contentType, PROFILE_IMAGE_UPLOAD_TTL);
        return new PresignedProfileImageUpload(uploadUrl, fileKey);
    }

    @Transactional
    public Institution confirmProfileImageUpload(String institutionId, String fileKey) {
        Matcher matcher = PROFILE_IMAGE_KEY_PATTERN.matcher(fileKey);
        if (!matcher.matches() || !matcher.group(1).equals(institutionId)) {
            throw new IllegalArgumentException("fileKey does not match expected scheme for this institution: " + fileKey);
        }
        Institution institution = institutionQueryUseCase.findById(institutionId);
        if (institution == null) {
            throw new IllegalArgumentException("institution " + institutionId + " not found");
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
        String previousKey = institution.getProfileImageKey();
        if (previousKey != null && !previousKey.equals(fileKey)) {
            assetUrlService.deleteObject(previousKey);
        }
        institutionRepository.updateProfileImageKeyById(institutionId, fileKey);
        return institutionQueryUseCase.findById(institutionId);
    }

    public record PresignedProfileImageUpload(String uploadUrl, String fileKey) {
    }
}
