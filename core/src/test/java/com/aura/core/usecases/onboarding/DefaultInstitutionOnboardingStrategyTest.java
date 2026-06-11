package com.aura.core.usecases.onboarding;

import com.aura.core.entities.institutions.Institution;
import com.aura.core.exceptions.DuplicateResourceException;
import com.aura.core.repositories.InstitutionRepository;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Default institution onboarding strategy")
class DefaultInstitutionOnboardingStrategyTest {

    private static class StubInstitutionRepository implements InstitutionRepository {
        private final boolean nameExists;
        private final boolean emailExists;

        StubInstitutionRepository(boolean nameExists, boolean emailExists) {
            this.nameExists = nameExists;
            this.emailExists = emailExists;
        }

        @Override
        public Institution save(Institution institution) {
            return institution;
        }

        @Override
        public Institution findById(String id) {
            return null;
        }

        @Override
        public void updateProfileImageKeyById(String id, String profileImageKey) {
        }

        @Override
        public boolean existsByName(String name) {
            return nameExists;
        }

        @Override
        public boolean existsByEmail(String email) {
            return emailExists;
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
    }

    @DisplayName("Should save institution with matching id and tenantId and load initial data")
    @Test
    void onboard_savesInstitutionAndLoadsInitialData() {
        var loadedTenants = new java.util.ArrayList<String>();
        TenantInitialDataLoader loader = loadedTenants::add;

        Institution institution = new Institution();
        var strategy = new DefaultInstitutionOnboardingStrategy(
                institution, new StubInstitutionRepository(false, false), loader);

        Institution result = strategy.onboard();

        assertNotNull(result.getId());
        assertEquals(result.getId(), result.getTenantId());
        assertEquals(List.of(result.getId()), loadedTenants);
    }

    @DisplayName("Should throw DuplicateResourceException when an institution with the same name exists")
    @Test
    void onboard_throwsWhenNameExists() {
        Institution institution = new Institution();
        institution.setName("Acme Edu");
        var strategy = new DefaultInstitutionOnboardingStrategy(
                institution, new StubInstitutionRepository(true, false), tenantId -> {
                });

        var ex = assertThrows(DuplicateResourceException.class, strategy::onboard);
        assertTrue(ex.getMessage().contains("name"));
    }

    @DisplayName("Should throw DuplicateResourceException when an institution with the same email exists")
    @Test
    void onboard_throwsWhenEmailExists() {
        Institution institution = new Institution();
        institution.setEmail("acme@example.com");
        var strategy = new DefaultInstitutionOnboardingStrategy(
                institution, new StubInstitutionRepository(false, true), tenantId -> {
                });

        var ex = assertThrows(DuplicateResourceException.class, strategy::onboard);
        assertTrue(ex.getMessage().contains("email"));
    }
}
