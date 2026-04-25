package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.institutions.Institution;
import com.aura.details.springdata.models.institutions.InstitutionModel;
import com.aura.details.springdata.repositories.jpa.institutions.JpaInstitutionRepository;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Institution repository")
@ExtendWith(MockitoExtension.class)
class InstitutionRepositoryImplTest {

    @Mock
    private JpaInstitutionRepository jpaRepository;

    private InstitutionRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new InstitutionRepositoryImpl(jpaRepository);
    }

    @DisplayName("Should convert string IDs to UUIDs and map results to domain entities")
    @Test
    void findAllByIds_convertsIdsAndMapsResults() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        Set<String> ids = Set.of(uuid1.toString(), uuid2.toString());

        InstitutionModel table1 = new InstitutionModel();
        table1.setId(uuid1);
        table1.setName("Institution A");
        table1.setEmail("a@test.com");
        table1.setContactNumber("111");
        table1.setAddress("Address A");
        table1.setUen("UEN-A");
        table1.setTenantId(uuid1.toString());

        InstitutionModel table2 = new InstitutionModel();
        table2.setId(uuid2);
        table2.setName("Institution B");
        table2.setEmail("b@test.com");
        table2.setContactNumber("222");
        table2.setAddress("Address B");
        table2.setUen("UEN-B");
        table2.setTenantId(uuid2.toString());

        when(jpaRepository.findAllById(anyList())).thenReturn(List.of(table1, table2));

        List<Institution> result = repository.findAllByIds(ids);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Institution::getName)
                .containsExactlyInAnyOrder("Institution A", "Institution B");

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<UUID>> captor = ArgumentCaptor.forClass(List.class);
        verify(jpaRepository).findAllById(captor.capture());
        assertThat(captor.getValue()).containsExactlyInAnyOrder(uuid1, uuid2);
    }

    @Test
    void findAllByIds_emptySet_returnsEmptyList() {
        when(jpaRepository.findAllById(anyList())).thenReturn(List.of());

        List<Institution> result = repository.findAllByIds(Set.of());

        assertThat(result).isEmpty();
    }
}
