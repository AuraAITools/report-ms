package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.usecases.outlets.CreateOutletUseCase;
import com.aura.core.usecases.outlets.OutletQueryUseCase;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OutletService {

    private final CreateOutletUseCase createOutletUseCase;
    private final OutletQueryUseCase outletQueryUseCase;
    private final RelationshipAuthorizationService relationshipService;

    public Outlet createOutlet(Outlet outlet) {
        Outlet created = createOutletUseCase.createOutlet(outlet);
        relationshipService.writeRelationship("outlet", created.getId(),
                "parent_institution", "institution", created.getTenantId());
        return created;
    }

    public PagedResult<Outlet> queryPageBy(String tenantId, KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        return outletQueryUseCase.queryPageBy(tenantId, keysetPaginationCriteria, orderedSortCriterias, searchCriteria);
    }
}
