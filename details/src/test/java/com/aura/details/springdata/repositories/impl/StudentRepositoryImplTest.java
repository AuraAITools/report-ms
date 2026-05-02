package com.aura.details.springdata.repositories.impl;

import com.aura.details.springdata.repositories.jpa.courses.JpaCourseRepository;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentCourseLinkRepository;
import com.aura.details.springdata.repositories.jpa.schools.JpaSchoolRepository;
import com.aura.details.springdata.repositories.jpa.students.JpaStudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Student repository implementation")
@ExtendWith(MockitoExtension.class)
class StudentRepositoryImplTest {

    @Mock private JpaStudentRepository jpaStudentRepository;
    @Mock private JpaLevelRepository jpaLevelRepository;
    @Mock private JpaSchoolRepository jpaSchoolRepository;
    @Mock private JpaStudentCourseLinkRepository jpaStudentCourseLinkRepository;
    @Mock private JpaCourseRepository jpaCourseRepository;

    @InjectMocks private StudentRepositoryImpl studentRepositoryImpl;

    @DisplayName("Should return map of student ID to user ID")
    @Test
    void findUserIdsByStudentIds_returnsMappedUserIds() {
        UUID studentId1 = UUID.randomUUID();
        UUID studentId2 = UUID.randomUUID();
        List<String> studentIds = List.of(studentId1.toString(), studentId2.toString());

        when(jpaStudentRepository.findUserIdsByIds(List.of(studentId1, studentId2)))
                .thenReturn(List.of(
                        new Object[]{studentId1, "user-xxx"},
                        new Object[]{studentId2, "user-yyy"}
                ));

        Map<String, String> result = studentRepositoryImpl.findUserIdsByStudentIds(studentIds);

        assertThat(result).containsEntry(studentId1.toString(), "user-xxx");
        assertThat(result).containsEntry(studentId2.toString(), "user-yyy");
        assertThat(result).hasSize(2);
    }

    @DisplayName("Should return empty map when student list is empty")
    @Test
    void findUserIdsByStudentIds_emptyList_returnsEmptyMap() {
        Map<String, String> result = studentRepositoryImpl.findUserIdsByStudentIds(List.of());

        assertThat(result).isEmpty();
    }
}
