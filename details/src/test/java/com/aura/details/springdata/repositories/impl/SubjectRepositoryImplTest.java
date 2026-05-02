package com.aura.details.springdata.repositories.impl;

import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.links.SubjectCourseLinkModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import com.aura.details.springdata.repositories.jpa.courses.JpaCourseRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectCourseLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaTopicSubjectLinkRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Subject repository implementation")
@ExtendWith(MockitoExtension.class)
class SubjectRepositoryImplTest {

    @Mock private JpaSubjectRepository jpaSubjectRepository;
    @Mock private JpaTopicRepository jpaTopicRepository;
    @Mock private JpaTopicSubjectLinkRepository jpaTopicSubjectLinkRepository;
    @Mock private JpaCourseRepository jpaCourseRepository;
    @Mock private JpaSubjectCourseLinkRepository jpaSubjectCourseLinkRepository;

    @InjectMocks private SubjectRepositoryImpl subjectRepositoryImpl;

    @DisplayName("Should create subject-course links for each subject ID")
    @Test
    void createSubjectCourseLinks_createsLinkForEachSubject() {
        String courseId = UUID.randomUUID().toString();
        String subjectId1 = UUID.randomUUID().toString();
        String subjectId2 = UUID.randomUUID().toString();
        String tenantId = "tenant-1";

        CourseModel courseModel = CourseModel.builder().build();
        SubjectModel subjectModel1 = SubjectModel.builder().build();
        SubjectModel subjectModel2 = SubjectModel.builder().build();

        when(jpaCourseRepository.getReferenceById(UUID.fromString(courseId))).thenReturn(courseModel);
        when(jpaSubjectRepository.getReferenceById(UUID.fromString(subjectId1))).thenReturn(subjectModel1);
        when(jpaSubjectRepository.getReferenceById(UUID.fromString(subjectId2))).thenReturn(subjectModel2);

        subjectRepositoryImpl.createSubjectCourseLinks(List.of(subjectId1, subjectId2), courseId, tenantId);

        ArgumentCaptor<SubjectCourseLinkModel> captor = ArgumentCaptor.forClass(SubjectCourseLinkModel.class);
        verify(jpaSubjectCourseLinkRepository, times(2)).save(captor.capture());

        List<SubjectCourseLinkModel> savedLinks = captor.getAllValues();
        assertThat(savedLinks).hasSize(2);
        assertThat(savedLinks.get(0).getCourse()).isEqualTo(courseModel);
        assertThat(savedLinks.get(0).getSubject()).isEqualTo(subjectModel1);
        assertThat(savedLinks.get(0).getTenantId()).isEqualTo(tenantId);
        assertThat(savedLinks.get(1).getSubject()).isEqualTo(subjectModel2);
    }

    @DisplayName("Should not create any links when subject list is empty")
    @Test
    void createSubjectCourseLinks_emptyList_doesNothing() {
        subjectRepositoryImpl.createSubjectCourseLinks(List.of(), "course-1", "tenant-1");

        verifyNoInteractions(jpaSubjectCourseLinkRepository);
        verifyNoInteractions(jpaCourseRepository);
    }
}
