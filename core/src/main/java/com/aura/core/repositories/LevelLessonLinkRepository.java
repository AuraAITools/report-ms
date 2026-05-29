package com.aura.core.repositories;

import com.aura.core.entities.links.LevelLessonLink;
import java.util.List;

public interface LevelLessonLinkRepository {
    LevelLessonLink save(LevelLessonLink levelLessonLink);

    List<LevelLessonLink> saveAll(List<LevelLessonLink> levelLessonLinks);
}
