package com.aura.core.services.levels;

import com.aura.core.entities.levels.Level;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.services.institutions.onboarding.LevelQueryService;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import java.util.List;

public class LevelQueryServiceImpl implements LevelQueryService {
    private final LevelRepository levelRepository;

    public LevelQueryServiceImpl(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }

    @Override
    public PagedResult<Level> queryPageBy(String tenantId, KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        return levelRepository.findAllBy(tenantId, keysetPaginationCriteria, orderedSortCriterias, searchCriteria);
    }
}
