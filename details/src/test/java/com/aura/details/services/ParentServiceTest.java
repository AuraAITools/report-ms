package com.aura.details.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.aura.core.entities.accounts.Parent;
import com.aura.core.usecases.parents.DeleteParentUseCase;
import com.aura.core.usecases.parents.ParentQueryUseCase;
import com.aura.core.usecases.parents.UpdateParentUseCase;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ParentService")
class ParentServiceTest {

    @Mock
    private ParentQueryUseCase queryUseCase;
    @Mock
    private UpdateParentUseCase updateUseCase;
    @Mock
    private DeleteParentUseCase deleteUseCase;
    @InjectMocks
    private ParentService service;

    @Test
    @DisplayName("Should delegate findById to the query use case")
    void findByIdDelegates() {
        Parent p = Parent.builder().id("a").build();
        when(queryUseCase.findById("a")).thenReturn(p);
        assertThat(service.findById("a")).isEqualTo(p);
    }

    @Test
    @DisplayName("Should delegate findAllByTenantId to the query use case")
    void findAllByTenantIdDelegates() {
        when(queryUseCase.findAllByTenantId("t")).thenReturn(List.of());
        assertThat(service.findAllByTenantId("t")).isEmpty();
    }

    @Test
    @DisplayName("Should delegate updateById to the update use case")
    void updateByIdDelegates() {
        Parent p = Parent.builder().id("a").relationship(Parent.RELATIONSHIP.SELF).build();
        when(updateUseCase.update("a", Parent.RELATIONSHIP.SELF)).thenReturn(p);
        assertThat(service.updateById("a", Parent.RELATIONSHIP.SELF)).isEqualTo(p);
    }

    @Test
    @DisplayName("Should delegate deleteById to the delete use case")
    void deleteByIdDelegates() {
        service.deleteById("pid");

        verify(deleteUseCase).deleteById("pid");
    }
}
