package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.EducatorLessonLink;
import com.aura.details.springdata.models.educators.EducatorModel;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.links.EducatorLessonLinkModel;
import com.aura.details.springdata.repositories.jpa.links.JpaEducatorLessonLinkRepository;
import jakarta.persistence.EntityManager;
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
import static org.mockito.Mockito.when;

@DisplayName("Educator lesson link repository impl")
@ExtendWith(MockitoExtension.class)
class EducatorLessonLinkRepositoryImplTest {

    @Mock
    private JpaEducatorLessonLinkRepository jpaRepository;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private EducatorLessonLinkRepositoryImpl repository;

    @DisplayName("Should attach managed educator and lesson proxies via EntityManager.getReference before persisting")
    @Test
    void saveAttachesEducatorAndLessonRefs() {
        UUID educatorId = UUID.randomUUID();
        UUID lessonId = UUID.randomUUID();
        EducatorModel educatorProxy = EducatorModel.builder().id(educatorId).build();
        LessonModel lessonProxy = LessonModel.builder().id(lessonId).build();
        when(entityManager.getReference(EducatorModel.class, educatorId)).thenReturn(educatorProxy);
        when(entityManager.getReference(LessonModel.class, lessonId)).thenReturn(lessonProxy);
        when(jpaRepository.save(any(EducatorLessonLinkModel.class))).thenReturn(new EducatorLessonLinkModel());

        EducatorLessonLink domain = EducatorLessonLink.builder()
                .educatorId(educatorId.toString())
                .lessonId(lessonId.toString())
                .build();
        repository.save(domain);

        ArgumentCaptor<EducatorLessonLinkModel> captor = ArgumentCaptor.forClass(EducatorLessonLinkModel.class);
        verify(jpaRepository).save(captor.capture());
        assertThat(captor.getValue().getEducator()).isSameAs(educatorProxy);
        assertThat(captor.getValue().getLesson()).isSameAs(lessonProxy);
    }
}
