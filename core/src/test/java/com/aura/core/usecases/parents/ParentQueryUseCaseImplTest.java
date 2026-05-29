package com.aura.core.usecases.parents;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


import com.aura.core.entities.accounts.Parent;
import com.aura.core.repositories.ParentRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ParentQueryUseCaseImpl")
class ParentQueryUseCaseImplTest {

    @Mock
    private ParentRepository repo;
    @InjectMocks
    private ParentQueryUseCaseImpl useCase;

    @Test
    @DisplayName("Should return parent when found by id")
    void findByIdReturnsParent() {
        Parent p = Parent.builder().id("a").build();
        when(repo.findById("a")).thenReturn(p);

        assertThat(useCase.findById("a")).isEqualTo(p);
    }

    @Test
    @DisplayName("Should return null when parent not found by id")
    void findByIdReturnsNullWhenAbsent() {
        when(repo.findById("missing")).thenReturn(null);

        assertThat(useCase.findById("missing")).isNull();
    }

    @Test
    @DisplayName("Should return all parents for tenant")
    void findAllByTenantIdReturnsList() {
        var p1 = Parent.builder().id("a").build();
        var p2 = Parent.builder().id("b").build();
        when(repo.findAllByTenantId("tenant-1")).thenReturn(List.of(p1, p2));

        assertThat(useCase.findAllByTenantId("tenant-1")).containsExactly(p1, p2);
    }
}
