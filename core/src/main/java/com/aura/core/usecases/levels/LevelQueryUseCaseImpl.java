package com.aura.core.usecases.levels;

import com.aura.core.entities.levels.Level;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LevelQueryUseCaseImpl implements LevelQueryUseCase {
    private final LevelRepository levelRepository;

    @Override
    public PagedResult<Level> queryPageBy(String tenantId, KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        return levelRepository.findAllBy(tenantId, keysetPaginationCriteria, orderedSortCriterias, searchCriteria);
    }

    @Override
    public @Nullable Level findById(String levelId) {
        return levelRepository.findById(levelId);
    }

    @Override
    public List<Level> findAllByTenantId(String tenantId) {
        return levelRepository.findAllByTenantId(tenantId);
    }
}
