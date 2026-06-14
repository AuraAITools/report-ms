package com.aura.core.usecases.lesson_groups;

import com.aura.core.repositories.LessonGroupRepository;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.verify;

@DisplayName("Delete lesson group use case")
@ExtendWith(MockitoExtension.class)
class DeleteLessonGroupUseCaseImplTest {

    @Mock
    private LessonGroupRepository lessonGroupRepository;

    @InjectMocks
    private DeleteLessonGroupUseCaseImpl useCase;

    @Test
    @DisplayName("Should delegate deleteById to the lesson group repository")
    void deletesViaRepository() {
        UUID id = UUID.randomUUID();

        useCase.deleteById(id.toString());

        verify(lessonGroupRepository).deleteById(id);
    }
}
