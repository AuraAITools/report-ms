package com.aura.app.controllers.graphql.v1.lesson_objectives;

import com.aura.app.codegen.types.CreateLessonObjectiveInput;
import com.aura.app.codegen.types.DeleteLessonObjectiveByIdInput;
import com.aura.app.codegen.types.LessonObjectiveResponse;
import com.aura.app.codegen.types.UpdateLessonObjectiveInput;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.app.mappers.LessonObjectiveMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.lessons.LessonObjective;
import com.aura.details.services.LessonObjectiveService;
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
public class LessonObjectivesGqlController {

    private final LessonObjectiveService lessonObjectiveService;

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'create_lesson_objective')")
    @MutationMapping
    public LessonObjectiveResponse createLessonObjective(@Argument CreateLessonObjectiveInput input,
                                                         @ContextValue(name = "user") User user) {
        LessonObjective toCreate = LessonObjectiveMapper.INSTANCE.toDomainEntity(input);
        toCreate.setLessonPlanId(input.getLessonPlanId());
        LessonObjective created = lessonObjectiveService.createLessonObjective(toCreate, input.getTopicIds());
        return LessonObjectiveMapper.INSTANCE.toDto(created);
    }

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'update')")
    @MutationMapping
    public LessonObjectiveResponse updateLessonObjective(@Argument UpdateLessonObjectiveInput input,
                                                         @ContextValue(name = "user") User user) {
        LessonObjective toUpdate = LessonObjectiveMapper.INSTANCE.toDomainEntity(input);
        toUpdate.setId(input.getId());
        return Optional.ofNullable(lessonObjectiveService.updateLessonObjective(toUpdate, input.getTopicIds()))
                .map(LessonObjectiveMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("LessonObjective"));
    }

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'delete')")
    @MutationMapping
    public Boolean deleteLessonObjectiveById(@Argument DeleteLessonObjectiveByIdInput input,
                                             @ContextValue(name = "user") User user) {
        if (lessonObjectiveService.findById(input.getId()) == null) {
            throw new ResourceNotFoundBusinessException("LessonObjective");
        }
        lessonObjectiveService.deleteById(input.getId());
        return true;
    }

    @PreAuthorize("hasPermission(#lessonId, 'lesson', 'view')")
    @QueryMapping
    public LessonObjectiveResponse getLessonObjectiveById(@Argument("lessonId") String lessonId,
                                                          @Argument("id") String id,
                                                          @ContextValue(name = "user") User user) {
        return Optional.ofNullable(lessonObjectiveService.findById(id))
                .map(LessonObjectiveMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("LessonObjective"));
    }

    @PreAuthorize("hasPermission(#lessonId, 'lesson', 'view')")
    @QueryMapping
    public List<LessonObjectiveResponse> getAllLessonObjectivesByLessonPlanId(
            @Argument("lessonId") String lessonId,
            @Argument("lessonPlanId") String lessonPlanId,
            @ContextValue(name = "user") User user) {
        return lessonObjectiveService.findAllByLessonPlanId(lessonPlanId).stream()
                .map(LessonObjectiveMapper.INSTANCE::toDto)
                .toList();
    }
}
