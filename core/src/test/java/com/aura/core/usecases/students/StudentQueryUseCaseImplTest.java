package com.aura.core.usecases.students;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


import com.aura.core.entities.students.Student;
import com.aura.core.repositories.StudentRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudentQueryUseCaseImpl")
class StudentQueryUseCaseImplTest {

    @Mock
    private StudentRepository repo;
    @InjectMocks
    private StudentQueryUseCaseImpl useCase;

    @Test
    @DisplayName("Should return student when found by id")
    void findByIdReturnsStudent() {
        Student s = Student.builder().id("a").build();
        when(repo.findById("a")).thenReturn(s);

        assertThat(useCase.findById("a")).isEqualTo(s);
    }

    @Test
    @DisplayName("Should return null when student not found by id")
    void findByIdReturnsNullWhenAbsent() {
        when(repo.findById("missing")).thenReturn(null);

        assertThat(useCase.findById("missing")).isNull();
    }

    @Test
    @DisplayName("Should return all students for tenant")
    void findAllByTenantIdReturnsList() {
        var s1 = Student.builder().id("a").build();
        var s2 = Student.builder().id("b").build();
        when(repo.findAllByTenantId("tenant-1")).thenReturn(List.of(s1, s2));

        assertThat(useCase.findAllByTenantId("tenant-1")).containsExactly(s1, s2);
    }
}
