package com.aura.app.controllers.graphql.v1.parents;

import com.aura.app.codegen.types.ConfirmParentProfileImageUploadInput;
import com.aura.app.codegen.types.DeleteParentByIdInput;
import com.aura.app.codegen.types.GetParentByIdInput;
import com.aura.app.codegen.types.ParentProfileImageUploadResponse;
import com.aura.app.codegen.types.ParentResponse;
import com.aura.app.codegen.types.RequestParentProfileImageUploadInput;
import com.aura.app.codegen.types.UpdateParentByIdInput;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.app.mappers.ParentMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.accounts.Parent;
import com.aura.details.services.AssetUrlService;
import com.aura.details.services.ParentService;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ParentsGqlController {

    private static final Duration PROFILE_IMAGE_URL_TTL = Duration.ofMinutes(15);

    private final ParentService parentService;
    private final AssetUrlService assetUrlService;

    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    @QueryMapping
    public List<ParentResponse> getAllParentsInInstitution(@Argument("institutionId") String institutionId,
                                                           @ContextValue("user") User user) {
        return parentService.findAllByTenantId(institutionId).stream()
                .map(ParentMapper.INSTANCE::toDto)
                .toList();
    }

    @PreAuthorize("hasPermission(#input.institutionId, 'institution', 'view')")
    @QueryMapping
    public ParentResponse getParentById(@Argument("input") GetParentByIdInput input,
                                        @ContextValue("user") User user) {
        return Optional.ofNullable(parentService.findById(input.getId()))
                .map(ParentMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("Parent"));
    }

    @PreAuthorize("hasPermission(#input.institutionId, 'institution', 'update')")
    @MutationMapping
    public ParentResponse updateParentById(@Argument("input") UpdateParentByIdInput input,
                                           @ContextValue("user") User user) {
        Parent.RELATIONSHIP rel = input.getRelationship() == null
                ? null
                : Parent.RELATIONSHIP.valueOf(input.getRelationship().name());
        Parent updated = parentService.updateById(input.getId(), rel);
        return ParentMapper.INSTANCE.toDto(updated);
    }

    @PreAuthorize("hasPermission(#input.institutionId, 'institution', 'delete')")
    @MutationMapping
    public Boolean deleteParentById(@Argument("input") DeleteParentByIdInput input,
                                    @ContextValue("user") User user) {
        parentService.deleteById(input.getId());
        return true;
    }

    @PreAuthorize("hasPermission(#input.institutionId, 'institution', 'update')")
    @MutationMapping
    public ParentProfileImageUploadResponse requestParentProfileImageUpload(
            @Argument RequestParentProfileImageUploadInput input,
            @ContextValue("user") User user) {
        ParentService.PresignedProfileImageUpload presigned = parentService.requestProfileImageUpload(
                input.getInstitutionId(), input.getParentId(), input.getContentType());
        return ParentProfileImageUploadResponse.newBuilder()
                .uploadUrl(presigned.uploadUrl())
                .fileKey(presigned.fileKey())
                .build();
    }

    @PreAuthorize("hasPermission(#input.institutionId, 'institution', 'update')")
    @MutationMapping
    public ParentResponse confirmParentProfileImageUpload(
            @Argument ConfirmParentProfileImageUploadInput input,
            @ContextValue("user") User user) {
        Parent updated = parentService.confirmProfileImageUpload(
                input.getInstitutionId(), input.getParentId(), input.getFileKey());
        return ParentMapper.INSTANCE.toDto(updated);
    }

    @BatchMapping(typeName = "ParentResponse", field = "profileImageUrl")
    public Map<ParentResponse, String> profileImageUrl(List<ParentResponse> parents) {
        Set<String> ids = parents.stream().map(ParentResponse::getId).collect(Collectors.toSet());

        Map<String, String> keysById = ids.stream()
                .map(parentService::findById)
                .filter(Objects::nonNull)
                .filter(p -> p.getProfileImageKey() != null)
                .collect(Collectors.toMap(Parent::getId, Parent::getProfileImageKey));

        return parents.stream()
                .filter(p -> keysById.containsKey(p.getId()))
                .collect(Collectors.toMap(
                        Function.identity(),
                        p -> assetUrlService.presignGet(keysById.get(p.getId()), PROFILE_IMAGE_URL_TTL)
                ));
    }
}
