package com.aura.app.controllers.graphql.v1.institutions;

import com.aura.app.codegen.types.InstitutionCreationRequestResponse;
import com.aura.app.codegen.types.RequestInstitutionCreationInput;
import com.aura.app.mappers.InstitutionCreationRequestMapper;
import com.aura.core.entities.institutions.InstitutionCreationRequest;
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
public class InstitutionCreationRequestGqlController {

    private final InstitutionCreationRequestService institutionCreationRequestService;

    @MutationMapping
    public InstitutionCreationRequestResponse requestInstitutionCreation(
            @Argument RequestInstitutionCreationInput input) {
        InstitutionCreationRequest request = InstitutionCreationRequestMapper.INSTANCE.from(input);
        InstitutionCreationRequest saved = institutionCreationRequestService.request(request);
        return InstitutionCreationRequestMapper.INSTANCE.from(saved);
    }
}
