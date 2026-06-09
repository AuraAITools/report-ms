package com.aura.app.controllers.graphql.v1.lesson_topic_proficiencies;

import com.aura.app.codegen.types.CreateLessonTopicProficiencyInput;
import com.aura.app.codegen.types.DeleteLessonTopicProficiencyInput;
import com.aura.app.codegen.types.LessonTopicProficiencyResponse;
import com.aura.app.codegen.types.UpdateLessonTopicProficiencyInput;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.app.mappers.LessonTopicProficiencyMapper;
import com.aura.core.auth.User;
import com.aura.details.services.LessonTopicProficiencyService;
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
public class LessonTopicProficienciesGqlController {

    private final LessonTopicProficiencyService lessonTopicProficiencyService;

    @PreAuthorize("hasPermission(#lessonId, 'lesson', 'view')")
    @QueryMapping
    public LessonTopicProficiencyResponse getLessonTopicProficiencyById(
            @Argument("institutionId") String institutionId,
            @Argument("lessonId") String lessonId,
            @Argument("id") String id,
            @ContextValue(name = "user") User user) {
        return Optional.ofNullable(lessonTopicProficiencyService.findById(UUID.fromString(id)))
                .map(LessonTopicProficiencyMapper.INSTANCE::toDto)
                .orElse(null);
    }

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'update')")
    @MutationMapping
    public LessonTopicProficiencyResponse createLessonTopicProficiency(
            @Argument("input") CreateLessonTopicProficiencyInput input,
            @ContextValue(name = "user") User user) {
        return LessonTopicProficiencyMapper.INSTANCE.toDto(
                lessonTopicProficiencyService.create(input.getStudentLessonReportId(),
                        input.getTopicId(), input.getRating(), user.getCurrentTenantAccessed()));
    }

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'update')")
    @MutationMapping
    public LessonTopicProficiencyResponse updateLessonTopicProficiency(
            @Argument("input") UpdateLessonTopicProficiencyInput input,
            @ContextValue(name = "user") User user) {
        return Optional.ofNullable(lessonTopicProficiencyService.update(input.getId(), input.getRating()))
                .map(LessonTopicProficiencyMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("LessonTopicProficiency"));
    }

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'update')")
    @MutationMapping
    public Boolean deleteLessonTopicProficiency(
            @Argument("input") DeleteLessonTopicProficiencyInput input,
            @ContextValue(name = "user") User user) {
        lessonTopicProficiencyService.deleteById(UUID.fromString(input.getId()));
        return true;
    }
}
