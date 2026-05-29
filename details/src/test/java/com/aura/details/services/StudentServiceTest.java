package com.aura.details.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;


import com.aura.core.entities.students.Student;
import com.aura.core.repositories.StudentRepository;
import com.aura.core.usecases.students.DeleteStudentUseCase;
import com.aura.core.usecases.students.StudentQueryUseCase;
import com.aura.core.usecases.students.UpdateStudentUseCase;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudentService")
class StudentServiceTest {

    @Mock
    private StudentQueryUseCase queryUseCase;
    @Mock
    private UpdateStudentUseCase updateUseCase;
    @Mock
    private DeleteStudentUseCase deleteUseCase;
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentService service;

    @Test
    @DisplayName("Should delegate findById to the query use case")
    void findByIdDelegates() {
        Student s = Student.builder().id("a").build();
        when(queryUseCase.findById("a")).thenReturn(s);
        assertThat(service.findById("a")).isEqualTo(s);
    }

    @Test
    @DisplayName("Should delegate findAllByTenantId to the query use case")
    void findAllByTenantIdDelegates() {
        when(queryUseCase.findAllByTenantId("t")).thenReturn(List.of());
        assertThat(service.findAllByTenantId("t")).isEmpty();
    }

    @Test
    @DisplayName("Should delegate updateById to the update use case")
    void updateByIdDelegates() {
        Student s = Student.builder().id("a").name("New").build();
        when(updateUseCase.update("a", "New", null, null)).thenReturn(s);
        assertThat(service.updateById("a", "New", null, null)).isEqualTo(s);
    }

    @Test
    @DisplayName("Should clean all link rows before calling delete use case on deleteById")
    void deleteByIdClearsLinksThenDeletes() {
        service.deleteById("sid");

        InOrder order = inOrder(studentRepository, deleteUseCase);
        order.verify(studentRepository).deleteAllStudentCourseLinksByStudentId("sid");
        order.verify(studentRepository).deleteAllStudentOutletLinksByStudentId("sid");
        order.verify(studentRepository).deleteAllStudentLessonLinksByStudentId("sid");
        order.verify(studentRepository).deleteAllSubjectStudentLinksByStudentId("sid");
        order.verify(studentRepository).deleteAllAccountStudentLinksByStudentId("sid");
        order.verify(deleteUseCase).deleteById("sid");
    }
}
