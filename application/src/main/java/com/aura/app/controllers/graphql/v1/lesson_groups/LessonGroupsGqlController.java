package com.aura.app.controllers.graphql.v1.lesson_groups;

import com.aura.app.codegen.types.CreateLessonGroupInLessonInput;
import com.aura.app.codegen.types.LessonGroupPlanResponse;
import com.aura.app.codegen.types.LessonGroupResponse;
import com.aura.app.codegen.types.LessonResponse;
import com.aura.app.codegen.types.LessonStudentResponse;
import com.aura.app.codegen.types.StudentResponse;
import com.aura.app.mappers.LessonGroupMapper;
import com.aura.app.mappers.LessonGroupPlanMapper;
import com.aura.app.mappers.LessonMapper;
import com.aura.app.mappers.StudentLessonReportMapper;
import com.aura.app.mappers.StudentMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.lessons.LessonGroup;
import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.core.entities.links.StudentLessonReport;
import com.aura.core.entities.students.Student;
import com.aura.core.repositories.LessonGroupPlanRepository;
import com.aura.core.repositories.LessonGroupRepository;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.StudentLessonReportRepository;
import com.aura.core.repositories.StudentRepository;
import com.aura.details.services.LessonGroupPlanService;
import com.aura.details.services.LessonGroupService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LessonGroupsGqlController {

    private final LessonGroupService lessonGroupService;
    private final LessonGroupPlanService lessonGroupPlanService;
    private final LessonGroupRepository lessonGroupRepository;
    private final LessonGroupPlanRepository lessonGroupPlanRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final StudentLessonReportRepository studentLessonReportRepository;

    @PreAuthorize("hasPermission(#input.lessonId, 'lesson', 'create_lesson_plan')")
    @MutationMapping
    public LessonGroupResponse createLessonGroupInLesson(
            @Argument("input") CreateLessonGroupInLessonInput input,
            @ContextValue(name = "user") User user) {
        LessonGroup toCreate = LessonGroupMapper.INSTANCE.toDomainEntity(input);
        LessonGroup created = lessonGroupService.createLessonGroup(toCreate, input.getLessonId());

        // Add students to the group (required by schema even when empty)
        if (!input.getStudentIds().isEmpty()) {
            lessonGroupService.addStudentsToLessonGroup(created.getId(), input.getStudentIds(), created.getTenantId());
        }

        // Create the plan under the new group (schema requires one)
        LessonGroupPlan planToCreate = LessonGroupPlanMapper.INSTANCE.toDomainEntity(input.getPlan());
        List<LessonGroupPlanService.NewLessonObjective> newObjectives = input.getPlan().getLessonObjectives().stream()
                .map(o -> new LessonGroupPlanService.NewLessonObjective(
                        LessonGroupPlanMapper.INSTANCE.toDomainEntity(o), o.getTopicIds()))
                .toList();
        lessonGroupPlanService.createLessonGroupPlan(
                planToCreate, created.getId(),
                input.getPlan().getTopicIds(), input.getPlan().getMaterialIds(), newObjectives);

        return LessonGroupMapper.INSTANCE.toDto(created);
    }

    @BatchMapping(typeName = "LessonGroupResponse", field = "lesson")
    public Map<LessonGroupResponse, LessonResponse> lesson(List<LessonGroupResponse> responses) {
        List<UUID> groupIds = responses.stream().map(r -> UUID.fromString(r.getId())).toList();
        Map<String, LessonGroup> byId = lessonGroupRepository.findAllByIds(groupIds).stream()
                .collect(Collectors.toMap(LessonGroup::getId, Function.identity()));
        List<UUID> lessonIds = byId.values().stream().map(g -> UUID.fromString(g.getLessonId())).toList();
        Map<String, LessonResponse> lessonById = lessonRepository.findAllById(lessonIds).stream()
                .collect(Collectors.toMap(Lesson::getId, LessonMapper.INSTANCE::toResponseDto));
        return responses.stream().collect(Collectors.toMap(
                Function.identity(),
                r -> lessonById.get(byId.get(r.getId()).getLessonId())));
    }

    @BatchMapping(typeName = "LessonGroupResponse", field = "lessonGroupPlan")
    public Map<LessonGroupResponse, LessonGroupPlanResponse> lessonGroupPlan(List<LessonGroupResponse> responses) {
        List<String> groupIds = responses.stream().map(LessonGroupResponse::getId).toList();
        Map<String, LessonGroupPlan> byGroup = lessonGroupPlanRepository.findAllByLessonGroupIds(groupIds);
        return responses.stream()
                .filter(r -> byGroup.containsKey(r.getId()))
                .collect(Collectors.toMap(
                        Function.identity(),
                        r -> LessonGroupPlanMapper.INSTANCE.toDto(byGroup.get(r.getId()))));
    }

    @BatchMapping(typeName = "LessonGroupResponse", field = "lessonStudents")
    public Map<LessonGroupResponse, List<LessonStudentResponse>> lessonStudents(List<LessonGroupResponse> responses) {
        List<String> groupIds = responses.stream().map(LessonGroupResponse::getId).toList();
        Map<String, List<Student>> studentsByGroup = studentRepository.findAllByLessonGroupIds(groupIds);
        Map<String, Map<String, StudentLessonReport>> reportsByGroup =
                studentLessonReportRepository.findReportsByLessonGroupIds(groupIds);
        Map<LessonGroupResponse, List<LessonStudentResponse>> result = new HashMap<>();
        for (LessonGroupResponse r : responses) {
            Map<String, StudentLessonReport> reportsByStudent = reportsByGroup.getOrDefault(r.getId(), Map.of());
            List<LessonStudentResponse> entries = studentsByGroup.getOrDefault(r.getId(), List.of()).stream()
                    .map(s -> {
                        StudentLessonReport report = reportsByStudent.get(s.getId());
                        StudentResponse studentDto = StudentMapper.INSTANCE.toDto(s);
                        LessonStudentResponse entry = new LessonStudentResponse();
                        entry.setStudent(studentDto);
                        entry.setStudentLessonReport(
                                report == null ? null : StudentLessonReportMapper.INSTANCE.toDto(report));
                        return entry;
                    })
                    .toList();
            result.put(r, entries);
        }
        return result;
    }
}
