package com.aura.details.services;

import com.aura.core.entities.accounts.Parent;
import com.aura.core.entities.accounts.Parent.RELATIONSHIP;
import com.aura.core.repositories.ParentRepository;
import com.aura.core.usecases.parents.DeleteParentUseCase;
import com.aura.core.usecases.parents.ParentQueryUseCase;
import com.aura.core.usecases.parents.UpdateParentUseCase;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class ParentService {

    private static final Duration PROFILE_IMAGE_UPLOAD_TTL = Duration.ofMinutes(5);
    private static final Pattern PROFILE_IMAGE_KEY_PATTERN =
            Pattern.compile("^institutions/([^/]+)/parents/([^/]+)/profile\\.(png|jpg|webp)$");

    private final ParentQueryUseCase parentQueryUseCase;
    private final UpdateParentUseCase updateParentUseCase;
    private final DeleteParentUseCase deleteParentUseCase;
    private final ParentRepository parentRepository;
    private final AssetUrlService assetUrlService;
    private final ProfileImageValidator profileImageValidator;

    public @Nullable Parent findById(String id) {
        return parentQueryUseCase.findById(id);
    }

    public List<Parent> findAllByTenantId(String tenantId) {
        return parentQueryUseCase.findAllByTenantId(tenantId);
    }

    @Transactional
    public Parent updateById(String id, @Nullable RELATIONSHIP relationship) {
        return updateParentUseCase.update(id, relationship);
    }

    @Transactional
    public void deleteById(String id) {
        deleteParentUseCase.deleteById(id);
    }

    public PresignedProfileImageUpload requestProfileImageUpload(
            String institutionId, String parentId, String contentType) {
        if (!ProfileImageValidator.ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new ProfileImageValidator.ProfileImageRejectedException(
                    "content type '" + contentType + "' is not allowed; must be one of "
                            + ProfileImageValidator.ALLOWED_CONTENT_TYPES);
        }
        String ext = ProfileImageValidator.extensionFor(contentType);
        String fileKey = "institutions/" + institutionId + "/parents/" + parentId + "/profile." + ext;
        String uploadUrl = assetUrlService.presignPut(fileKey, contentType, PROFILE_IMAGE_UPLOAD_TTL);
        return new PresignedProfileImageUpload(uploadUrl, fileKey);
    }

    @Transactional
    public Parent confirmProfileImageUpload(String institutionId, String parentId, String fileKey) {
        Matcher matcher = PROFILE_IMAGE_KEY_PATTERN.matcher(fileKey);
        if (!matcher.matches()
                || !matcher.group(1).equals(institutionId)
                || !matcher.group(2).equals(parentId)) {
            throw new IllegalArgumentException("fileKey does not match expected scheme for this parent: " + fileKey);
        }
        Parent parent = parentQueryUseCase.findById(parentId);
        if (parent == null || !institutionId.equals(parent.getTenantId())) {
            throw new IllegalArgumentException("parent " + parentId + " not in institution " + institutionId);
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
        String previousKey = parent.getProfileImageKey();
        if (previousKey != null && !previousKey.equals(fileKey)) {
            assetUrlService.deleteObject(previousKey);
        }
        parentRepository.updateProfileImageKeyById(parentId, fileKey);
        return parentQueryUseCase.findById(parentId);
    }

    public record PresignedProfileImageUpload(String uploadUrl, String fileKey) {
    }
}
