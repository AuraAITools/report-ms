package com.aura.core.repositories;

import com.aura.core.entities.links.EducatorLessonLink;
import java.util.List;

public interface EducatorLessonLinkRepository {
    EducatorLessonLink save(EducatorLessonLink educatorLessonLink);

    List<EducatorLessonLink> saveAll(List<EducatorLessonLink> educatorLessonLinks);
}
