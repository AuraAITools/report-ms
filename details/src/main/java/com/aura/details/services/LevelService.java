package com.aura.details.services;

import com.aura.core.entities.delete_impact.DeleteImpact;
import com.aura.core.entities.levels.Level;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.usecases.levels.CreateLevelUseCase;
import com.aura.core.usecases.levels.DeleteLevelUseCase;
import com.aura.core.usecases.levels.GetLevelDeleteImpactUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LevelService {

    private final CreateLevelUseCase createLevelUseCase;
    private final DeleteLevelUseCase deleteLevelUseCase;
    private final GetLevelDeleteImpactUseCase getLevelDeleteImpactUseCase;
    private final LevelRepository levelRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public Level createLevelInInstitution(Level level) {
        return createLevelUseCase.createLevel(level);
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
    }
}
