package com.aura.core.usecases.educators;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.aura.core.entities.educators.Educator;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.EducatorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteEducatorUseCaseImpl")
class DeleteEducatorUseCaseImplTest {

    @Mock
    private EducatorRepository repo;
    @InjectMocks
    private DeleteEducatorUseCaseImpl useCase;

    @Test
    @DisplayName("Should delete when educator exists")
    void deletesWhenExists() {
        when(repo.findById("a")).thenReturn(Educator.builder().id("a").build());

        useCase.deleteById("a");

        verify(repo).deleteById("a");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when educator missing")
    void throwsWhenMissing() {
        when(repo.findById("missing")).thenReturn(null);

        assertThatThrownBy(() -> useCase.deleteById("missing"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
