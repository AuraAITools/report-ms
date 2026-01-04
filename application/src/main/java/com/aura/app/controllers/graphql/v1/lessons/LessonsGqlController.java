package com.aura.app.controllers.graphql.v1.lessons;

import com.aura.app.codegen.DgsConstants;
import com.aura.app.codegen.types.CreateStandaloneLessonInput;
import com.aura.app.codegen.types.LessonResponse;
import com.aura.app.mappers.LessonMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.lessons.Lesson;
import com.aura.details.services.LessonService;
import java.util.UUID;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;


import static com.aura.app.codegen.DgsConstants.MUTATION.CreateStandaloneLesson;

@Controller
public class LessonsGqlController {
    private final LessonService lessonService;

    public LessonsGqlController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @MutationMapping(CreateStandaloneLesson)
    public LessonResponse createStandaloneLesson(@Argument(name = DgsConstants.MUTATION.CREATESTANDALONELESSON_INPUT_ARGUMENT.CreateStandaloneLesson) CreateStandaloneLessonInput createStandaloneLessonInput, @ContextValue(name = "user") User user) {
        Lesson lessonToBeCreated = LessonMapper.INSTANCE.toDomainEntity(createStandaloneLessonInput, user.getCurrentTenantAccessed());
        Lesson lesson = lessonService.createStandaloneLesson(lessonToBeCreated, UUID.fromString(createStandaloneLessonInput.getOutletId()));
        return LessonMapper.INSTANCE.toResponseDto(lesson);
    }
}
