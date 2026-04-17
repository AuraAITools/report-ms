package com.aura.app.controllers.graphql.v1.institutions;

import com.aura.app.annotations.ProtectedOperation;
import com.aura.app.codegen.types.CreateInstitutionInput;
import com.aura.app.codegen.types.InstitutionResponse;
import com.aura.app.codegen.types.InstitutionSearchInput;
import com.aura.app.codegen.types.SortInput;
import com.aura.app.dto.PageParametersDto;
import com.aura.app.mappers.InstitutionMapper;
import com.aura.app.utils.PaginationUtils;
import com.aura.core.auth.User;
import com.aura.core.entities.institutions.Institution;
import com.aura.core.usecases.institutions.InstitutionQueryUseCase;
import com.aura.core.utils.PagedResult;
import com.aura.details.services.OnboardingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.relay.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.pagination.CursorStrategy;
import org.springframework.graphql.data.query.ScrollSubrange;
import org.springframework.stereotype.Controller;


import static com.aura.app.codegen.DgsConstants.MUTATION.CREATEINSTITUTION_INPUT_ARGUMENT.CreateInstitutionInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateInstitution;
import static com.aura.app.codegen.DgsConstants.QUERY.GetInstitutionByPage;
import static com.aura.app.utils.PaginationUtils.getPageParametersDto;
import static com.aura.app.utils.PaginationUtils.mapToSearchFilters;

@Slf4j
@Controller
@RequiredArgsConstructor
public class InstitutionsGqlController {
    private final OnboardingService onboardingService;
    private final InstitutionQueryUseCase institutionQueryUseCase;
    private final CursorStrategy<ScrollPosition> cursorStrategy;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ProtectedOperation(resource = "institution", scope = "create")
    @MutationMapping(name = CreateInstitution)
    public InstitutionResponse createInstitution(@Argument(value = CreateInstitutionInput) CreateInstitutionInput createInstitutionInput, @ContextValue(name = "user") User user) {
        Institution institution = InstitutionMapper.INSTANCE.from(createInstitutionInput);
        Institution onboardedInstitution = onboardingService.onboard(institution);
        return InstitutionMapper.INSTANCE.from(onboardedInstitution);
    }

    @ProtectedOperation(resource = "institution", scope = "view")
    @QueryMapping(name = GetInstitutionByPage)
    public Connection<InstitutionResponse> getInstitutionByPage(
            @Argument(name = "search") InstitutionSearchInput searchInputs,
            @Argument(name = "sort") SortInput sortInput,
            // ScrollSubrange auto-parses first, last, before, after arguments
            ScrollSubrange scrollSubrange,
            @ContextValue(name = "user") User user) {

        PageParametersDto pageParameters = getPageParametersDto(searchInputs, sortInput, scrollSubrange, searchInputParameters -> mapToSearchFilters(searchInputParameters, objectMapper));

        if ("*".equals(user.getCurrentTenantAccessed())) {
            PagedResult<Institution> pagedResult = institutionQueryUseCase.queryPageBy(pageParameters.keysetPaginationCriteria(), pageParameters.sortCriteriasList(), pageParameters.searchCriteria());
            return PaginationUtils.buildConnection(pagedResult, pageParameters.sortCriteriasList(), InstitutionMapper.INSTANCE::from, keyset -> cursorStrategy.toCursor(ScrollPosition.forward(keyset)));
        }

        PagedResult<Institution> pagedResult = institutionQueryUseCase.queryPageBy(user.getCurrentTenantAccessed(), pageParameters.keysetPaginationCriteria(), pageParameters.sortCriteriasList(), pageParameters.searchCriteria());
        return PaginationUtils.buildConnection(pagedResult, pageParameters.sortCriteriasList(), InstitutionMapper.INSTANCE::from, keyset -> cursorStrategy.toCursor(ScrollPosition.forward(keyset)));
    }
}
