package com.aura.core.usecases.onboarding;

import com.aura.core.entities.institutions.Institution;
import com.aura.core.repositories.InstitutionRepository;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Default institution onboarding strategy")
class DefaultInstitutionOnboardingStrategyTest {

    @DisplayName("Should save institution with matching id and tenantId and load initial data")
    @Test
    void onboard_savesInstitutionAndLoadsInitialData() {
        var loadedTenants = new java.util.ArrayList<String>();

        InstitutionRepository repo = new InstitutionRepository() {
            @Override
            public Institution save(Institution institution) {
                return institution;
            }

            @Override
            public Institution findById(String id) {
                return null;
            }

            @Override
            public List<Institution> findAll() {
                return List.of();
            }

            @Override
            public List<Institution> findAllByIds(Set<String> ids) {
                return List.of();
            }

            @Override
            public com.aura.core.utils.PagedResult<Institution> findAllBy(com.aura.core.utils.KeysetPaginationCriteria k, List<com.aura.core.utils.SortCriteria> s, com.aura.core.utils.SearchCriteria sc) {
                return null;
            }

            @Override
            public com.aura.core.utils.PagedResult<Institution> findAllByTenantId(String t, com.aura.core.utils.KeysetPaginationCriteria k, List<com.aura.core.utils.SortCriteria> s, com.aura.core.utils.SearchCriteria sc) {
                return null;
            }
        };
        TenantInitialDataLoader loader = loadedTenants::add;

        Institution institution = new Institution();
        var strategy = new DefaultInstitutionOnboardingStrategy(institution, repo, loader);

        Institution result = strategy.onboard();

        assertNotNull(result.getId());
        assertEquals(result.getId(), result.getTenantId());
        assertEquals(List.of(result.getId()), loadedTenants);
    }
}
