package com.aura.details.services;

import com.aura.core.entities.outlets.Outlet;
import com.aura.core.usecases.outlets.CreateOutletUseCase;
import com.aura.core.usecases.outlets.OutletQueryUseCase;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import java.util.List;

public class OutletService {
    private final CreateOutletUseCase createOutletUseCase;

    private final OutletQueryUseCase outletQueryUseCase;

    public OutletService(CreateOutletUseCase createOutletUseCase, OutletQueryUseCase outletQueryUseCase) {
        this.createOutletUseCase = createOutletUseCase;
        this.outletQueryUseCase = outletQueryUseCase;
    }

    public Outlet createOutlet(Outlet outlet) {
        return createOutletUseCase.createOutlet(outlet);
    }

    public PagedResult<Outlet> queryPageBy(KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        return outletQueryUseCase.queryPageBy(keysetPaginationCriteria, orderedSortCriterias, searchCriteria);
    }


    public PagedResult<Outlet> queryPageBy(String tenantId, KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        return outletQueryUseCase.queryPageBy(tenantId, keysetPaginationCriteria, orderedSortCriterias, searchCriteria);
    }
}
