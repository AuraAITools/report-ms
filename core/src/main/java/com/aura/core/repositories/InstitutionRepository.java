package com.aura.core.repositories;

import com.aura.core.entities.institutions.Institution;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;

public interface InstitutionRepository {
    Institution save(Institution institution);

    @Nullable Institution findById(String id);

    /**
     * Targeted single-column update for the profile image S3 key. Avoids the
     * domain-mapper round-trip used by {@link #save(Institution)}, which would
     * detach related entities (e.g. outlets, accounts) on merge. Pass null to
     * clear the key.
     */
    void updateProfileImageKeyById(String id, @Nullable String profileImageKey);

    List<Institution> findAll();

    List<Institution> findAllByIds(Set<String> ids);

    PagedResult<Institution> findAllBy(KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria);

    PagedResult<Institution> findAllByTenantId(String tenantId, KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria);
}
