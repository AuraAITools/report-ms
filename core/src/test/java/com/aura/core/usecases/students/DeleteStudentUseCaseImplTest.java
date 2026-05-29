package com.aura.core.usecases.students;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.aura.core.entities.students.Student;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteStudentUseCaseImpl")
class DeleteStudentUseCaseImplTest {

    @Mock
    private StudentRepository repo;
    @InjectMocks
    private DeleteStudentUseCaseImpl useCase;

    @Test
    @DisplayName("Should delete when student exists")
    void deletesWhenExists() {
        when(repo.findById("a")).thenReturn(Student.builder().id("a").build());

        useCase.deleteById("a");

        verify(repo).deleteById("a");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when student missing")
    void throwsWhenMissing() {
        when(repo.findById("missing")).thenReturn(null);

        assertThatThrownBy(() -> useCase.deleteById("missing"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
