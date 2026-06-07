package com.aura.app.controllers.graphql.v1.institutions;

import com.aura.app.codegen.types.InstitutionResponse;
import com.aura.app.mappers.InstitutionMapper;
import com.aura.core.entities.institutions.Institution;
import com.aura.details.services.InstitutionCreationRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OnboardInstitutionFromRequestGqlController {

    private final InstitutionCreationRequestService institutionCreationRequestService;

    @PreAuthorize("hasPermission('aura', 'system', 'create_institution')")
    @MutationMapping
    public InstitutionResponse onboardInstitutionFromRequest(
            @Argument String institutionCreationRequestId) {
        Institution institution = institutionCreationRequestService.onboardFromRequest(institutionCreationRequestId);
        return InstitutionMapper.INSTANCE.from(institution);
    }
}
