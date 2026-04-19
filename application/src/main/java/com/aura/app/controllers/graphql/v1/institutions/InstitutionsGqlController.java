package com.aura.app.controllers.graphql.v1.institutions;

import com.aura.app.annotations.ProtectedOperation;
import com.aura.app.codegen.types.CreateInstitutionInput;
import com.aura.app.codegen.types.InstitutionResponse;
import com.aura.app.mappers.InstitutionMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.institutions.Institution;
import com.aura.core.usecases.institutions.InstitutionQueryUseCase;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.details.services.OnboardingService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import static com.aura.app.codegen.DgsConstants.MUTATION.CREATEINSTITUTION_INPUT_ARGUMENT.CreateInstitutionInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateInstitution;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InstitutionsGqlController {
    private final OnboardingService onboardingService;
    private final InstitutionQueryUseCase institutionQueryUseCase;

    @ProtectedOperation(resource = "institution", scope = "create")
    @MutationMapping(name = CreateInstitution)
    public InstitutionResponse createInstitution(@Argument(value = CreateInstitutionInput) CreateInstitutionInput createInstitutionInput, @ContextValue(name = "user") User user) {
        Institution institution = InstitutionMapper.INSTANCE.from(createInstitutionInput);
        Institution onboardedInstitution = onboardingService.onboard(institution);
        return InstitutionMapper.INSTANCE.from(onboardedInstitution);
    }

    @ProtectedOperation(resource = "institution", scope = "view", resourceIdParam = "$tenant")
    @QueryMapping
    public InstitutionResponse getInstitution(@ContextValue(name = "user") User user) {
        return Optional.ofNullable(institutionQueryUseCase.findById(user.getCurrentTenantAccessed()))
                .map(InstitutionMapper.INSTANCE::from)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("Institution"));
    }
}
