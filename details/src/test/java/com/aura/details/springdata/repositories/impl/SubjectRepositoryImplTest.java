package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.subjects.Subject;
import com.aura.core.entities.topics.Topic;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.links.SubjectCourseLinkModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import com.aura.details.springdata.models.topics.TopicModel;
import com.aura.details.springdata.repositories.jpa.courses.JpaCourseRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectCourseLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectLessonLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaTopicSubjectLinkRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicRepository;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("Subject repository implementation")
@ExtendWith(MockitoExtension.class)
class SubjectRepositoryImplTest {

    @Mock
    private JpaSubjectRepository jpaSubjectRepository;
    @Mock
    private JpaTopicRepository jpaTopicRepository;
    @Mock
    private JpaTopicSubjectLinkRepository jpaTopicSubjectLinkRepository;
    @Mock
    private JpaCourseRepository jpaCourseRepository;
    @Mock
    private JpaSubjectCourseLinkRepository jpaSubjectCourseLinkRepository;
    @Mock
    private JpaSubjectLessonLinkRepository jpaSubjectLessonLinkRepository;
    @Mock
    private JpaSubjectEducatorLinkRepository jpaSubjectEducatorLinkRepository;

    @InjectMocks
    private SubjectRepositoryImpl subjectRepositoryImpl;

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

    @DisplayName("Should group subjects by course id from link table query")
    @Test
    void findAllByCourseIds_groupsSubjectsByCourse() {
        UUID courseId1 = UUID.randomUUID();
        UUID courseId2 = UUID.randomUUID();
        UUID subject1Id = UUID.randomUUID();
        UUID subject2Id = UUID.randomUUID();
        SubjectModel sub1 = SubjectModel.builder().id(subject1Id).build();
        SubjectModel sub2 = SubjectModel.builder().id(subject2Id).build();

        when(jpaSubjectCourseLinkRepository.findSubjectsByCourseIds(List.of(courseId1, courseId2)))
                .thenReturn(List.of(
                        new Object[]{courseId1, sub1},
                        new Object[]{courseId2, sub2}
                ));

        Map<String, List<Subject>> result = subjectRepositoryImpl
                .findAllByCourseIds(List.of(courseId1.toString(), courseId2.toString()));

        assertThat(result).hasSize(2);
        assertThat(result.get(courseId1.toString())).extracting(Subject::getId)
                .containsExactly(subject1Id.toString());
        assertThat(result.get(courseId2.toString())).extracting(Subject::getId)
                .containsExactly(subject2Id.toString());
    }

    @DisplayName("Should return empty map when course id list is empty for findAllByCourseIds")
    @Test
    void findAllByCourseIds_emptyList_returnsEmptyMap() {
        assertThat(subjectRepositoryImpl.findAllByCourseIds(List.of())).isEmpty();
    }

    @DisplayName("Should return the subject found by id")
    @Test
    void findById_returnsSubject() {
        UUID id = UUID.randomUUID();
        SubjectModel model = SubjectModel.builder().id(id).name("Math").build();
        when(jpaSubjectRepository.findById(id)).thenReturn(java.util.Optional.of(model));

        Subject result = subjectRepositoryImpl.findById(id.toString());
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Math");
    }

    @DisplayName("Should return null when subject not found by id")
    @Test
    void findById_returnsNullWhenMissing() {
        UUID id = UUID.randomUUID();
        when(jpaSubjectRepository.findById(id)).thenReturn(java.util.Optional.empty());
        assertThat(subjectRepositoryImpl.findById(id.toString())).isNull();
    }

    @DisplayName("Should return all subjects for the given tenant id")
    @Test
    void findAllByTenantId_returnsSubjects() {
        SubjectModel s1 = SubjectModel.builder().id(UUID.randomUUID()).name("Math").build();
        SubjectModel s2 = SubjectModel.builder().id(UUID.randomUUID()).name("Science").build();
        when(jpaSubjectRepository.findByTenantId("t1")).thenReturn(List.of(s1, s2));

        assertThat(subjectRepositoryImpl.findAllByTenantId("t1")).extracting(Subject::getName)
                .containsExactly("Math", "Science");
    }

    @DisplayName("Should return empty list when no subjects in tenant")
    @Test
    void findAllByTenantId_returnsEmpty() {
        when(jpaSubjectRepository.findByTenantId("t1")).thenReturn(List.of());
        assertThat(subjectRepositoryImpl.findAllByTenantId("t1")).isEmpty();
    }

    @DisplayName("Should group topics by subject id from link table query")
    @Test
    void findTopicsBySubjectIds_groupsTopicsBySubject() {
        UUID subjectId1 = UUID.randomUUID();
        UUID subjectId2 = UUID.randomUUID();
        UUID topic1Id = UUID.randomUUID();
        UUID topic2Id = UUID.randomUUID();
        TopicModel t1 = TopicModel.builder().id(topic1Id).build();
        TopicModel t2 = TopicModel.builder().id(topic2Id).build();

        when(jpaTopicSubjectLinkRepository.findTopicsBySubjectIds(List.of(subjectId1, subjectId2)))
                .thenReturn(List.of(
                        new Object[]{subjectId1, t1},
                        new Object[]{subjectId1, t2}
                ));

        Map<String, List<Topic>> result = subjectRepositoryImpl
                .findTopicsBySubjectIds(List.of(subjectId1.toString(), subjectId2.toString()));

        assertThat(result).hasSize(1);
        assertThat(result.get(subjectId1.toString())).extracting(Topic::getId)
                .containsExactly(topic1Id.toString(), topic2Id.toString());
    }

    @DisplayName("Should return empty map when subject id list is empty")
    @Test
    void findTopicsBySubjectIds_emptyList_returnsEmptyMap() {
        assertThat(subjectRepositoryImpl.findTopicsBySubjectIds(List.of())).isEmpty();
    }

    @DisplayName("Should return empty map when no educator ids provided to findAllByEducatorIds")
    @Test
    void findAllByEducatorIds_emptyList_returnsEmptyMap() {
        assertThat(subjectRepositoryImpl.findAllByEducatorIds(List.of())).isEmpty();
    }

    @DisplayName("Should group subjects by educator id from link table query")
    @Test
    void findAllByEducatorIds_groupsSubjectsByEducator() {
        UUID educatorId1 = UUID.randomUUID();
        UUID educatorId2 = UUID.randomUUID();
        UUID subject1Id = UUID.randomUUID();
        UUID subject2Id = UUID.randomUUID();
        SubjectModel sub1 = SubjectModel.builder().id(subject1Id).build();
        SubjectModel sub2 = SubjectModel.builder().id(subject2Id).build();

        when(jpaSubjectEducatorLinkRepository.findSubjectsByEducatorIds(List.of(educatorId1, educatorId2)))
                .thenReturn(List.of(
                        new Object[]{educatorId1, sub1},
                        new Object[]{educatorId2, sub2}
                ));

        Map<String, List<Subject>> result = subjectRepositoryImpl
                .findAllByEducatorIds(List.of(educatorId1.toString(), educatorId2.toString()));

        assertThat(result).hasSize(2);
        assertThat(result.get(educatorId1.toString())).extracting(Subject::getId).containsExactly(subject1Id.toString());
        assertThat(result.get(educatorId2.toString())).extracting(Subject::getId).containsExactly(subject2Id.toString());
    }
}
