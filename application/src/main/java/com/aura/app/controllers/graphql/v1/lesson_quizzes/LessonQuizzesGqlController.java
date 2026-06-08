package com.aura.app.controllers.graphql.v1.lesson_quizzes;

import com.aura.app.codegen.types.CreateLessonQuizInput;
import com.aura.app.codegen.types.DeleteLessonQuizByIdInput;
import com.aura.app.codegen.types.LessonQuizResponse;
import com.aura.app.codegen.types.UpdateLessonQuizInput;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.app.mappers.LessonQuizMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.lessons.LessonQuiz;
import com.aura.details.services.LessonQuizService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LessonQuizzesGqlController {

    private final LessonQuizService lessonQuizService;

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'create_lesson_quiz')")
    @MutationMapping
    public LessonQuizResponse createLessonQuiz(@Argument CreateLessonQuizInput input,
                                                @ContextValue(name = "user") User user) {
        LessonQuiz toCreate = LessonQuizMapper.INSTANCE.toDomainEntity(input);
        toCreate.setLessonGroupPlanId(input.getLessonGroupPlanId());
        LessonQuiz created = lessonQuizService.createLessonQuiz(toCreate);
        return LessonQuizMapper.INSTANCE.toDto(created);
    }

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'update')")
    @MutationMapping
    public LessonQuizResponse updateLessonQuiz(@Argument UpdateLessonQuizInput input,
                                                @ContextValue(name = "user") User user) {
        LessonQuiz toUpdate = LessonQuizMapper.INSTANCE.toDomainEntity(input);
        toUpdate.setId(input.getId());
        return Optional.ofNullable(lessonQuizService.updateLessonQuiz(toUpdate))
                .map(LessonQuizMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("LessonQuiz"));
    }

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'delete')")
    @MutationMapping
    public Boolean deleteLessonQuizById(@Argument DeleteLessonQuizByIdInput input,
                                         @ContextValue(name = "user") User user) {
        if (lessonQuizService.findById(input.getId()) == null) {
            throw new ResourceNotFoundBusinessException("LessonQuiz");
        }
        lessonQuizService.deleteById(input.getId());
        return true;
    }

    @PreAuthorize("hasPermission(#lessonId, 'lesson', 'view')")
    @QueryMapping
    public LessonQuizResponse getLessonQuizById(@Argument("institutionId") String institutionId,
                                                 @Argument("lessonId") String lessonId,
                                                 @Argument("id") String id,
                                                 @ContextValue(name = "user") User user) {
        return Optional.ofNullable(lessonQuizService.findById(id))
                .map(LessonQuizMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("LessonQuiz"));
    }

    @PreAuthorize("hasPermission(#lessonId, 'lesson', 'view')")
    @QueryMapping
    public List<LessonQuizResponse> getAllLessonQuizzesByLessonGroupPlanId(
            @Argument("institutionId") String institutionId,
            @Argument("lessonId") String lessonId,
            @Argument("lessonGroupPlanId") String lessonGroupPlanId,
            @ContextValue(name = "user") User user) {
        return lessonQuizService.findAllByLessonGroupPlanId(lessonGroupPlanId).stream()
                .map(LessonQuizMapper.INSTANCE::toDto)
                .toList();
    }
}
