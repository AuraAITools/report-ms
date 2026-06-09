package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.delete_impact.DeleteImpact;
import com.aura.core.entities.levels.Level;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.usecases.levels.CreateLevelUseCase;
import com.aura.core.usecases.levels.DeleteLevelUseCase;
import com.aura.core.usecases.levels.GetLevelDeleteImpactUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LevelService {

    private final CreateLevelUseCase createLevelUseCase;
    private final DeleteLevelUseCase deleteLevelUseCase;
    private final GetLevelDeleteImpactUseCase getLevelDeleteImpactUseCase;
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

    public DeleteImpact getDeleteImpact(String id) {
        return getLevelDeleteImpactUseCase.getDeleteImpact(id);
    }

    @Transactional
    public void deleteById(String id) {
        courseRepository.nullOutLevelForCourses(id);
        levelRepository.nullOutLevelOnStudentsByLevelId(id);
        levelRepository.deleteAllLevelLessonLinksByLevelId(id);
        levelRepository.deleteAllSubjectLevelLinksByLevelId(id);
        levelRepository.deleteAllLevelEducatorLinksByLevelId(id);
        deleteLevelUseCase.deleteById(id);
        relationshipService.deleteAllRelationshipsForResource("level", id);
        relationshipService.deleteAllRelationshipsForResource("level_user_group", id);
    }
}
