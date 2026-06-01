package com.aura.app.controllers.graphql.v1.institutions;

import com.aura.app.codegen.types.ConfirmInstitutionProfileImageUploadInput;
import com.aura.app.codegen.types.CreateInstitutionInput;
import com.aura.app.codegen.types.InstitutionProfileImageUploadResponse;
import com.aura.app.codegen.types.InstitutionResponse;
import com.aura.app.codegen.types.OutletResponse;
import com.aura.app.codegen.types.RequestInstitutionProfileImageUploadInput;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.app.mappers.InstitutionMapper;
import com.aura.app.mappers.OutletMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.institutions.Institution;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.usecases.institutions.InstitutionQueryUseCase;
import com.aura.core.usecases.outlets.OutletQueryUseCase;
import com.aura.details.services.AssetUrlService;
import com.aura.details.services.InstitutionService;
import com.aura.details.services.OnboardingService;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
public class InstitutionsGqlController {
    private static final Duration LOGO_URL_TTL = Duration.ofMinutes(15);
    private static final Duration PROFILE_IMAGE_URL_TTL = Duration.ofMinutes(15);

    private final InstitutionQueryUseCase institutionQueryUseCase;
    private final OutletQueryUseCase outletQueryUseCase;
    private final OnboardingService onboardingService;
    private final InstitutionService institutionService;
    private final AssetUrlService assetUrlService;

    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    @QueryMapping
    public InstitutionResponse getInstitution(
            @Argument("institutionId") String institutionId,
            @ContextValue(name = "user") User user) {
        return Optional.ofNullable(institutionQueryUseCase.findById(institutionId))
                .map(InstitutionMapper.INSTANCE::from)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("Institution"));
    }

    @PreAuthorize("hasPermission(#institutionIds, 'institution', 'view')")
    @QueryMapping
    public List<InstitutionResponse> getInstitutionsByIds(
            @Argument("institutionIds") List<String> institutionIds,
            @ContextValue(name = "user") User user) {
        return institutionQueryUseCase.findAllByIds(Set.copyOf(institutionIds)).stream()
                .map(InstitutionMapper.INSTANCE::from)
                .toList();
    }

    @PreAuthorize("hasPermission('aura', 'system', 'create_institution')")
    @MutationMapping
    public InstitutionResponse createInstitution(
            @Argument CreateInstitutionInput createInstitutionInput) {
        Institution institution = InstitutionMapper.INSTANCE.from(createInstitutionInput);
        Institution onboardedInstitution = onboardingService.onboard(institution);
        return InstitutionMapper.INSTANCE.from(onboardedInstitution);
    }

    @PreAuthorize("hasPermission(#input.institutionId, 'institution', 'update')")
    @MutationMapping
    public InstitutionProfileImageUploadResponse requestInstitutionProfileImageUpload(
            @Argument RequestInstitutionProfileImageUploadInput input,
            @ContextValue(name = "user") User user) {
        InstitutionService.PresignedProfileImageUpload presigned =
                institutionService.requestProfileImageUpload(input.getInstitutionId(), input.getContentType());
        return InstitutionProfileImageUploadResponse.newBuilder()
                .uploadUrl(presigned.uploadUrl())
                .fileKey(presigned.fileKey())
                .build();
    }

    @PreAuthorize("hasPermission(#input.institutionId, 'institution', 'update')")
    @MutationMapping
    public InstitutionResponse confirmInstitutionProfileImageUpload(
            @Argument ConfirmInstitutionProfileImageUploadInput input,
            @ContextValue(name = "user") User user) {
        Institution updated = institutionService.confirmProfileImageUpload(
                input.getInstitutionId(), input.getFileKey());
        return InstitutionMapper.INSTANCE.from(updated);
    }

    @BatchMapping(typeName = "InstitutionResponse", field = "logoUrl")
    public Map<InstitutionResponse, String> logoUrl(List<InstitutionResponse> institutions) {
        Set<String> ids = institutions.stream()
                .map(InstitutionResponse::getId)
                .collect(Collectors.toSet());

        Map<String, String> logoKeysById = institutionQueryUseCase.findAllByIds(ids).stream()
                .filter(i -> i.getLogoKey() != null)
                .collect(Collectors.toMap(Institution::getId, Institution::getLogoKey));

        return institutions.stream()
                .filter(inst -> logoKeysById.containsKey(inst.getId()))
                .collect(Collectors.toMap(
                        inst -> inst,
                        inst -> assetUrlService.presignGet(logoKeysById.get(inst.getId()), LOGO_URL_TTL)
                ));
    }

    @BatchMapping(typeName = "InstitutionResponse", field = "profileImageUrl")
    public Map<InstitutionResponse, String> profileImageUrl(List<InstitutionResponse> institutions) {
        Set<String> ids = institutions.stream()
                .map(InstitutionResponse::getId)
                .collect(Collectors.toSet());

        Map<String, String> keysById = institutionQueryUseCase.findAllByIds(ids).stream()
                .filter(i -> i.getProfileImageKey() != null)
                .collect(Collectors.toMap(Institution::getId, Institution::getProfileImageKey));

        return institutions.stream()
                .filter(inst -> keysById.containsKey(inst.getId()))
                .collect(Collectors.toMap(
                        inst -> inst,
                        inst -> assetUrlService.presignGet(keysById.get(inst.getId()), PROFILE_IMAGE_URL_TTL)
                ));
    }

    @BatchMapping(typeName = "InstitutionResponse", field = "outlets")
    public Map<InstitutionResponse, List<OutletResponse>> outlets(List<InstitutionResponse> institutions) {
        Set<String> institutionIds = institutions.stream()
                .map(InstitutionResponse::getId)
                .collect(Collectors.toSet());

        Map<String, List<Outlet>> outletsByTenantId = outletQueryUseCase.findAllByTenantIds(institutionIds).stream()
                .collect(Collectors.groupingBy(Outlet::getTenantId));

        return institutions.stream()
                .collect(Collectors.toMap(
                        institution -> institution,
                        institution -> outletsByTenantId
                                .getOrDefault(institution.getId(), List.of())
                                .stream()
                                .map(OutletMapper.INSTANCE::toDto)
                                .toList()
                ));
    }
}
