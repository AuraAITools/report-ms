package com.aura.app.controllers.graphql.v1.tenant.institutions;

import com.aura.app.annotations.ProtectedOperation;
import com.aura.app.codegen.types.InstitutionResponse;
import com.aura.app.codegen.types.OutletResponse;
import com.aura.app.mappers.InstitutionMapper;
import com.aura.app.mappers.OutletMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.usecases.institutions.InstitutionQueryUseCase;
import com.aura.core.usecases.outlets.OutletQueryUseCase;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
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
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InstitutionsGqlController {
    private final InstitutionQueryUseCase institutionQueryUseCase;
    private final OutletQueryUseCase outletQueryUseCase;

    @ProtectedOperation(resource = "institution", scope = "view", resourceIdParam = "institutionId")
    @QueryMapping
    public InstitutionResponse getInstitution(
            @Argument("institutionId") String institutionId,
            @ContextValue(name = "user") User user) {
        return Optional.ofNullable(institutionQueryUseCase.findById(user.getCurrentTenantAccessed()))
                .map(InstitutionMapper.INSTANCE::from)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("Institution"));
    }

    @ProtectedOperation(resource = "institution", scope = "view", resourceIdParam = "institutionId")
    @QueryMapping
    public List<InstitutionResponse> getUserInstitutions(
            @Argument("institutionId") String institutionId,
            @ContextValue(name = "user") User user) {
        return institutionQueryUseCase.findAllByIds(user.getTenantIds()).stream()
                .map(InstitutionMapper.INSTANCE::from)
                .toList();
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
