package com.aura.core.usecases.lessons;

import com.aura.core.repositories.LessonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.verify;

@DisplayName("Delete lesson use case")
@ExtendWith(MockitoExtension.class)
class DeleteLessonUseCaseImplTest {

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private DeleteLessonUseCaseImpl useCase;

    @Test
    @DisplayName("Should delegate deleteById to the lesson repository")
    void deletesViaRepository() {
        useCase.deleteById("lesson-1");

        verify(lessonRepository).deleteById("lesson-1");
    }
}
