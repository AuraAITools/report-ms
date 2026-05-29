package com.aura.app.controllers.graphql.v1.levels;

import com.aura.app.codegen.types.CreateLevelInInstitutionInput;
import com.aura.app.codegen.types.InstitutionSearchInput;
import com.aura.app.codegen.types.LevelResponse;
import com.aura.app.codegen.types.SortInput;
import com.aura.app.dto.PageParametersDto;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.app.mappers.LevelMapper;
import com.aura.app.utils.PaginationUtils;
import com.aura.core.auth.User;
import com.aura.core.entities.levels.Level;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.usecases.levels.LevelQueryUseCase;
import com.aura.core.utils.PagedResult;
import com.aura.details.services.LevelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.relay.Connection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.pagination.CursorStrategy;
import org.springframework.graphql.data.query.ScrollSubrange;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;


import static com.aura.app.codegen.DgsConstants.QUERY.GetLevelByPage;
import static com.aura.app.utils.PaginationUtils.buildConnection;
import static com.aura.app.utils.PaginationUtils.mapToSearchFilters;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LevelsGqlController {
    private final LevelQueryUseCase levelQueryUseCase;
    private final LevelService levelService;
    private final LevelRepository levelRepository;
    private final ObjectMapper objectMapper;
    private final CursorStrategy<ScrollPosition> cursorStrategy;

    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    @QueryMapping(name = GetLevelByPage)
    public Connection<LevelResponse> getLevelByPage(
            @Argument("institutionId") String institutionId,
            @Argument(name = "search") InstitutionSearchInput searchInputs,
            @Argument(name = "sort") SortInput sortInput,
            // ScrollSubrange auto-parses first, last, before, after arguments
            ScrollSubrange scrollSubrange,
            @ContextValue(name = "user") User user
    ) {
        PageParametersDto pageParametersDto = PaginationUtils.getPageParametersDto(searchInputs, sortInput, scrollSubrange, (searchInputParameters) -> mapToSearchFilters(searchInputParameters, objectMapper));
        PagedResult<Level> pagedResult = levelQueryUseCase.queryPageBy(institutionId, pageParametersDto.keysetPaginationCriteria(), pageParametersDto.sortCriteriasList(), pageParametersDto.searchCriteria());

        return buildConnection(pagedResult, pageParametersDto.sortCriteriasList(), LevelMapper.INSTANCE::mapToDto, (keyset) -> cursorStrategy.toCursor(ScrollPosition.forward(keyset)));
    }

    @PreAuthorize("hasPermission(#id, 'level', 'delete')")
    @MutationMapping
    public Boolean deleteLevelById(@Argument("id") String id, @ContextValue(name = "user") User user) {
        if (levelRepository.findById(id) == null) {
            throw new ResourceNotFoundBusinessException("Level");
        }
        levelService.deleteById(id);
        return true;
    }

    @PreAuthorize("hasPermission(#input.institutionId, 'institution', 'create_level')")
    @MutationMapping
    public LevelResponse createLevelInInstitution(@Argument("input") CreateLevelInInstitutionInput input,
                                                  @ContextValue(name = "user") User user) {
        Level level = LevelMapper.INSTANCE.toDomainEntity(input);
        Level created = levelService.createLevelInInstitution(level);
        return LevelMapper.INSTANCE.mapToDto(created);
    }

    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    @QueryMapping
    public LevelResponse getLevelByIdInInstitution(@Argument("levelId") String levelId, @Argument("institutionId") String institutionId,
                                                   @ContextValue(name = "user") User user) {
        return Optional.ofNullable(levelQueryUseCase.findById(levelId))
                .map(LevelMapper.INSTANCE::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("Level"));
    }

    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    @QueryMapping
    public List<LevelResponse> getLevelsInInstitution(@Argument("institutionId") String institutionId,
                                                      @ContextValue(name = "user") User user) {
        return levelQueryUseCase.findAllByTenantId(institutionId).stream()
                .map(LevelMapper.INSTANCE::mapToDto)
                .toList();
    }
}
