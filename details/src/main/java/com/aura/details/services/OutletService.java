package com.aura.details.services;

import com.aura.core.clients.ResourceAuthorizationService;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.usecases.outlets.CreateOutletUseCase;
import com.aura.core.usecases.outlets.OutletQueryUseCase;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OutletService {
    private static final String OUTLET_RESOURCE_TYPE = "urn:aura:outlet";

    private final CreateOutletUseCase createOutletUseCase;
    private final OutletQueryUseCase outletQueryUseCase;
    private final ResourceAuthorizationService resourceAuthorizationService;

    public Outlet createOutlet(Outlet outlet) {
        Outlet created = createOutletUseCase.createOutlet(outlet);
        resourceAuthorizationService.registerResource(
                "outlet:" + created.getId(),
                OUTLET_RESOURCE_TYPE,
                null,
                Set.of("view", "manage", "delegate")
        );
        return created;
    }

    public PagedResult<Outlet> queryPageBy(KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        return outletQueryUseCase.queryPageBy(keysetPaginationCriteria, orderedSortCriterias, searchCriteria);
    }


    public PagedResult<Outlet> queryPageBy(String tenantId, KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        return outletQueryUseCase.queryPageBy(tenantId, keysetPaginationCriteria, orderedSortCriterias, searchCriteria);
    }
}
