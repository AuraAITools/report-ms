package com.aura.core.usecases.lesson_groups;

import com.aura.core.entities.lessons.LessonGroup;
import com.aura.core.entities.links.StudentLessonLink;
import com.aura.core.repositories.LessonGroupRepository;
import com.aura.core.repositories.StudentLessonLinkRepository;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;


import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class AddStudentsToLessonGroupUseCaseImpl implements AddStudentsToLessonGroupUseCase {
    private final LessonGroupRepository lessonGroupRepository;
    private final StudentLessonLinkRepository studentLessonLinkRepository;

    @Override
    public List<StudentLessonLink> addStudentsToLessonGroup(String lessonGroupId, List<String> studentIds, String tenantId) {
        if (studentIds.isEmpty()) return emptyList();
        LessonGroup group = ofNullable(lessonGroupRepository.findById(UUID.fromString(lessonGroupId)))
                .orElseThrow(() -> new RuntimeException("no lesson group id found"));
        String lessonId = group.getLessonId();

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
                .assignLessonGroupToStudentLessonLinks(lessonId, studentIds, lessonGroupId);
    }
}
