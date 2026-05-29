package com.aura.core.usecases.educators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


import com.aura.core.entities.educators.Educator;
import com.aura.core.repositories.EducatorRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("EducatorQueryUseCaseImpl")
class EducatorQueryUseCaseImplTest {

    @Mock
    private EducatorRepository repo;
    @InjectMocks
    private EducatorQueryUseCaseImpl useCase;

    @Test
    @DisplayName("Should return educator when found by id")
    void findByIdReturnsEducator() {
        Educator e = Educator.builder().id("a").build();
        when(repo.findById("a")).thenReturn(e);

        assertThat(useCase.findById("a")).isEqualTo(e);
    }

    @Test
    @DisplayName("Should return null when educator not found by id")
    void findByIdReturnsNullWhenAbsent() {
        when(repo.findById("missing")).thenReturn(null);

        assertThat(useCase.findById("missing")).isNull();
    }

    @Test
    @DisplayName("Should return all educators for tenant")
    void findAllByTenantIdReturnsList() {
        var e1 = Educator.builder().id("a").build();
        var e2 = Educator.builder().id("b").build();
        when(repo.findAllByTenantId("tenant-1")).thenReturn(List.of(e1, e2));

        assertThat(useCase.findAllByTenantId("tenant-1")).containsExactly(e1, e2);
    }
}
