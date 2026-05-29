package com.aura.core.usecases.parents;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
@DisplayName("UpdateParentUseCaseImpl")
class UpdateParentUseCaseImplTest {

    @Mock
    private ParentRepository repo;
    @InjectMocks
    private UpdateParentUseCaseImpl useCase;

    @Test
    @DisplayName("Should update the relationship when provided and persist")
    void updatesRelationship() {
        Parent existing = Parent.builder()
                .id("a")
                .relationship(Parent.RELATIONSHIP.PARENT)
                .build();
        when(repo.findById("a")).thenReturn(existing);
        when(repo.save(any(Parent.class))).thenAnswer(inv -> inv.getArgument(0));

        Parent updated = useCase.update("a", Parent.RELATIONSHIP.SELF);

        assertThat(updated.getRelationship()).isEqualTo(Parent.RELATIONSHIP.SELF);
    }

    @Test
    @DisplayName("Should leave existing relationship intact when null passed")
    void leavesRelationshipWhenNull() {
        Parent existing = Parent.builder()
                .id("a")
                .relationship(Parent.RELATIONSHIP.PARENT)
                .build();
        when(repo.findById("a")).thenReturn(existing);
        when(repo.save(any(Parent.class))).thenAnswer(inv -> inv.getArgument(0));

        Parent updated = useCase.update("a", null);

        assertThat(updated.getRelationship()).isEqualTo(Parent.RELATIONSHIP.PARENT);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when parent does not exist")
    void throwsWhenMissing() {
        when(repo.findById("missing")).thenReturn(null);

        assertThatThrownBy(() -> useCase.update("missing", Parent.RELATIONSHIP.SELF))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
