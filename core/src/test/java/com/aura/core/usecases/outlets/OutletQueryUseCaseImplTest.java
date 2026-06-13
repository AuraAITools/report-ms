package com.aura.core.usecases.outlets;

import com.aura.core.entities.outlets.Outlet;
import com.aura.core.repositories.OutletRepository;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Outlet query use case")
class OutletQueryUseCaseImplTest {

    @DisplayName("Should return all outlets for the given tenant id")
    @Test
    void findAllByTenantId_returnsOutlets() {
        Outlet o1 = Outlet.builder().id("o1").name("Bukit Timah").build();
        Outlet o2 = Outlet.builder().id("o2").name("Tampines").build();
        var stub = new StubOutletRepository();
        stub.findAllByTenantIdResult = List.of(o1, o2);

        assertThat(new OutletQueryUseCaseImpl(stub).findAllByTenantId("tenant-1"))
                .containsExactly(o1, o2);
        assertThat(stub.findAllByTenantIdArg).isEqualTo("tenant-1");
    }

    @DisplayName("Should return empty list when no outlets in tenant")
    @Test
    void findAllByTenantId_returnsEmpty() {
        var stub = new StubOutletRepository();
        stub.findAllByTenantIdResult = List.of();

        assertThat(new OutletQueryUseCaseImpl(stub).findAllByTenantId("tenant-1")).isEmpty();
    }

    private static class StubOutletRepository implements OutletRepository {
        List<Outlet> findAllByTenantIdResult = List.of();
        String findAllByTenantIdArg;

        @Override
        public @Nullable Outlet findById(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Outlet save(Outlet outlet) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean existsByNameAndTenantId(String name, String tenantId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Outlet> findAllByIds(List<String> ids) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Outlet> findAllByTenantId(String tenantId) {
            this.findAllByTenantIdArg = tenantId;
            return findAllByTenantIdResult;
        }

        @Override
        public List<Outlet> findAllByTenantIds(Set<String> tenantIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public PagedResult<Outlet> queryPageBy(KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
            throw new UnsupportedOperationException();
        }

        @Override
        public PagedResult<Outlet> queryPageBy(String tenantId, KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Map<String, List<Outlet>> findAllByEducatorIds(List<String> educatorIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteById(String id) {
            throw new UnsupportedOperationException();
        }
    }
}
