package com.aura.app.controllers.graphql.v1.student_lesson_reports;

import com.aura.app.codegen.types.CreateStudentLessonReportInput;
import com.aura.app.codegen.types.DeleteStudentLessonReportInput;
import com.aura.app.codegen.types.StudentLessonReportResponse;
import com.aura.app.codegen.types.UpdateStudentLessonReportInput;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.app.mappers.LessonParticipationReviewMapper;
import com.aura.app.mappers.StudentLessonReportMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.links.StudentLessonReport;
import com.aura.core.usecases.student_lesson_reports.UpdateStudentLessonReportUseCase;
import com.aura.details.services.StudentLessonReportService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StudentLessonReportsGqlController {

    private final StudentLessonReportService studentLessonReportService;

    @PreAuthorize("hasPermission(#lessonId, 'lesson', 'view')")
    @QueryMapping
    public StudentLessonReportResponse getStudentLessonReportById(
            @Argument("institutionId") String institutionId,
            @Argument("lessonId") String lessonId,
            @Argument("id") String id,
            @ContextValue(name = "user") User user) {
        return Optional.ofNullable(studentLessonReportService.findById(UUID.fromString(id)))
                .map(StudentLessonReportMapper.INSTANCE::toDto)
                .orElse(null);
    }

    @PreAuthorize("hasPermission(#lessonId, 'lesson', 'view')")
    @QueryMapping
    public List<StudentLessonReportResponse> getStudentLessonReportsByLessonGroup(
            @Argument("institutionId") String institutionId,
            @Argument("lessonId") String lessonId,
            @Argument("lessonGroupId") String lessonGroupId,
            @ContextValue(name = "user") User user) {
        return studentLessonReportService.findAllByLessonGroupId(lessonGroupId).stream()
                .map(StudentLessonReportMapper.INSTANCE::toDto)
                .toList();
    }

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'update')")
    @MutationMapping
    public StudentLessonReportResponse createStudentLessonReport(
            @Argument("input") CreateStudentLessonReportInput input,
            @ContextValue(name = "user") User user) {
        StudentLessonReport created = studentLessonReportService.createForStudentLessonLink(
                input.getStudentLessonLinkId(), user.getCurrentTenantAccessed());
        return StudentLessonReportMapper.INSTANCE.toDto(created);
    }

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'update')")
    @MutationMapping
    public StudentLessonReportResponse updateStudentLessonReport(
            @Argument("input") UpdateStudentLessonReportInput input,
            @ContextValue(name = "user") User user) {
        List<UpdateStudentLessonReportUseCase.TopicProficiencyUpdate> topicUpdates =
                input.getTopicProficiencies() == null ? List.of()
                        : input.getTopicProficiencies().stream()
                                .map(p -> new UpdateStudentLessonReportUseCase.TopicProficiencyUpdate(p.getId(), p.getRating()))
                                .toList();
        StudentLessonReport updated = studentLessonReportService.update(
                input.getId(),
                input.getHomeworkCompletion(),
                LessonParticipationReviewMapper.INSTANCE.toDomainEnum(input.getParticipationType()),
                topicUpdates);
        return Optional.ofNullable(updated)
                .map(StudentLessonReportMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("StudentLessonReport"));
    }

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'update')")
    @MutationMapping
    public Boolean deleteStudentLessonReport(
            @Argument("input") DeleteStudentLessonReportInput input,
            @ContextValue(name = "user") User user) {
        studentLessonReportService.deleteById(UUID.fromString(input.getId()));
        return true;
    }
}
