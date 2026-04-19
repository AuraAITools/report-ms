package com.aura.details.services;

import com.aura.core.clients.GroupAuthorizationService;
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
    private final GroupAuthorizationService groupAuthorizationService;

    public Outlet createOutlet(Outlet outlet) {
        Outlet created = createOutletUseCase.createOutlet(outlet);

        String outletId = created.getId();
        String institutionId = created.getTenantId();
        String resourceName = "outlet:" + outletId;
        String outletGroupName = "outlet:" + outletId + ":admin";
        String outletPolicyName = "outlet-" + outletId + "-admin-policy";
        String permissionName = "outlet-" + outletId + "-permission";
        String institutionPolicyName = "institution-" + institutionId + "-admin-policy";

        // 1. Register UMA resource
        resourceAuthorizationService.registerResource(
                resourceName,
                OUTLET_RESOURCE_TYPE,
                null,
                Set.of("view", "manage", "delegate")
        );

        // 2. Create outlet admin group
        groupAuthorizationService.createGroup(outletGroupName);

        // 3. Create outlet admin group policy
        groupAuthorizationService.createGroupPolicy(outletPolicyName, outletGroupName);

        // 4. Create resource permission linking BOTH outlet admin AND institution admin policies
        groupAuthorizationService.createResourcePermission(
                permissionName,
                resourceName,
                Set.of("view", "manage", "delegate"),
                Set.of(outletPolicyName, institutionPolicyName)
        );

        return created;
    }

    public PagedResult<Outlet> queryPageBy(String tenantId, KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        return outletQueryUseCase.queryPageBy(tenantId, keysetPaginationCriteria, orderedSortCriterias, searchCriteria);
    }
}
