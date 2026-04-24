package com.aura.app.controllers.graphql.v1.outlets;

import com.aura.app.annotations.ProtectedOperation;
import com.aura.app.codegen.DgsConstants;
import com.aura.app.codegen.types.CreateOutletInput;
import com.aura.app.codegen.types.OutletResponse;
import com.aura.app.codegen.types.OutletSearchInput;
import com.aura.app.codegen.types.SortInput;
import com.aura.app.dto.PageParametersDto;
import com.aura.app.mappers.OutletMapper;
import com.aura.app.utils.PaginationUtils;
import com.aura.core.auth.User;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.utils.PagedResult;
import com.aura.details.services.OutletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.relay.Connection;
import lombok.RequiredArgsConstructor;
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

@Controller
@RequiredArgsConstructor
public class OutletsGqlController {
    private final OutletService outletService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CursorStrategy<ScrollPosition> cursorStrategy;

    @ProtectedOperation(resource = "outlet", scope = "create")
    @MutationMapping(CreateOutlet)
    public OutletResponse createOutlet(@Argument(name = CreateOutletInput) CreateOutletInput createOutletInput, @ContextValue(name = "user") User user) {
        Outlet outletToBeCreated = OutletMapper.INSTANCE.toDomainEntity(createOutletInput);
        Outlet outlet = outletService.createOutlet(outletToBeCreated);
        return OutletMapper.INSTANCE.toDto(outlet);
    }

    @ProtectedOperation(resource = "outlet", scope = "view", resourceIdParam = "institutionId")
    @QueryMapping(DgsConstants.QUERY.GetOutletsByPage)
    public Connection<OutletResponse> getOutletsByPage(
            @Argument("institutionId") String institutionId,
            @Argument(name = "search") OutletSearchInput searchInputs,
            @Argument(name = "sort") SortInput sortInput,
            ScrollSubrange scrollSubrange,
            @ContextValue(name = "user") User user) {

        PageParametersDto pageParameters = getPageParametersDto(searchInputs, sortInput, scrollSubrange, searchInputParameters -> mapToSearchFilters(searchInputParameters, objectMapper));

        PagedResult<Outlet> pagedResult = outletService.queryPageBy(institutionId, pageParameters.keysetPaginationCriteria(), pageParameters.sortCriteriasList(), pageParameters.searchCriteria());
        return PaginationUtils.buildConnection(pagedResult, pageParameters.sortCriteriasList(), OutletMapper.INSTANCE::toDto, keyset -> cursorStrategy.toCursor(ScrollPosition.forward(keyset)));

    }
}
