package com.aura.core.repositories;

import com.aura.core.entities.links.SubjectLessonLink;
import java.util.List;

public interface SubjectLessonLinkRepository {
    SubjectLessonLink save(SubjectLessonLink subjectLessonLink);

    List<SubjectLessonLink> saveAll(List<SubjectLessonLink> subjectLessonLinks);
}
