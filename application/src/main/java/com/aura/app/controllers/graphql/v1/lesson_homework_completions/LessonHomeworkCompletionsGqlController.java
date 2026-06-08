package com.aura.app.controllers.graphql.v1.lesson_homework_completions;

import com.aura.app.codegen.types.DeleteLessonHomeworkCompletionInput;
import com.aura.app.codegen.types.LessonHomeworkCompletionResponse;
import com.aura.app.codegen.types.UpdateLessonHomeworkCompletionInput;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.app.mappers.LessonHomeworkCompletionMapper;
import com.aura.core.auth.User;
import java.util.Optional;
import java.util.UUID;
import com.aura.details.services.LessonHomeworkCompletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LessonHomeworkCompletionsGqlController {

    private final LessonHomeworkCompletionService lessonHomeworkCompletionService;

    @PreAuthorize("hasPermission(#lessonId, 'lesson', 'view')")
    @QueryMapping
    public LessonHomeworkCompletionResponse getLessonHomeworkCompletionById(
            @Argument("institutionId") String institutionId,
            @Argument("lessonId") String lessonId,
            @Argument("id") String id,
            @ContextValue(name = "user") User user) {
        return Optional.ofNullable(lessonHomeworkCompletionService.findById(UUID.fromString(id)))
                .map(LessonHomeworkCompletionMapper.INSTANCE::toDto)
                .orElse(null);
    }

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'update')")
    @MutationMapping
    public LessonHomeworkCompletionResponse updateLessonHomeworkCompletion(
            @Argument("input") UpdateLessonHomeworkCompletionInput input,
            @ContextValue(name = "user") User user) {
        return Optional.ofNullable(lessonHomeworkCompletionService.update(input.getId(), input.getCompletion()))
                .map(LessonHomeworkCompletionMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("LessonHomeworkCompletion"));
    }

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'update')")
    @MutationMapping
    public Boolean deleteLessonHomeworkCompletion(
            @Argument("input") DeleteLessonHomeworkCompletionInput input,
            @ContextValue(name = "user") User user) {
        lessonHomeworkCompletionService.deleteById(UUID.fromString(input.getId()));
        return true;
    }
}
