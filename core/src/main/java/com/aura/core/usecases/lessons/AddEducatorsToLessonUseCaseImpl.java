package com.aura.core.usecases.lessons;

import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.links.EducatorLessonLink;
import com.aura.core.repositories.EducatorLessonLinkRepository;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.repositories.LessonRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;


import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class AddEducatorsToLessonUseCaseImpl implements AddEducatorsToLessonUseCase {
    private final EducatorLessonLinkRepository educatorLessonLinkRepository;
    private final LessonRepository lessonRepository;
    private final EducatorRepository educatorRepository;

    @Override
    public List<EducatorLessonLink> addEducatorsToLessonUseCase(String lessonId, List<String> educatorIds, String tenantId) {
        if (educatorIds.isEmpty()) return emptyList();
        Lesson lesson = ofNullable(lessonRepository.findById(UUID.fromString(lessonId)))
                .orElseThrow(() -> new RuntimeException("no lesson id found"));
        List<Educator> educators = educatorRepository.findByIds(educatorIds.stream().map(UUID::fromString).toList());
        List<EducatorLessonLink> educatorLessonLinks = educators.stream()
                .map(educator -> EducatorLessonLink
                        .builder()
                        .educator(educator)
                        .lesson(lesson)
                        .build()).toList();

        return educatorLessonLinkRepository.saveAll(educatorLessonLinks);
    }
}
