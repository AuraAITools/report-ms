package com.aura.app.controllers.graphql.v1.institutions;

import com.aura.app.codegen.DgsConstants;
import com.aura.app.codegen.types.CreateInstitutionInput;
import com.aura.app.codegen.types.InstitutionResponse;
import com.aura.app.codegen.types.InstitutionSearchInput;
import com.aura.app.codegen.types.SortInput;
import com.aura.app.controllers.graphql.v1.institutions.exceptions.AccessDeniedException;
import com.aura.app.controllers.graphql.v1.institutions.mappers.InstitutionMapper;
import com.aura.app.dto.PageParametersDto;
import com.aura.app.utils.PaginationUtils;
import com.aura.core.auth.User;
import com.aura.core.commons.QueryService;
import com.aura.core.entities.institutions.Institution;
import com.aura.core.services.institutions.OnboardingInstitutionService;
import com.aura.core.utils.PagedResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.relay.Connection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.pagination.CursorStrategy;
import org.springframework.graphql.data.query.ScrollSubrange;
import org.springframework.stereotype.Controller;


import static com.aura.app.utils.PaginationUtils.getPageParametersDto;
import static com.aura.app.utils.PaginationUtils.mapToSearchFilters;
import static com.aura.core.auth.RoleTypes.AURA_ADMIN;
import static com.aura.core.utils.AuthUtils.hasAnyTechnicalUserRole;

@Slf4j
@Controller
public class InstitutionsGqlController {
    private final OnboardingInstitutionService onboardingInstitutionService;
    private final QueryService<Institution> institutionQueryService;
    private final CursorStrategy<ScrollPosition> cursorStrategy;
    private final ObjectMapper objectMapper;

    public InstitutionsGqlController(OnboardingInstitutionService onboardingInstitutionService, QueryService<Institution> institutionQueryService, CursorStrategy<ScrollPosition> cursorStrategy) {
        this.onboardingInstitutionService = onboardingInstitutionService;
        this.institutionQueryService = institutionQueryService;
        this.cursorStrategy = cursorStrategy;
        this.objectMapper = new ObjectMapper();
    }

    @MutationMapping(name = DgsConstants.MUTATION.CreateInstitution)
    public InstitutionResponse createInstitution(@Argument(value = DgsConstants.MUTATION.CREATEINSTITUTION_INPUT_ARGUMENT.CreateInstitutionInput) CreateInstitutionInput createInstitutionInput, @ContextValue(name = "user") User user) {
        if (!user.isTechnicalUser()) throw new AccessDeniedException("not authorized to create institution");
        Institution institution = InstitutionMapper.INSTANCE.from(createInstitutionInput);
        Institution onboardedInstitution = onboardingInstitutionService.onboard(institution);
        return InstitutionMapper.INSTANCE.from(onboardedInstitution);
    }

    @QueryMapping(name = DgsConstants.QUERY.GetInstitutionByPage)
    public Connection<InstitutionResponse> getInstitutionByPage(
            @Argument(name = "search") InstitutionSearchInput searchInputs,
            @Argument(name = "sort") SortInput sortInput,
            // ScrollSubrange auto-parses first, last, before, after arguments
            ScrollSubrange scrollSubrange,
            @ContextValue(name = "user") User user) {
        if (!hasAnyTechnicalUserRole(user, AURA_ADMIN))
            throw new AccessDeniedException("not authorized to get institution");
        // use keyset cursor pagination
        PageParametersDto pageParameters = getPageParametersDto(searchInputs, sortInput, scrollSubrange, (searchInputParameters) -> mapToSearchFilters(searchInputParameters, objectMapper));

        PagedResult<Institution> pagedResult = institutionQueryService.queryPageBy(pageParameters.keysetPaginationCriteria(), pageParameters.sortCriteriasList(), pageParameters.searchCriteria());

        return PaginationUtils.buildConnection(pagedResult, pageParameters.sortCriteriasList(), InstitutionMapper.INSTANCE::from, (keyset) -> cursorStrategy.toCursor(ScrollPosition.forward(keyset)));
    }
}
