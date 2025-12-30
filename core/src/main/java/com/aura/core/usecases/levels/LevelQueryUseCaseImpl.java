package com.aura.core.usecases.levels;

import com.aura.core.entities.levels.Level;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import java.util.List;

public class LevelQueryUseCaseImpl implements LevelQueryUseCase {
    private final LevelRepository levelRepository;

    public LevelQueryUseCaseImpl(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }

    @Override
    public PagedResult<Level> queryPageBy(String tenantId, KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        return levelRepository.findAllBy(tenantId, keysetPaginationCriteria, orderedSortCriterias, searchCriteria);
    }

    @Override
    public PagedResult<Level> queryPageBy(KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        return levelRepository.findAllBy(keysetPaginationCriteria, orderedSortCriterias, searchCriteria);
    }
}
