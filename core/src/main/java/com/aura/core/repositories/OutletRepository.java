package com.aura.core.repositories;

import com.aura.core.entities.outlets.Outlet;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;

public interface OutletRepository {
    @Nullable Outlet findById(String id);

    Outlet save(Outlet outlet);

    List<Outlet> findAllByTenantIds(Set<String> tenantIds);

    PagedResult<Outlet> queryPageBy(KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria);

    PagedResult<Outlet> queryPageBy(String tenantId, KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria);

}
