package com.aura.app.controllers.graphql.v1.lessons;

import com.aura.app.annotations.ProtectedOperation;
import com.aura.app.codegen.DgsConstants;
import com.aura.app.codegen.types.CreateLessonInput;
import com.aura.app.codegen.types.CreateStandaloneLessonInput;
import com.aura.app.codegen.types.LessonResponse;
import com.aura.app.mappers.LessonMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.lessons.Lesson;
import com.aura.details.services.LessonService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;


import static com.aura.app.codegen.DgsConstants.MUTATION.CreateLesson;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateStandaloneLesson;

@Controller
@RequiredArgsConstructor
public class LessonsGqlController {
    private final LessonService lessonService;

    @ProtectedOperation(resource = "lesson", scope = "create")
    @MutationMapping(CreateStandaloneLesson)
    public LessonResponse createStandaloneLesson(@Argument(name = DgsConstants.MUTATION.CREATESTANDALONELESSON_INPUT_ARGUMENT.CreateStandaloneLesson) CreateStandaloneLessonInput createStandaloneLessonInput, @ContextValue(name = "user") User user) {
        Lesson lessonToBeCreated = LessonMapper.INSTANCE.toDomainEntity(createStandaloneLessonInput, user.getCurrentTenantAccessed());
        Lesson lesson = lessonService.createStandaloneLesson(lessonToBeCreated, createStandaloneLessonInput.getOutletId());
        return LessonMapper.INSTANCE.toResponseDto(lesson);
    }

    @ProtectedOperation(resource = "lesson", scope = "create")
    @MutationMapping(CreateLesson)
    public LessonResponse createLesson(@Argument(name = DgsConstants.MUTATION.CREATELESSON_INPUT_ARGUMENT.CreateLesson) CreateLessonInput createLessonInput, @ContextValue(name = "user") User user) {
        Lesson lessonToBeCreated = LessonMapper.INSTANCE.toDomainEntity(createLessonInput, user.getCurrentTenantAccessed());
        Lesson lesson = lessonService.createLesson(
                lessonToBeCreated,
                createLessonInput.getOutletId(),
                user.getCurrentTenantAccessed(),
                createLessonInput.getCourseId(),
                createLessonInput.getEducatorIds());
        return LessonMapper.INSTANCE.toResponseDto(lesson);
    }
}
