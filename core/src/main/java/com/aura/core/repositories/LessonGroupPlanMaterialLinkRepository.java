package com.aura.core.repositories;

import com.aura.core.entities.links.LessonGroupPlanMaterialLink;
import java.util.List;

public interface LessonGroupPlanMaterialLinkRepository {
    List<LessonGroupPlanMaterialLink> saveAll(List<LessonGroupPlanMaterialLink> links);
}
