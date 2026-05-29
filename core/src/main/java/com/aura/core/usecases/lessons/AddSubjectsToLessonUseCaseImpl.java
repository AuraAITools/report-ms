package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.links.SubjectLessonLink;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.SubjectLessonLinkRepository;
import com.aura.core.repositories.SubjectRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;


import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class AddSubjectsToLessonUseCaseImpl implements AddSubjectsToLessonUseCase {
    private final SubjectLessonLinkRepository subjectLessonLinkRepository;
    private final LessonRepository lessonRepository;
    private final SubjectRepository subjectRepository;

    @Override
    public List<SubjectLessonLink> addSubjectsToLesson(String lessonId, List<String> subjectIds, String tenantId) {
        if (subjectIds.isEmpty()) return emptyList();
        Lesson lesson = ofNullable(lessonRepository.findById(UUID.fromString(lessonId)))
                .orElseThrow(() -> new RuntimeException("no lesson id found"));
        List<Subject> subjects = subjectRepository.findAllByIds(subjectIds.stream().map(UUID::fromString).toList());
        List<SubjectLessonLink> subjectLessonLinks = subjects.stream()
                .map(subject -> SubjectLessonLink
                        .builder()
                        .subjectId(subject.getId())
                        .lessonId(lesson.getId())
                        .tenantId(tenantId)
                        .build()).toList();

        return subjectLessonLinkRepository.saveAll(subjectLessonLinks);
    }
}
