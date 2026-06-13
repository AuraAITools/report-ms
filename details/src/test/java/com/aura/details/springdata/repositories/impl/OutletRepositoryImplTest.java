package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.outlets.Outlet;
import com.aura.details.springdata.models.outlets.OutletModel;
import com.aura.details.springdata.repositories.jpa.links.JpaOutletEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.outlets.JpaOutletRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Outlet repository implementation")
@ExtendWith(MockitoExtension.class)
class OutletRepositoryImplTest {

    @Mock
    private JpaOutletRepository jpaOutletRepository;
    @Mock
    private JpaOutletEducatorLinkRepository jpaOutletEducatorLinkRepository;

    @InjectMocks
    private OutletRepositoryImpl outletRepositoryImpl;

    @DisplayName("Should return all outlets for the given tenant id")
    @Test
    void findAllByTenantId_returnsOutlets() {
        OutletModel m1 = OutletModel.builder().id(UUID.randomUUID()).name("Bukit Timah").build();
        OutletModel m2 = OutletModel.builder().id(UUID.randomUUID()).name("Tampines").build();
        when(jpaOutletRepository.findByTenantId("tenant-1")).thenReturn(List.of(m1, m2));

        assertThat(outletRepositoryImpl.findAllByTenantId("tenant-1"))
                .extracting(Outlet::getName)
                .containsExactly("Bukit Timah", "Tampines");
    }

    @DisplayName("Should return empty list when no outlets in tenant")
    @Test
    void findAllByTenantId_returnsEmpty() {
        when(jpaOutletRepository.findByTenantId("tenant-1")).thenReturn(List.of());

        assertThat(outletRepositoryImpl.findAllByTenantId("tenant-1")).isEmpty();
    }

    @DisplayName("Should delegate existsByNameAndTenantId to the JPA repository")
    @Test
    void existsByNameAndTenantId_delegatesToJpaRepository() {
        when(jpaOutletRepository.existsByNameAndTenantId("Bukit Timah", "tenant-1")).thenReturn(true);

        assertThat(outletRepositoryImpl.existsByNameAndTenantId("Bukit Timah", "tenant-1")).isTrue();
        assertThat(outletRepositoryImpl.existsByNameAndTenantId("Tampines", "tenant-1")).isFalse();
    }

    @DisplayName("Should return empty map when no educator ids provided to findAllByEducatorIds")
    @Test
    void findAllByEducatorIds_emptyList_returnsEmptyMap() {
        assertThat(outletRepositoryImpl.findAllByEducatorIds(List.of())).isEmpty();
    }

    @DisplayName("Should group outlets by educator id from link table query")
    @Test
    void findAllByEducatorIds_groupsOutletsByEducator() {
        UUID educatorId1 = UUID.randomUUID();
        UUID educatorId2 = UUID.randomUUID();
        OutletModel o1 = OutletModel.builder().id(UUID.randomUUID()).name("Bukit Timah").build();
        OutletModel o2 = OutletModel.builder().id(UUID.randomUUID()).name("Tampines").build();

        when(jpaOutletEducatorLinkRepository.findOutletsByEducatorIds(List.of(educatorId1, educatorId2)))
                .thenReturn(List.<Object[]>of(
                        new Object[]{educatorId1, o1},
                        new Object[]{educatorId2, o2}
                ));

        Map<String, List<Outlet>> result = outletRepositoryImpl
                .findAllByEducatorIds(List.of(educatorId1.toString(), educatorId2.toString()));

        assertThat(result).hasSize(2);
        assertThat(result.get(educatorId1.toString())).extracting(Outlet::getName).containsExactly("Bukit Timah");
        assertThat(result.get(educatorId2.toString())).extracting(Outlet::getName).containsExactly("Tampines");
    }
}
