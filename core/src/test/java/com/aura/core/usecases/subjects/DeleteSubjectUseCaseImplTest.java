package com.aura.core.usecases.subjects;

import com.aura.core.repositories.SubjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.verify;

@DisplayName("Delete subject use case")
@ExtendWith(MockitoExtension.class)
class DeleteSubjectUseCaseImplTest {

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private DeleteSubjectUseCaseImpl useCase;

    @Test
    @DisplayName("Should delegate deleteById to the subject repository")
    void deletesViaRepository() {
        useCase.deleteById("subject-1");

        verify(subjectRepository).deleteById("subject-1");
    }
}
