package com.aura.core.usecases.students;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


import com.aura.core.entities.students.Student;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.StudentRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateStudentUseCaseImpl")
class UpdateStudentUseCaseImplTest {

    @Mock
    private StudentRepository repo;
    @InjectMocks
    private UpdateStudentUseCaseImpl useCase;

    @Test
    @DisplayName("Should update only the non-null fields and persist")
    void updatesNonNullFields() {
        Student existing = Student.builder()
                .id("a")
                .name("Old")
                .email("old@e.com")
                .dateOfBirth(LocalDate.of(2010, 1, 1))
                .build();
        when(repo.findById("a")).thenReturn(existing);
        when(repo.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

        Student updated = useCase.update("a", "New", null, LocalDate.of(2011, 2, 2));

        assertThat(updated.getName()).isEqualTo("New");
        assertThat(updated.getEmail()).isEqualTo("old@e.com");
        assertThat(updated.getDateOfBirth()).isEqualTo(LocalDate.of(2011, 2, 2));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when student does not exist")
    void throwsWhenMissing() {
        when(repo.findById("missing")).thenReturn(null);

        assertThatThrownBy(() -> useCase.update("missing", "x", null, null))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
