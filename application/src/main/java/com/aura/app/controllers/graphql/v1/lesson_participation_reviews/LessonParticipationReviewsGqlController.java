package com.aura.app.controllers.graphql.v1.lesson_participation_reviews;

import com.aura.app.codegen.types.CreateLessonParticipationReviewInput;
import com.aura.app.codegen.types.DeleteLessonParticipationReviewInput;
import com.aura.app.codegen.types.LessonParticipationReviewResponse;
import com.aura.app.codegen.types.UpdateLessonParticipationReviewInput;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.app.mappers.LessonParticipationReviewMapper;
import com.aura.core.auth.User;
import com.aura.details.services.LessonParticipationReviewService;
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
public class LessonParticipationReviewsGqlController {

    private final LessonParticipationReviewService lessonParticipationReviewService;

    @PreAuthorize("hasPermission(#lessonId, 'lesson', 'view')")
    @QueryMapping
    public LessonParticipationReviewResponse getLessonParticipationReviewById(
            @Argument("institutionId") String institutionId,
            @Argument("lessonId") String lessonId,
            @Argument("id") String id,
            @ContextValue(name = "user") User user) {
        return Optional.ofNullable(lessonParticipationReviewService.findById(UUID.fromString(id)))
                .map(LessonParticipationReviewMapper.INSTANCE::toDto)
                .orElse(null);
    }

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'update')")
    @MutationMapping
    public LessonParticipationReviewResponse createLessonParticipationReview(
            @Argument("input") CreateLessonParticipationReviewInput input,
            @ContextValue(name = "user") User user) {
        return LessonParticipationReviewMapper.INSTANCE.toDto(
                lessonParticipationReviewService.create(input.getStudentLessonReportId(),
                        LessonParticipationReviewMapper.INSTANCE.toDomainEnum(input.getParticipationType()),
                        user.getCurrentTenantAccessed()));
    }

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'update')")
    @MutationMapping
    public LessonParticipationReviewResponse updateLessonParticipationReview(
            @Argument("input") UpdateLessonParticipationReviewInput input,
            @ContextValue(name = "user") User user) {
        return Optional.ofNullable(lessonParticipationReviewService.update(
                        input.getId(),
                        LessonParticipationReviewMapper.INSTANCE.toDomainEnum(input.getParticipationType())))
                .map(LessonParticipationReviewMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("LessonParticipationReview"));
    }

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'update')")
    @MutationMapping
    public Boolean deleteLessonParticipationReview(
            @Argument("input") DeleteLessonParticipationReviewInput input,
            @ContextValue(name = "user") User user) {
        lessonParticipationReviewService.deleteById(UUID.fromString(input.getId()));
        return true;
    }
}
