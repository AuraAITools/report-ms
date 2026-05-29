package com.aura.core.repositories;

import com.aura.core.entities.links.LessonPlanMaterialLink;
import java.util.List;

public interface LessonPlanMaterialLinkRepository {
    List<LessonPlanMaterialLink> saveAll(List<LessonPlanMaterialLink> links);
}
