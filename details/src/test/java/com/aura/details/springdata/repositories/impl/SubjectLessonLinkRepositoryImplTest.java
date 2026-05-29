package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.SubjectLessonLink;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.links.SubjectLessonLinkModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectLessonLinkRepository;
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

@DisplayName("Subject lesson link repository impl")
@ExtendWith(MockitoExtension.class)
class SubjectLessonLinkRepositoryImplTest {

    @Mock
    private JpaSubjectLessonLinkRepository jpaRepository;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private SubjectLessonLinkRepositoryImpl repository;

    @DisplayName("Should save a single subject-lesson link")
    @Test
    void saveDelegatesToJpaRepository() {
        SubjectLessonLinkModel savedModel = new SubjectLessonLinkModel();
        when(jpaRepository.save(any(SubjectLessonLinkModel.class))).thenReturn(savedModel);

        SubjectLessonLink domain = SubjectLessonLink.builder().build();
        SubjectLessonLink result = repository.save(domain);

        assertThat(result).isNotNull();
        verify(jpaRepository).save(any(SubjectLessonLinkModel.class));
    }

    @DisplayName("Should save all subject-lesson links")
    @Test
    void saveAllDelegatesToJpaRepository() {
        SubjectLessonLinkModel model1 = new SubjectLessonLinkModel();
        SubjectLessonLinkModel model2 = new SubjectLessonLinkModel();
        when(jpaRepository.saveAll(anyList())).thenReturn(List.of(model1, model2));

        List<SubjectLessonLink> domains = List.of(
                SubjectLessonLink.builder().build(),
                SubjectLessonLink.builder().build());
        List<SubjectLessonLink> result = repository.saveAll(domains);

        assertThat(result).hasSize(2);
        verify(jpaRepository).saveAll(anyList());
    }

    @DisplayName("Should attach managed subject and lesson proxies via EntityManager.getReference before persisting")
    @Test
    void saveAttachesSubjectAndLessonRefs() {
        UUID subjectId = UUID.randomUUID();
        UUID lessonId = UUID.randomUUID();
        SubjectModel subjectProxy = SubjectModel.builder().id(subjectId).build();
        LessonModel lessonProxy = LessonModel.builder().id(lessonId).build();
        when(entityManager.getReference(SubjectModel.class, subjectId)).thenReturn(subjectProxy);
        when(entityManager.getReference(LessonModel.class, lessonId)).thenReturn(lessonProxy);
        when(jpaRepository.save(any(SubjectLessonLinkModel.class))).thenReturn(new SubjectLessonLinkModel());

        SubjectLessonLink domain = SubjectLessonLink.builder()
                .subjectId(subjectId.toString())
                .lessonId(lessonId.toString())
                .build();
        repository.save(domain);

        ArgumentCaptor<SubjectLessonLinkModel> captor = ArgumentCaptor.forClass(SubjectLessonLinkModel.class);
        verify(jpaRepository).save(captor.capture());
        assertThat(captor.getValue().getSubject()).isSameAs(subjectProxy);
        assertThat(captor.getValue().getLesson()).isSameAs(lessonProxy);
    }
}
