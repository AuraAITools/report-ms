package com.aura.details.services;

import com.aura.core.entities.lessons.LessonGroup;
import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.core.entities.links.StudentLessonLink;
import com.aura.core.entities.links.StudentLessonReport;
import com.aura.core.repositories.LessonGroupPlanRepository;
import com.aura.core.repositories.LessonGroupPlanTopicLinkRepository;
import com.aura.core.repositories.StudentLessonLinkRepository;
import com.aura.core.usecases.student_lesson_reports.CreateStudentLessonReportUseCase;
import com.aura.core.usecases.student_lesson_reports.DeleteStudentLessonReportUseCase;
import com.aura.core.usecases.student_lesson_reports.StudentLessonReportQueryUseCase;
import com.aura.core.usecases.student_lesson_reports.UpdateStudentLessonReportUseCase;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("StudentLessonReportService")
@ExtendWith(MockitoExtension.class)
class StudentLessonReportServiceTest {

    @Mock
    private CreateStudentLessonReportUseCase createStudentLessonReportUseCase;
    @Mock
    private StudentLessonReportQueryUseCase studentLessonReportQueryUseCase;
    @Mock
    private UpdateStudentLessonReportUseCase updateStudentLessonReportUseCase;
    @Mock
    private DeleteStudentLessonReportUseCase deleteStudentLessonReportUseCase;
    @Mock
    private StudentLessonLinkRepository studentLessonLinkRepository;
    @Mock
    private LessonGroupPlanRepository lessonGroupPlanRepository;
    @Mock
    private LessonGroupPlanTopicLinkRepository lessonGroupPlanTopicLinkRepository;

    @InjectMocks
    private StudentLessonReportService service;

    @DisplayName("Should look up the student lesson links and create one report per link with the plan's topics")
    @Test
    void createReportsForStudentsInGroup_createsReportPerLink() {
        String lessonId = "lesson-1";
        String tenantId = "tenant-A";
        List<String> studentIds = List.of("stu-1", "stu-2");
        List<String> topicIds = List.of("topic-a", "topic-b");

        StudentLessonLink link1 = StudentLessonLink.builder().id("link-1").studentId("stu-1").lessonId(lessonId).build();
        StudentLessonLink link2 = StudentLessonLink.builder().id("link-2").studentId("stu-2").lessonId(lessonId).build();
        when(studentLessonLinkRepository.findAllByLessonIdAndStudentIds(lessonId, studentIds))
                .thenReturn(List.of(link1, link2));

        service.createReportsForStudentsInGroup(lessonId, studentIds, topicIds, tenantId);

        verify(createStudentLessonReportUseCase).createEmptyReport("link-1", topicIds, tenantId);
        verify(createStudentLessonReportUseCase).createEmptyReport("link-2", topicIds, tenantId);
    }

    @DisplayName("Should do nothing when studentIds is empty")
    @Test
    void createReportsForStudentsInGroup_withEmptyStudents_doesNothing() {
        service.createReportsForStudentsInGroup("lesson-1", List.of(), List.of("topic-a"), "tenant-A");

        verifyNoInteractions(studentLessonLinkRepository, createStudentLessonReportUseCase);
    }

    @DisplayName("Should resolve the link's group plan topics and delegate to createEmptyReport")
    @Test
    void createForStudentLessonLink_resolvesPlanTopicsAndDelegates() {
        String linkId = UUID.randomUUID().toString();
        String groupId = "group-1";
        String planId = "plan-1";
        StudentLessonLink link = StudentLessonLink.builder()
                .id(linkId)
                .lessonGroup(LessonGroup.builder().id(groupId).build())
                .build();
        LessonGroupPlan plan = LessonGroupPlan.builder().id(planId).build();
        StudentLessonReport created = StudentLessonReport.builder().id("rep-1").build();

        when(studentLessonLinkRepository.findById(UUID.fromString(linkId))).thenReturn(link);
        when(lessonGroupPlanRepository.findByLessonGroupId(groupId)).thenReturn(plan);
        when(lessonGroupPlanTopicLinkRepository.findTopicIdsByLessonGroupPlanId(planId))
                .thenReturn(Set.of("topic-a", "topic-b"));
        org.mockito.ArgumentCaptor<List<String>> topicsCaptor = org.mockito.ArgumentCaptor.forClass(List.class);
        when(createStudentLessonReportUseCase.createEmptyReport(
                org.mockito.ArgumentMatchers.eq(linkId),
                topicsCaptor.capture(),
                org.mockito.ArgumentMatchers.eq("tenant-A")))
                .thenReturn(created);

        StudentLessonReport result = service.createForStudentLessonLink(linkId, "tenant-A");

        assertThat(result).isSameAs(created);
        assertThat(topicsCaptor.getValue()).containsExactlyInAnyOrder("topic-a", "topic-b");
    }
}
