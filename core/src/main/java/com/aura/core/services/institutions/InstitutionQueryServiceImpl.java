package com.aura.core.services.institutions;

import com.aura.core.entities.institutions.Institution;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import java.util.List;

public class InstitutionQueryServiceImpl implements InstitutionQueryService {
    InstitutionRepository institutionRepository;

    public InstitutionQueryServiceImpl(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    @Override
    public PagedResult<Institution> queryPageBy(KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        return institutionRepository.findAllBy(keysetPaginationCriteria, orderedSortCriterias, searchCriteria);
    }
}
