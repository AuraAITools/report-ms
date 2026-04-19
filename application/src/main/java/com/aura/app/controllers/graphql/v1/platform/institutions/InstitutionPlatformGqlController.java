package com.aura.app.controllers.graphql.v1.platform.institutions;

import com.aura.app.annotations.ProtectedOperation;
import com.aura.app.codegen.types.CreateInstitutionInput;
import com.aura.app.codegen.types.InstitutionResponse;
import com.aura.app.mappers.InstitutionMapper;
import com.aura.core.entities.institutions.Institution;
import com.aura.details.services.OnboardingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InstitutionPlatformGqlController {
    private final OnboardingService onboardingService;

    @ProtectedOperation(resource = "institution", scope = "create")
    @MutationMapping
    public InstitutionResponse createInstitution(
            @Argument CreateInstitutionInput createInstitutionInput) {
        Institution institution = InstitutionMapper.INSTANCE.from(createInstitutionInput);
        Institution onboardedInstitution = onboardingService.onboard(institution);
        return InstitutionMapper.INSTANCE.from(onboardedInstitution);
    }
}
