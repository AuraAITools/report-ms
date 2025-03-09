package com.reportai.www.reportapi.services.levels;

import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.exceptions.http.HttpInstitutionNotFoundException;
import com.reportai.www.reportapi.exceptions.lib.ResourceAlreadyExistsException;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.LevelRepository;
import com.reportai.www.reportapi.repositories.SubjectRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LevelsService {
    private final LevelRepository levelRepository;
    private final InstitutionRepository institutionRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public LevelsService(LevelRepository levelRepository, InstitutionRepository institutionRepository, SubjectRepository subjectRepository) {
        this.levelRepository = levelRepository;
        this.institutionRepository = institutionRepository;
        this.subjectRepository = subjectRepository;
    }

    public Level findById(UUID id) {
        return levelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("level not found"));
    }

    public List<Level> findByIds(List<UUID> ids) {
        return levelRepository.findAllById(ids);
    }

    @Transactional
    public Level createLevelForInstitution(UUID institutionId, Level newLevel, List<UUID> subjectIds) {
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(HttpInstitutionNotFoundException::new);
        Optional<Level> existingLevel = levelRepository.findByNameAndTenantId(newLevel.getName(), institution.getId().toString());
        if (existingLevel.isPresent()) {
            throw new ResourceAlreadyExistsException(String.format("Level with name %s already exists", newLevel.getName()));
        }
        // attach existing subjects to level
        List<Subject> subjects = subjectRepository.findAllById(subjectIds);
        newLevel.setInstitution(institution);
        newLevel.getSubjects().addAll(subjects);
        return levelRepository.save(newLevel);
    }

    public List<Level> getAllLevelsForInstitution(UUID id) {
        Institution institution = institutionRepository.findById(id).orElseThrow(HttpInstitutionNotFoundException::new);
        return institution.getLevels();
    }

    @Transactional
    public Subject attachSubjectForLevel(UUID levelId, UUID subjectId) {
        Level level = findById(levelId);
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new ResourceNotFoundException("subject not found"));
        level.getSubjects().add(subject);
        levelRepository.save(level);
        return subject;
    }
}
