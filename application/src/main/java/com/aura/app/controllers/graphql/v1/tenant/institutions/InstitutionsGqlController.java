package com.aura.app.controllers.graphql.v1.tenant.institutions;

import com.aura.app.annotations.ProtectedOperation;
import com.aura.app.codegen.types.InstitutionResponse;
import com.aura.app.mappers.InstitutionMapper;
import com.aura.core.auth.User;
import com.aura.core.usecases.institutions.InstitutionQueryUseCase;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InstitutionsGqlController {
    private final InstitutionQueryUseCase institutionQueryUseCase;

    @ProtectedOperation(resource = "institution", scope = "view", resourceIdParam = "$tenant")
    @QueryMapping
    public InstitutionResponse getInstitution(@ContextValue(name = "user") User user) {
        return Optional.ofNullable(institutionQueryUseCase.findById(user.getCurrentTenantAccessed()))
                .map(InstitutionMapper.INSTANCE::from)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("Institution"));
    }
}
