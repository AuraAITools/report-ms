package com.aura.app.controllers.graphql.v1.institutions;

import com.aura.app.codegen.DgsConstants;
import com.aura.app.codegen.types.CreateInstitutionInput;
import com.aura.app.codegen.types.InstitutionResponse;
import com.aura.app.codegen.types.InstitutionSearchInput;
import com.aura.app.codegen.types.SortInput;
import com.aura.app.controllers.graphql.v1.institutions.exceptions.AccessDeniedException;
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
import static com.aura.app.utils.PaginationUtils.getPageParametersDto;
import static com.aura.app.utils.PaginationUtils.mapToSearchFilters;
import static com.aura.core.auth.RoleTypes.AURA_ADMIN;
import static com.aura.core.auth.RoleTypes.INSTITUTION_ADMIN;
import static com.aura.core.utils.AuthUtils.hasAnyRoles;
import static com.aura.core.utils.AuthUtils.hasAnyTechnicalUserRole;
import static com.aura.core.utils.AuthUtils.isAggregateQuery;

@Slf4j
@Controller
public class InstitutionsGqlController {
    private final OnboardingService onboardingService;
    private final InstitutionQueryUseCase institutionQueryUseCase;
    private final CursorStrategy<ScrollPosition> cursorStrategy;
    private final ObjectMapper objectMapper;

    public InstitutionsGqlController(OnboardingService onboardingService, InstitutionQueryUseCase institutionQueryUseCase, CursorStrategy<ScrollPosition> cursorStrategy) {
        this.onboardingService = onboardingService;
        this.institutionQueryUseCase = institutionQueryUseCase;
        this.cursorStrategy = cursorStrategy;
        this.objectMapper = new ObjectMapper();
    }

    // Available to: aura admin
    @MutationMapping(name = DgsConstants.MUTATION.CreateInstitution)
    public InstitutionResponse createInstitution(@Argument(value = CreateInstitutionInput) CreateInstitutionInput createInstitutionInput, @ContextValue(name = "user") User user) {
        if (!isAggregateQuery(user) || !hasAnyTechnicalUserRole(user, AURA_ADMIN))
            throw new AccessDeniedException("Access denied");
        Institution institution = InstitutionMapper.INSTANCE.from(createInstitutionInput);
        Institution onboardedInstitution = onboardingService.onboard(institution);
        return InstitutionMapper.INSTANCE.from(onboardedInstitution);
    }

    // Available to: aura-admin & tenant
    @QueryMapping(name = DgsConstants.QUERY.GetInstitutionByPage)
    public Connection<InstitutionResponse> getInstitutionByPage(
            @Argument(name = "search") InstitutionSearchInput searchInputs,
            @Argument(name = "sort") SortInput sortInput,
            // ScrollSubrange auto-parses first, last, before, after arguments
            ScrollSubrange scrollSubrange,
            @ContextValue(name = "user") User user) {

        PageParametersDto pageParameters = getPageParametersDto(searchInputs, sortInput, scrollSubrange, searchInputParameters -> mapToSearchFilters(searchInputParameters, objectMapper));

        if (isAggregateQuery(user)) {
            if (!hasAnyTechnicalUserRole(user, AURA_ADMIN))
                throw new AccessDeniedException("not authorized for aggregate query");
            PagedResult<Institution> pagedResult = institutionQueryUseCase.queryPageBy(pageParameters.keysetPaginationCriteria(), pageParameters.sortCriteriasList(), pageParameters.searchCriteria());
            return PaginationUtils.buildConnection(pagedResult, pageParameters.sortCriteriasList(), InstitutionMapper.INSTANCE::from, keyset -> cursorStrategy.toCursor(ScrollPosition.forward(keyset)));
        }

        if (!hasAnyTechnicalUserRole(user, AURA_ADMIN) && !hasAnyRoles(user, INSTITUTION_ADMIN))
            throw new AccessDeniedException("not authorized for institution");
        PagedResult<Institution> pagedResult = institutionQueryUseCase.queryPageBy(user.getCurrentTenantAccessed(), pageParameters.keysetPaginationCriteria(), pageParameters.sortCriteriasList(), pageParameters.searchCriteria());
        return PaginationUtils.buildConnection(pagedResult, pageParameters.sortCriteriasList(), InstitutionMapper.INSTANCE::from, keyset -> cursorStrategy.toCursor(ScrollPosition.forward(keyset)));
    }
}
