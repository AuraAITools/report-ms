package com.aura.core.repositories;

import com.aura.core.entities.institutions.Institution;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import java.util.List;
import java.util.Optional;

public interface InstitutionRepository {
    Institution save(Institution institution);

    Optional<Institution> findById(String id);

    List<Institution> findAll();

    PagedResult<Institution> findAllBy(KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria);

    PagedResult<Institution> findAllByTenantId(String tenantId, KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria);
}
