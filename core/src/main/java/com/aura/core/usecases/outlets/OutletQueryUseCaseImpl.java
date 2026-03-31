package com.aura.core.usecases.outlets;

import com.aura.core.entities.outlets.Outlet;
import com.aura.core.repositories.OutletRepository;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OutletQueryUseCaseImpl implements OutletQueryUseCase {
    private final OutletRepository outletRepository;

    @Override
    public PagedResult<Outlet> queryPageBy(KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        return outletRepository.queryPageBy(keysetPaginationCriteria, orderedSortCriterias, searchCriteria);
    }

    @Override
    public PagedResult<Outlet> queryPageBy(String tenantId, KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        return outletRepository.queryPageBy(tenantId, keysetPaginationCriteria, orderedSortCriterias, searchCriteria);
    }
}
