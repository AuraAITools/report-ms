package com.aura.app.controllers.graphql.v1.materials;

import com.aura.app.codegen.types.CreateMaterialInInstitutionInput;
import com.aura.app.codegen.types.DeleteMaterialByIdInput;
import com.aura.app.codegen.types.MaterialResponse;
import com.aura.app.codegen.types.MaterialUploadResponse;
import com.aura.app.codegen.types.RequestMaterialUploadInput;
import com.aura.app.codegen.types.TopicResponse;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.app.mappers.MaterialMapper;
import com.aura.app.mappers.TopicMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.materials.Material;
import com.aura.core.entities.topics.Topic;
import com.aura.details.services.AssetUrlService;
import com.aura.details.services.MaterialService;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MaterialsGqlController {

    private static final Duration FILE_URL_TTL = Duration.ofMinutes(15);

    private final MaterialService materialService;
    private final AssetUrlService assetUrlService;

    @PreAuthorize("hasPermission(#input.institutionId, 'institution', 'create_material')")
    @MutationMapping
    public MaterialUploadResponse requestMaterialUpload(
            @Argument RequestMaterialUploadInput input,
            @ContextValue(name = "user") User user) {
        MaterialService.PresignedUpload presigned =
                materialService.requestMaterialUpload(input.getInstitutionId(), input.getContentType());
        return MaterialUploadResponse.newBuilder()
                .uploadUrl(presigned.uploadUrl())
                .fileKey(presigned.fileKey())
                .build();
    }

    @PreAuthorize("hasPermission(#input.institutionId, 'institution', 'create_material')")
    @MutationMapping
    public MaterialResponse createMaterialInInstitution(
            @Argument CreateMaterialInInstitutionInput input,
            @ContextValue(name = "user") User user) {
        Material toCreate = MaterialMapper.INSTANCE.toDomainEntity(input);
        toCreate.setFileKey(input.getFileKey());
        Material created = materialService.createMaterialInInstitution(toCreate, input.getTopicIds());
        return MaterialMapper.INSTANCE.toDto(created);
    }

    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    @QueryMapping
    public List<MaterialResponse> getMaterialsInInstitution(@Argument("institutionId") String institutionId,
                                                            @ContextValue(name = "user") User user) {
        return materialService.findAllByTenantId(institutionId).stream()
                .map(MaterialMapper.INSTANCE::toDto)
                .toList();
    }

    @PreAuthorize("hasPermission(#materialId, 'material', 'view')")
    @QueryMapping
    public MaterialResponse getMaterialById(@Argument("institutionId") String institutionId,
                                            @Argument("materialId") String materialId,
                                            @ContextValue(name = "user") User user) {
        return Optional.ofNullable(materialService.findById(materialId))
                .map(MaterialMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("Account"));
    }

    @PreAuthorize("hasPermission(#deleteMaterialByIdInput.materialId, 'material', 'delete')")
    @MutationMapping
    public Boolean deleteMaterialById(@Argument DeleteMaterialByIdInput deleteMaterialByIdInput,
                                      @ContextValue(name = "user") User user) {
        Optional.ofNullable(materialService.findById(deleteMaterialByIdInput.getMaterialId()))
                .ifPresentOrElse(material -> materialService.deleteById(material.getId()),
                        () -> {
                            throw new ResourceNotFoundBusinessException("Material");
                        });
        return true;
    }

    @SchemaMapping(typeName = "MaterialResponse", field = "fileUrl")
    public @Nullable String fileUrl(MaterialResponse material) {
        return Optional.ofNullable(materialService.findById(material.getId()))
                .map(stored -> assetUrlService.presignGet(stored.getFileKey(), FILE_URL_TTL))
                .orElse(null);
    }

    @BatchMapping(typeName = "MaterialResponse", field = "topics")
    public Map<MaterialResponse, List<TopicResponse>> topics(List<MaterialResponse> materialResponses) {
        List<String> materialIds = materialResponses.stream().map(MaterialResponse::getId).toList();
        Map<String, List<Topic>> topicsByMaterialId = materialService.findTopicsByMaterialIds(materialIds);
        return materialResponses.stream().collect(Collectors.toMap(
                Function.identity(),
                m -> topicsByMaterialId.getOrDefault(m.getId(), List.of()).stream()
                        .map(TopicMapper.INSTANCE::toDto).toList()
        ));
    }
}
