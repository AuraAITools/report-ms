package com.aura.core.usecases.courses;

import com.aura.core.repositories.CourseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.verify;

@DisplayName("Delete course use case")
@ExtendWith(MockitoExtension.class)
class DeleteCourseUseCaseImplTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private DeleteCourseUseCaseImpl useCase;

    @Test
    @DisplayName("Should delegate deleteById to the course repository")
    void deletesViaRepository() {
        useCase.deleteById("course-1");

        verify(courseRepository).deleteById("course-1");
    }
}
