package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.levels.Level;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.usecases.levels.CreateLevelUseCase;
import com.aura.core.usecases.levels.DeleteLevelUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LevelService {

    private final CreateLevelUseCase createLevelUseCase;
    private final DeleteLevelUseCase deleteLevelUseCase;
    private final LevelRepository levelRepository;
    private final CourseRepository courseRepository;
    private final RelationshipAuthorizationService relationshipService;

    @Transactional
    public Level createLevelInInstitution(Level level) {
        Level created = createLevelUseCase.createLevel(level);
        String levelId = created.getId();
        String institutionId = created.getTenantId();

        relationshipService.writeRelationships(List.of(
                new RelationshipAuthorizationService.Relationship(
                        "level", levelId, "user_group", "level_user_group", levelId),
                new RelationshipAuthorizationService.Relationship(
                        "level_user_group", levelId, "parent", "system_user_group", "aura"),
                new RelationshipAuthorizationService.Relationship(
                        "level", levelId, "parent", "institution", institutionId)));
        return created;
    }

    @Transactional
    public void deleteById(String id) {
        courseRepository.nullOutLevelForCourses(id);
        levelRepository.deleteAllLevelLessonLinksByLevelId(id);
        deleteLevelUseCase.deleteById(id);
        relationshipService.deleteAllRelationshipsForResource("level", id);
        relationshipService.deleteAllRelationshipsForResource("level_user_group", id);
    }
}
