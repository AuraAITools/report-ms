package com.reportai.www.reportapi.services.levels;

import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.exceptions.lib.ResourceAlreadyExistsException;
import com.reportai.www.reportapi.repositories.LevelRepository;
import com.reportai.www.reportapi.services.common.BaseServiceTemplate;
import com.reportai.www.reportapi.services.institutions.InstitutionsService;
import com.reportai.www.reportapi.services.subjects.SubjectsService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class LevelsService implements BaseServiceTemplate<Level, UUID> {
    private final LevelRepository levelRepository;
    private final InstitutionsService institutionsService;
    private final SubjectsService subjectsService;

    @Autowired
    public LevelsService(LevelRepository levelRepository, InstitutionsService institutionsService, SubjectsService subjectsService) {
        this.levelRepository = levelRepository;
        this.institutionsService = institutionsService;
        this.subjectsService = subjectsService;
    }

    @Override
    public JpaRepository<Level, UUID> getRepository() {
        return levelRepository;
    }

    @Transactional
    public Level createLevelForInstitution(@NonNull UUID institutionId, @NonNull Level newLevel, @NonNull List<UUID> subjectIds) {
        Institution institution = institutionsService.findById(institutionId);
        Optional<Level> existingLevel = levelRepository.findByNameAndTenantId(newLevel.getName(), institution.getId().toString());
        if (existingLevel.isPresent()) {
            throw new ResourceAlreadyExistsException(String.format("Level with name %s already exists", newLevel.getName()));
        }
        // attach existing subjects to level
        List<Subject> subjects = subjectsService.findByIds(subjectIds);
        newLevel.setInstitution(institution);
        newLevel.getSubjects().addAll(subjects);
        return levelRepository.save(newLevel);
    }

    public List<Level> getAllLevelsForInstitution(@NonNull UUID id) {
        Institution institution = institutionsService.findById(id);
        return institution.getLevels();
    }

    @Transactional
    public Subject addSubject(@NonNull UUID levelId, @NonNull UUID subjectId) {
        Level level = findById(levelId);
        Subject subject = subjectsService.findById(subjectId);
        // TODO: refactor
        level.getSubjects().add(subject);
        levelRepository.save(level);
        return subject;
    }
}
