package com.aura.details.services;

import com.aura.core.entities.lessons.LessonGroup;
import com.aura.core.entities.links.StudentLessonLink;
import com.aura.core.repositories.LessonGroupRepository;
import com.aura.core.usecases.lesson_groups.AddStudentsToLessonGroupUseCase;
import com.aura.core.usecases.lesson_groups.CreateLessonGroupUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonGroupService {

    private final CreateLessonGroupUseCase createLessonGroupUseCase;
    private final AddStudentsToLessonGroupUseCase addStudentsToLessonGroupUseCase;
    private final LessonGroupRepository lessonGroupRepository;

    @Transactional
    public LessonGroup createLessonGroup(LessonGroup group, String lessonId) {
        return createLessonGroupUseCase.createLessonGroup(group, lessonId);
    }

    @Transactional
    public List<StudentLessonLink> addStudentsToLessonGroup(String lessonGroupId, List<String> studentIds, String tenantId) {
        return addStudentsToLessonGroupUseCase.addStudentsToLessonGroup(lessonGroupId, studentIds, tenantId);
    }

    public @Nullable LessonGroup findById(UUID id) {
        return lessonGroupRepository.findById(id);
    }

    public List<LessonGroup> findAllByLessonId(String lessonId) {
        return lessonGroupRepository.findAllByLessonId(lessonId);
    }
}
