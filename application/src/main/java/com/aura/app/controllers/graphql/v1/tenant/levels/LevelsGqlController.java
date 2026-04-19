package com.aura.app.controllers.graphql.v1.tenant.levels;

import com.aura.app.annotations.ProtectedOperation;
import com.aura.app.codegen.types.InstitutionSearchInput;
import com.aura.app.codegen.types.LevelResponse;
import com.aura.app.codegen.types.SortInput;
import com.aura.app.dto.PageParametersDto;
import com.aura.app.mappers.LevelMapper;
import com.aura.app.utils.PaginationUtils;
import com.aura.core.auth.User;
import com.aura.core.entities.levels.Level;
import com.aura.core.usecases.levels.LevelQueryUseCase;
import com.aura.core.utils.PagedResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.relay.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.pagination.CursorStrategy;
import org.springframework.graphql.data.query.ScrollSubrange;
import org.springframework.stereotype.Controller;


import static com.aura.app.codegen.DgsConstants.QUERY.GetLevelByPage;
import static com.aura.app.utils.PaginationUtils.buildConnection;
import static com.aura.app.utils.PaginationUtils.mapToSearchFilters;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LevelsGqlController {
    private final LevelQueryUseCase levelQueryUseCase;
    private final ObjectMapper objectMapper;
    private final CursorStrategy<ScrollPosition> cursorStrategy;

    @ProtectedOperation(resource = "level", scope = "view")
    @QueryMapping(name = GetLevelByPage)
    public Connection<LevelResponse> getLevelByPage(
            @Argument(name = "search") InstitutionSearchInput searchInputs,
            @Argument(name = "sort") SortInput sortInput,
            // ScrollSubrange auto-parses first, last, before, after arguments
            ScrollSubrange scrollSubrange,
            @ContextValue(name = "user") User user
    ) {
        PageParametersDto pageParametersDto = PaginationUtils.getPageParametersDto(searchInputs, sortInput, scrollSubrange, (searchInputParameters) -> mapToSearchFilters(searchInputParameters, objectMapper));
        PagedResult<Level> pagedResult = levelQueryUseCase.queryPageBy(user.getCurrentTenantAccessed(), pageParametersDto.keysetPaginationCriteria(), pageParametersDto.sortCriteriasList(), pageParametersDto.searchCriteria());

        return buildConnection(pagedResult, pageParametersDto.sortCriteriasList(), LevelMapper.INSTANCE::mapToDto, (keyset) -> cursorStrategy.toCursor(ScrollPosition.forward(keyset)));
    }
}
