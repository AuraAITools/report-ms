package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.levels.Level;
import com.aura.core.entities.schools.School;
import com.aura.core.entities.students.Student;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.models.schools.SchoolModel;
import com.aura.details.springdata.models.students.StudentModel;
import com.aura.details.springdata.repositories.jpa.courses.JpaCourseRepository;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaAccountStudentLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentCourseLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentLessonLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentOutletLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectStudentLinkRepository;
import com.aura.details.springdata.repositories.jpa.schools.JpaSchoolRepository;
import com.aura.details.springdata.repositories.jpa.students.JpaStudentRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("Student repository implementation")
@ExtendWith(MockitoExtension.class)
class StudentRepositoryImplTest {

    @Mock
    private JpaStudentRepository jpaStudentRepository;
    @Mock
    private JpaLevelRepository jpaLevelRepository;
    @Mock
    private JpaSchoolRepository jpaSchoolRepository;
    @Mock
    private JpaStudentCourseLinkRepository jpaStudentCourseLinkRepository;
    @Mock
    private JpaCourseRepository jpaCourseRepository;
    @Mock
    private JpaStudentLessonLinkRepository jpaStudentLessonLinkRepository;
    @Mock
    private JpaStudentOutletLinkRepository jpaStudentOutletLinkRepository;
    @Mock
    private JpaSubjectStudentLinkRepository jpaSubjectStudentLinkRepository;
    @Mock
    private JpaAccountStudentLinkRepository jpaAccountStudentLinkRepository;
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private StudentRepositoryImpl studentRepositoryImpl;

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

    @DisplayName("Should attach managed school and level proxies via EntityManager.getReference before persisting")
    @Test
    void saveAttachesSchoolAndLevelRefs() {
        UUID schoolId = UUID.randomUUID();
        UUID levelId = UUID.randomUUID();
        SchoolModel schoolProxy = SchoolModel.builder().id(schoolId).build();
        LevelModel levelProxy = LevelModel.builder().id(levelId).build();
        when(entityManager.getReference(SchoolModel.class, schoolId)).thenReturn(schoolProxy);
        when(entityManager.getReference(LevelModel.class, levelId)).thenReturn(levelProxy);
        when(jpaStudentRepository.save(any(StudentModel.class))).thenReturn(new StudentModel());

        Student domain = Student.builder()
                .schoolId(schoolId.toString())
                .levelId(levelId.toString())
                .name("Alice")
                .build();
        studentRepositoryImpl.save(domain);

        ArgumentCaptor<StudentModel> captor = ArgumentCaptor.forClass(StudentModel.class);
        verify(jpaStudentRepository).save(captor.capture());
        assertThat(captor.getValue().getSchool()).isSameAs(schoolProxy);
        assertThat(captor.getValue().getLevel()).isSameAs(levelProxy);
        assertThat(captor.getValue().getName()).isEqualTo("Alice");
    }

    @DisplayName("Should group students by course id from link table query")
    @Test
    void findAllByCourseIds_groupsStudentsByCourse() {
        UUID courseId1 = UUID.randomUUID();
        UUID courseId2 = UUID.randomUUID();
        UUID student1Id = UUID.randomUUID();
        UUID student2Id = UUID.randomUUID();
        StudentModel s1 = StudentModel.builder().id(student1Id).build();
        StudentModel s2 = StudentModel.builder().id(student2Id).build();
        when(jpaStudentCourseLinkRepository.findStudentsByCourseIds(List.of(courseId1, courseId2)))
                .thenReturn(List.of(
                        new Object[]{courseId1, s1},
                        new Object[]{courseId2, s2}
                ));

        Map<String, List<Student>> result = studentRepositoryImpl
                .findAllByCourseIds(List.of(courseId1.toString(), courseId2.toString()));

        assertThat(result).hasSize(2);
        assertThat(result.get(courseId1.toString())).extracting(Student::getId)
                .containsExactly(student1Id.toString());
        assertThat(result.get(courseId2.toString())).extracting(Student::getId)
                .containsExactly(student2Id.toString());
    }

    @DisplayName("Should return empty map when course id list is empty")
    @Test
    void findAllByCourseIds_emptyList_returnsEmptyMap() {
        assertThat(studentRepositoryImpl.findAllByCourseIds(List.of())).isEmpty();
    }

    @DisplayName("Should return all students for tenant")
    @Test
    void findAllByTenantId_returnsAll() {
        UUID studentId = UUID.randomUUID();
        StudentModel model = StudentModel.builder().id(studentId).name("Alice").build();
        when(jpaStudentRepository.findByTenantId("tenant-1")).thenReturn(List.of(model));

        List<Student> result = studentRepositoryImpl.findAllByTenantId("tenant-1");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(studentId.toString());
        assertThat(result.get(0).getName()).isEqualTo("Alice");
    }

    @DisplayName("Should delegate deleteById to JPA repository with parsed UUID")
    @Test
    void deleteById_delegates() {
        String id = "00000000-0000-0000-0000-000000000001";

        studentRepositoryImpl.deleteById(id);

        verify(jpaStudentRepository).deleteById(UUID.fromString(id));
    }

    @DisplayName("Should delegate deleteAllStudentCourseLinksByStudentId to JPA repository")
    @Test
    void deleteAllStudentCourseLinksByStudentId_delegates() {
        UUID studentId = UUID.randomUUID();

        studentRepositoryImpl.deleteAllStudentCourseLinksByStudentId(studentId.toString());

        verify(jpaStudentCourseLinkRepository).deleteAllByStudentId(studentId);
    }

    @DisplayName("Should delegate deleteAllStudentOutletLinksByStudentId to JPA repository")
    @Test
    void deleteAllStudentOutletLinksByStudentId_delegates() {
        UUID studentId = UUID.randomUUID();

        studentRepositoryImpl.deleteAllStudentOutletLinksByStudentId(studentId.toString());

        verify(jpaStudentOutletLinkRepository).deleteAllByStudentId(studentId);
    }

    @DisplayName("Should delegate deleteAllStudentLessonLinksByStudentId to JPA repository")
    @Test
    void deleteAllStudentLessonLinksByStudentId_delegates() {
        UUID studentId = UUID.randomUUID();

        studentRepositoryImpl.deleteAllStudentLessonLinksByStudentId(studentId.toString());

        verify(jpaStudentLessonLinkRepository).deleteAllByStudentId(studentId);
    }

    @DisplayName("Should delegate deleteAllSubjectStudentLinksByStudentId to JPA repository")
    @Test
    void deleteAllSubjectStudentLinksByStudentId_delegates() {
        UUID studentId = UUID.randomUUID();

        studentRepositoryImpl.deleteAllSubjectStudentLinksByStudentId(studentId.toString());

        verify(jpaSubjectStudentLinkRepository).deleteAllByStudentId(studentId);
    }

    @DisplayName("Should delegate deleteAllAccountStudentLinksByStudentId to JPA repository")
    @Test
    void deleteAllAccountStudentLinksByStudentId_delegates() {
        UUID studentId = UUID.randomUUID();

        studentRepositoryImpl.deleteAllAccountStudentLinksByStudentId(studentId.toString());

        verify(jpaAccountStudentLinkRepository).deleteAllByStudentId(studentId);
    }

    @DisplayName("Should return empty map when no student ids provided to findLevelsByStudentIds")
    @Test
    void findLevelsByStudentIds_emptyList_returnsEmptyMap() {
        assertThat(studentRepositoryImpl.findLevelsByStudentIds(List.of())).isEmpty();
        verifyNoInteractions(jpaStudentRepository);
    }

    @DisplayName("Should group level lookup by student id")
    @Test
    void findLevelsByStudentIds_groupsByStudentId() {
        UUID studentId1 = UUID.randomUUID();
        UUID studentId2 = UUID.randomUUID();
        LevelModel level1 = LevelModel.builder().id(UUID.randomUUID()).name("P1").build();
        LevelModel level2 = LevelModel.builder().id(UUID.randomUUID()).name("P2").build();
        when(jpaStudentRepository.findLevelsByStudentIds(List.of(studentId1, studentId2)))
                .thenReturn(List.of(
                        new Object[]{studentId1, level1},
                        new Object[]{studentId2, level2}
                ));

        Map<String, Level> result = studentRepositoryImpl.findLevelsByStudentIds(
                List.of(studentId1.toString(), studentId2.toString()));

        assertThat(result).hasSize(2);
        assertThat(result.get(studentId1.toString()).getName()).isEqualTo("P1");
        assertThat(result.get(studentId2.toString()).getName()).isEqualTo("P2");
    }

    @DisplayName("Should return empty map when no student ids provided to findSchoolsByStudentIds")
    @Test
    void findSchoolsByStudentIds_emptyList_returnsEmptyMap() {
        assertThat(studentRepositoryImpl.findSchoolsByStudentIds(List.of())).isEmpty();
        verifyNoInteractions(jpaStudentRepository);
    }

    @DisplayName("Should group school lookup by student id")
    @Test
    void findSchoolsByStudentIds_groupsByStudentId() {
        UUID studentId1 = UUID.randomUUID();
        UUID studentId2 = UUID.randomUUID();
        SchoolModel school1 = SchoolModel.builder().id(UUID.randomUUID()).name("School A").build();
        SchoolModel school2 = SchoolModel.builder().id(UUID.randomUUID()).name("School B").build();
        when(jpaStudentRepository.findSchoolsByStudentIds(List.of(studentId1, studentId2)))
                .thenReturn(List.of(
                        new Object[]{studentId1, school1},
                        new Object[]{studentId2, school2}
                ));

        Map<String, School> result = studentRepositoryImpl.findSchoolsByStudentIds(
                List.of(studentId1.toString(), studentId2.toString()));

        assertThat(result).hasSize(2);
        assertThat(result.get(studentId1.toString()).getName()).isEqualTo("School A");
        assertThat(result.get(studentId2.toString()).getName()).isEqualTo("School B");
    }
}
