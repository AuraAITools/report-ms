package com.aura.core.usecases.institutions;

import com.aura.core.entities.institutions.Institution;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.repositories.OutletRepository;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.jspecify.annotations.Nullable;

public class InstitutionQueryUseCaseImpl implements InstitutionQueryUseCase {
    private final InstitutionRepository institutionRepository;
    private final OutletRepository outletRepository;

    public InstitutionQueryUseCaseImpl(InstitutionRepository institutionRepository, OutletRepository outletRepository) {
        this.institutionRepository = institutionRepository;
        this.outletRepository = outletRepository;
    }

    @Override
    public @Nullable Institution findById(String id) {
        Institution institution = institutionRepository.findById(id);
        if (institution != null) {
            List<Outlet> outlets = outletRepository.findAllByTenantIds(Set.of(institution.getId()));
            institution.setOutlets(outlets);
        }
        return institution;
    }

    @Override
    public List<Institution> findAllByIds(Set<String> ids) {
        List<Institution> institutions = institutionRepository.findAllByIds(ids);

        Map<String, List<Outlet>> outletsByTenantId = outletRepository.findAllByTenantIds(ids).stream()
                .collect(Collectors.groupingBy(Outlet::getTenantId));

        institutions.forEach(institution ->
                institution.setOutlets(outletsByTenantId.getOrDefault(institution.getId(), List.of()))
        );

        return institutions;
    }

    @Override
    public PagedResult<Institution> queryPageBy(String tenantId, KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        return institutionRepository.findAllByTenantId(tenantId, keysetPaginationCriteria, orderedSortCriterias, searchCriteria);
    }
}
