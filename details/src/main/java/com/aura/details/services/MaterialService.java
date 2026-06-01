package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.materials.Material;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.MaterialRepository;
import com.aura.core.usecases.materials.CreateMaterialInInstitutionUseCase;
import com.aura.core.usecases.materials.DeleteMaterialUseCase;
import com.aura.core.usecases.materials.MaterialQueryUseCase;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class MaterialService {

    private static final Duration UPLOAD_TTL = Duration.ofMinutes(5);

    private final CreateMaterialInInstitutionUseCase createMaterialInInstitutionUseCase;
    private final MaterialQueryUseCase materialQueryUseCase;
    private final DeleteMaterialUseCase deleteMaterialUseCase;
    private final MaterialRepository materialRepository;
    private final AssetUrlService assetUrlService;
    private final RelationshipAuthorizationService relationshipService;

    /**
     * Reserves an S3 object key under the institution's prefix and returns a presigned
     * PUT URL the client uses to upload the file directly to S3.
     * <p>
     * The key is server-generated (UUID-suffixed) — never trust client-supplied paths.
     * Nothing is persisted at this point; the client passes the key back to
     * {@link #createMaterialInInstitution} after the upload completes.
     */
    public PresignedUpload requestMaterialUpload(String institutionId, String contentType) {
        String fileKey = "institutions/" + institutionId + "/materials/" + UUID.randomUUID();
        String uploadUrl = assetUrlService.presignPut(fileKey, contentType, UPLOAD_TTL);
        return new PresignedUpload(uploadUrl, fileKey);
    }

    /**
     * Persists a Material in the given institution. If {@code material.fileKey} is set,
     * we verify the object exists in S3 (HeadObject) before saving — this catches the
     * client claiming an upload succeeded when it never landed.
     */
    @Transactional
    public Material createMaterialInInstitution(Material material, List<String> topicIds) {
        if (material.getFileKey() != null) {
            if (!assetUrlService.objectExists(material.getFileKey())) {
                throw new IllegalStateException(
                        "Uploaded file not found at key " + material.getFileKey());
            }
        }
        Material created = createMaterialInInstitutionUseCase.create(material, topicIds);
        relationshipService.writeRelationship(
                "material", created.getId(), "parent", "institution", created.getTenantId());
        return created;
    }

    public @Nullable Material findById(String id) {
        return materialQueryUseCase.findById(id);
    }

    public List<Material> findAllByTenantId(String tenantId) {
        return materialQueryUseCase.findAllByTenantId(tenantId);
    }

    public Map<String, List<Topic>> findTopicsByMaterialIds(List<String> materialIds) {
        return materialQueryUseCase.findTopicsByMaterialIds(materialIds);
    }

    @Transactional
    public void deleteById(String id) {
        materialRepository.deleteAllTopicLinksByMaterialId(id);
        materialRepository.deleteAllLessonPlanLinksByMaterialId(id);
        deleteMaterialUseCase.deleteById(id);
        relationshipService.deleteAllRelationshipsForResource("material", id);
        relationshipService.deleteAllRelationshipsForResource("material_user_group", id);
    }

    public record PresignedUpload(String uploadUrl, String fileKey) {
    }
}
