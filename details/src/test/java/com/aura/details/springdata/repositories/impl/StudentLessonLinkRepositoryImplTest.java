package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.StudentLessonLink;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.links.StudentLessonLinkModel;
import com.aura.details.springdata.models.students.StudentModel;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentLessonLinkRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
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
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Student lesson link repository impl")
@ExtendWith(MockitoExtension.class)
class StudentLessonLinkRepositoryImplTest {

    @Mock
    private JpaStudentLessonLinkRepository jpaRepository;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private StudentLessonLinkRepositoryImpl repository;

    @DisplayName("Should save a single student-lesson link")
    @Test
    void saveDelegatesToJpaRepository() {
        StudentLessonLinkModel savedModel = new StudentLessonLinkModel();
        when(jpaRepository.save(any(StudentLessonLinkModel.class))).thenReturn(savedModel);

        StudentLessonLink domain = StudentLessonLink.builder().build();
        StudentLessonLink result = repository.save(domain);

        assertThat(result).isNotNull();
        verify(jpaRepository).save(any(StudentLessonLinkModel.class));
    }

    @DisplayName("Should save all student-lesson links")
    @Test
    void saveAllDelegatesToJpaRepository() {
        StudentLessonLinkModel model1 = new StudentLessonLinkModel();
        StudentLessonLinkModel model2 = new StudentLessonLinkModel();
        when(jpaRepository.saveAll(anyList())).thenReturn(List.of(model1, model2));

        List<StudentLessonLink> domains = List.of(
                StudentLessonLink.builder().build(),
                StudentLessonLink.builder().build());
        List<StudentLessonLink> result = repository.saveAll(domains);

        assertThat(result).hasSize(2);
        verify(jpaRepository).saveAll(anyList());
    }

    @DisplayName("Should attach managed student and lesson proxies via EntityManager.getReference before persisting")
    @Test
    void saveAttachesStudentAndLessonRefs() {
        UUID studentId = UUID.randomUUID();
        UUID lessonId = UUID.randomUUID();
        StudentModel studentProxy = StudentModel.builder().id(studentId).build();
        LessonModel lessonProxy = LessonModel.builder().id(lessonId).build();
        when(entityManager.getReference(StudentModel.class, studentId)).thenReturn(studentProxy);
        when(entityManager.getReference(LessonModel.class, lessonId)).thenReturn(lessonProxy);
        when(jpaRepository.save(any(StudentLessonLinkModel.class))).thenReturn(new StudentLessonLinkModel());

        StudentLessonLink domain = StudentLessonLink.builder()
                .studentId(studentId.toString())
                .lessonId(lessonId.toString())
                .build();
        repository.save(domain);

        ArgumentCaptor<StudentLessonLinkModel> captor = ArgumentCaptor.forClass(StudentLessonLinkModel.class);
        verify(jpaRepository).save(captor.capture());
        assertThat(captor.getValue().getStudent()).isSameAs(studentProxy);
        assertThat(captor.getValue().getLesson()).isSameAs(lessonProxy);
    }
}
