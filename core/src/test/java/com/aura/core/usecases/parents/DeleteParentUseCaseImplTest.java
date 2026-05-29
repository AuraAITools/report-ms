package com.aura.core.usecases.parents;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.aura.core.entities.accounts.Parent;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.ParentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteParentUseCaseImpl")
class DeleteParentUseCaseImplTest {

    @Mock
    private ParentRepository repo;
    @InjectMocks
    private DeleteParentUseCaseImpl useCase;

    @Test
    @DisplayName("Should delete when parent exists")
    void deletesWhenExists() {
        when(repo.findById("a")).thenReturn(Parent.builder().id("a").build());

        useCase.deleteById("a");

        verify(repo).deleteById("a");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when parent missing")
    void throwsWhenMissing() {
        when(repo.findById("missing")).thenReturn(null);

        assertThatThrownBy(() -> useCase.deleteById("missing"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
