package com.aura.app.controllers.graphql.v1.outlets;

import com.aura.app.codegen.DgsConstants;
import com.aura.app.codegen.types.CreateOutletInput;
import com.aura.app.codegen.types.OutletResponse;
import com.aura.app.codegen.types.OutletSearchInput;
import com.aura.app.codegen.types.SortInput;
import com.aura.app.controllers.graphql.v1.institutions.exceptions.AccessDeniedException;
import com.aura.app.dto.PageParametersDto;
import com.aura.app.mappers.OutletMapper;
import com.aura.app.utils.PaginationUtils;
import com.aura.core.auth.User;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.utils.PagedResult;
import com.aura.details.services.OutletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.relay.Connection;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.pagination.CursorStrategy;
import org.springframework.graphql.data.query.ScrollSubrange;
import org.springframework.stereotype.Controller;


import static com.aura.app.codegen.DgsConstants.MUTATION.CREATEOUTLET_INPUT_ARGUMENT.CreateOutletInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateOutlet;
import static com.aura.app.utils.PaginationUtils.getPageParametersDto;
import static com.aura.app.utils.PaginationUtils.mapToSearchFilters;
import static com.aura.core.usecases.onboarding.RoleTypes.AURA_ADMIN_ROLE;
import static com.aura.core.usecases.onboarding.RoleTypes.INSTITUTION_ADMIN_ROLE;
import static com.aura.core.utils.AuthUtils.hasAnyRoles;
import static com.aura.core.utils.AuthUtils.hasAnyTechnicalUserRole;
import static com.aura.core.utils.AuthUtils.isAggregateQuery;

@Controller
public class OutletsGqlController {
    private final OutletService outletService;
    private final ObjectMapper objectMapper;
    private final CursorStrategy<ScrollPosition> cursorStrategy;

    public OutletsGqlController(OutletService outletService, CursorStrategy<ScrollPosition> cursorStrategy) {
        this.outletService = outletService;
        this.objectMapper = new ObjectMapper();
        this.cursorStrategy = cursorStrategy;
    }

    @MutationMapping(CreateOutlet)
    public OutletResponse createOutlet(@Argument(name = CreateOutletInput) CreateOutletInput createOutletInput, @ContextValue(name = "user") User user) {
        Outlet outletToBeCreated = OutletMapper.INSTANCE.toDomainEntity(createOutletInput, user.getCurrentTenantAccessed());
        Outlet outlet = outletService.createOutlet(outletToBeCreated);
        return OutletMapper.INSTANCE.toDto(outlet);
    }

    @QueryMapping(DgsConstants.QUERY.GetOutletsByPage)
    public Connection<OutletResponse> getOutletsByPage(
            @Argument(name = "search") OutletSearchInput searchInputs,
            @Argument(name = "sort") SortInput sortInput,
            ScrollSubrange scrollSubrange,
            @ContextValue(name = "user") User user) {

        PageParametersDto pageParameters = getPageParametersDto(searchInputs, sortInput, scrollSubrange, searchInputParameters -> mapToSearchFilters(searchInputParameters, objectMapper));

        if (isAggregateQuery(user)) {
            if (!hasAnyTechnicalUserRole(user, AURA_ADMIN_ROLE))
                throw new AccessDeniedException("not authorized for aggregate query");
            PagedResult<Outlet> pagedResult = outletService.queryPageBy(pageParameters.keysetPaginationCriteria(), pageParameters.sortCriteriasList(), pageParameters.searchCriteria());
            return PaginationUtils.buildConnection(pagedResult, pageParameters.sortCriteriasList(), OutletMapper.INSTANCE::toDto, keyset -> cursorStrategy.toCursor(ScrollPosition.forward(keyset)));
        }

        if (!hasAnyTechnicalUserRole(user, AURA_ADMIN_ROLE) && !hasAnyRoles(user, INSTITUTION_ADMIN_ROLE))
            throw new AccessDeniedException("not authorized for institution");
        PagedResult<Outlet> pagedResult = outletService.queryPageBy(user.getCurrentTenantAccessed(), pageParameters.keysetPaginationCriteria(), pageParameters.sortCriteriasList(), pageParameters.searchCriteria());
        return PaginationUtils.buildConnection(pagedResult, pageParameters.sortCriteriasList(), OutletMapper.INSTANCE::toDto, keyset -> cursorStrategy.toCursor(ScrollPosition.forward(keyset)));

    }
}
