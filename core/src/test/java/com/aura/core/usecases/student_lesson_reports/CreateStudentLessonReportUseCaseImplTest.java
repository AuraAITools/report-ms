package com.aura.core.usecases.student_lesson_reports;

import com.aura.core.entities.lessons.LessonHomeworkCompletion;
import com.aura.core.entities.lessons.LessonParticipationReview;
import com.aura.core.entities.lessons.LessonTopicProficiency;
import com.aura.core.entities.links.StudentLessonReport;
import com.aura.core.repositories.StudentLessonReportRepository;
import java.util.List;
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

@DisplayName("CreateStudentLessonReportUseCaseImpl")
@ExtendWith(MockitoExtension.class)
class CreateStudentLessonReportUseCaseImplTest {

    @Mock
    private StudentLessonReportRepository studentLessonReportRepository;

    @InjectMocks
    private CreateStudentLessonReportUseCaseImpl useCase;

    @DisplayName("Should build a report with empty homework completion, UNFILLED participation review, and a topic proficiency per topicId, then save it")
    @Test
    void createEmptyReport_buildsEmptyChildrenAndDelegatesToRepository() {
        String linkId = "link-1";
        String tenantId = "tenant-X";
        List<String> topicIds = List.of("topic-a", "topic-b", "topic-c");
        when(studentLessonReportRepository.save(any(StudentLessonReport.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        StudentLessonReport result = useCase.createEmptyReport(linkId, topicIds, tenantId);

        ArgumentCaptor<StudentLessonReport> captor = ArgumentCaptor.forClass(StudentLessonReport.class);
        verify(studentLessonReportRepository).save(captor.capture());
        StudentLessonReport saved = captor.getValue();

        assertThat(saved.getStudentLessonLinkId()).isEqualTo(linkId);
        assertThat(saved.getTenantId()).isEqualTo(tenantId);

        LessonHomeworkCompletion hwc = saved.getLessonHomeworkCompletion();
        assertThat(hwc).isNotNull();
        assertThat(hwc.getCompletion()).isNull();
        assertThat(hwc.getTenantId()).isEqualTo(tenantId);

        LessonParticipationReview review = saved.getLessonParticipationReview();
        assertThat(review).isNotNull();
        assertThat(review.getParticipationType())
                .isEqualTo(LessonParticipationReview.PARTICIPATION_TYPE.UNFILLED);
        assertThat(review.getTenantId()).isEqualTo(tenantId);

        List<LessonTopicProficiency> proficiencies = saved.getLessonTopicProficiencies();
        assertThat(proficiencies).hasSize(3);
        assertThat(proficiencies).extracting(LessonTopicProficiency::getTopicId)
                .containsExactly("topic-a", "topic-b", "topic-c");
        assertThat(proficiencies).allSatisfy(p -> {
            assertThat(p.getTenantId()).isEqualTo(tenantId);
            assertThat(p.getRating()).isNull();
        });

        assertThat(result).isSameAs(saved);
    }

    @DisplayName("Should produce a report with no proficiencies when topicIds is empty")
    @Test
    void createEmptyReport_withNoTopics_producesEmptyProficiencies() {
        when(studentLessonReportRepository.save(any(StudentLessonReport.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        useCase.createEmptyReport("link-2", List.of(), "tenant-Y");

        ArgumentCaptor<StudentLessonReport> captor = ArgumentCaptor.forClass(StudentLessonReport.class);
        verify(studentLessonReportRepository).save(captor.capture());

        assertThat(captor.getValue().getLessonTopicProficiencies()).isEmpty();
    }
}
