package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.links.LevelLessonLink;
import com.aura.core.repositories.LevelLessonLinkRepository;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.repositories.LessonRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;


import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class AddLevelsToLessonUseCaseImpl implements AddLevelsToLessonUseCase {
    private final LevelLessonLinkRepository levelLessonLinkRepository;
    private final LessonRepository lessonRepository;
    private final LevelRepository levelRepository;

    @Override
    public List<LevelLessonLink> addLevelsToLesson(String lessonId, List<String> levelIds, String tenantId) {
        if (levelIds.isEmpty()) return emptyList();
        Lesson lesson = ofNullable(lessonRepository.findById(UUID.fromString(lessonId)))
                .orElseThrow(() -> new RuntimeException("no lesson id found"));
        List<Level> levels = levelRepository.findAllByIds(levelIds.stream().map(UUID::fromString).toList());
        List<LevelLessonLink> levelLessonLinks = levels.stream()
                .map(level -> LevelLessonLink
                        .builder()
                        .levelId(level.getId())
                        .lessonId(lesson.getId())
                        .tenantId(tenantId)
                        .build()).toList();

        return levelLessonLinkRepository.saveAll(levelLessonLinks);
    }
}
