package com.aura.core.usecases.lesson_plans;

import com.aura.core.entities.lessons.LessonPlan;
import com.aura.core.entities.links.StudentLessonLink;
import com.aura.core.repositories.LessonPlanRepository;
import com.aura.core.repositories.StudentLessonLinkRepository;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;


import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class AddStudentsToLessonPlanUseCaseImpl implements AddStudentsToLessonPlanUseCase {
    private final LessonPlanRepository lessonPlanRepository;
    private final StudentLessonLinkRepository studentLessonLinkRepository;

    @Override
    public List<StudentLessonLink> addStudentsToLessonPlan(String planId, List<String> studentIds, String tenantId) {
        if (studentIds.isEmpty()) return emptyList();
        LessonPlan plan = ofNullable(lessonPlanRepository.findById(UUID.fromString(planId)))
                .orElseThrow(() -> new RuntimeException("no lesson plan id found"));
        String lessonId = plan.getLessonId();

        List<StudentLessonLink> existing = studentLessonLinkRepository
                .findAllByLessonIdAndStudentIds(lessonId, studentIds);
        Set<String> foundStudentIds = existing.stream()
                .map(StudentLessonLink::getStudentId)
                .collect(Collectors.toSet());
        List<String> missing = studentIds.stream()
                .filter(id -> !foundStudentIds.contains(id))
                .toList();
        if (!missing.isEmpty()) {
            throw new RuntimeException("students not in lesson: " + missing);
        }

        return studentLessonLinkRepository
                .assignLessonPlanToStudentLessonLinks(lessonId, studentIds, plan.getId());
    }
}
