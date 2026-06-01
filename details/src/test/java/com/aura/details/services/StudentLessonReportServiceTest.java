package com.aura.details.services;

import com.aura.core.entities.links.StudentLessonLink;
import com.aura.core.repositories.StudentLessonLinkRepository;
import com.aura.core.usecases.student_lesson_reports.CreateStudentLessonReportUseCase;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("StudentLessonReportService")
@ExtendWith(MockitoExtension.class)
class StudentLessonReportServiceTest {

    @Mock
    private CreateStudentLessonReportUseCase createStudentLessonReportUseCase;
    @Mock
    private StudentLessonLinkRepository studentLessonLinkRepository;

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
}
