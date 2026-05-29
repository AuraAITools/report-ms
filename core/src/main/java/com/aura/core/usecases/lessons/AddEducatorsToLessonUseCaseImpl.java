package com.aura.core.usecases.lessons;

import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.links.EducatorLessonLink;
import com.aura.core.exceptions.TechnicalException;
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
    public List<Educator> addEducatorsToLessonUseCase(String lessonId, List<String> educatorIds, String tenantId) {
        if (educatorIds.isEmpty()) return emptyList();
        Lesson lesson = ofNullable(lessonRepository.findById(UUID.fromString(lessonId)))
                .orElseThrow(() -> new TechnicalException("no lesson found with id: " + lessonId));
        List<Educator> educators = educatorRepository.findAllByIds(educatorIds.stream().map(UUID::fromString).toList());
        if (educators.size() != educatorIds.size()) {
            throw new TechnicalException("Some educators could not be found");
        }
        List<EducatorLessonLink> educatorLessonLinks = educators.stream()
                .map(educator -> EducatorLessonLink
                        .builder()
                        .educatorId(educator.getId())
                        .lessonId(lesson.getId())
                        .tenantId(tenantId)
                        .build()).toList();

        educatorLessonLinkRepository.saveAll(educatorLessonLinks);
        return educators;
    }
}
