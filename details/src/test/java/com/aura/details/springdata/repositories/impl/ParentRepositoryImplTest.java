package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.accounts.Parent;
import com.aura.details.springdata.models.accounts.AccountModel;
import com.aura.details.springdata.models.accounts.ParentModel;
import com.aura.details.springdata.repositories.jpa.accounts.JpaParentRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Parent repository impl")
@ExtendWith(MockitoExtension.class)
class ParentRepositoryImplTest {

    @Mock
    private JpaParentRepository jpaParentRepository;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private ParentRepositoryImpl repository;

    @DisplayName("Should attach managed account proxy via EntityManager.getReference before persisting")
    @Test
    void saveAttachesAccountRef() {
        UUID accountId = UUID.randomUUID();
        AccountModel accountProxy = AccountModel.builder().id(accountId).build();
        when(entityManager.getReference(AccountModel.class, accountId)).thenReturn(accountProxy);
        when(jpaParentRepository.save(any(ParentModel.class))).thenReturn(new ParentModel());

        Parent parent = Parent.builder()
                .accountId(accountId.toString())
                .relationship(Parent.RELATIONSHIP.PARENT)
                .build();
        repository.save(parent);

        ArgumentCaptor<ParentModel> captor = ArgumentCaptor.forClass(ParentModel.class);
        verify(jpaParentRepository).save(captor.capture());
        assertThat(captor.getValue().getAccount()).isSameAs(accountProxy);
        assertThat(captor.getValue().getRelationship()).isEqualTo(ParentModel.RELATIONSHIP.PARENT);
    }

    @DisplayName("Should return all parents for tenant")
    @Test
    void findAllByTenantId_returnsAll() {
        UUID parentId = UUID.randomUUID();
        ParentModel model = ParentModel.builder()
                .id(parentId)
                .relationship(ParentModel.RELATIONSHIP.PARENT)
                .build();
        when(jpaParentRepository.findByTenantId("tenant-1")).thenReturn(List.of(model));

        List<Parent> result = repository.findAllByTenantId("tenant-1");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(parentId.toString());
        assertThat(result.get(0).getRelationship()).isEqualTo(Parent.RELATIONSHIP.PARENT);
    }

    @DisplayName("Should delegate deleteById to JPA repository with parsed UUID")
    @Test
    void deleteById_delegates() {
        String id = "00000000-0000-0000-0000-000000000001";

        repository.deleteById(id);

        verify(jpaParentRepository).deleteById(UUID.fromString(id));
    }
}
