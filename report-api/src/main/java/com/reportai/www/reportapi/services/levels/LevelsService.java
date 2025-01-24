package com.reportai.www.reportapi.services.levels;

import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.exceptions.http.HttpInstitutionNotFoundException;
import com.reportai.www.reportapi.exceptions.lib.ResourceAlreadyExistsException;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.LevelsRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class LevelsService {
    private final LevelsRepository levelsRepository;
    private final InstitutionRepository institutionRepository;

    public LevelsService(LevelsRepository levelsRepository, InstitutionRepository institutionRepository) {
        this.levelsRepository = levelsRepository;
        this.institutionRepository = institutionRepository;
    }

    @Transactional
    public Level createLevelForInstitution(UUID id, Level newLevel) {
        Institution institution = institutionRepository.findById(id).orElseThrow(HttpInstitutionNotFoundException::new);
        Optional<Level> existingLevel = levelsRepository.findByName(newLevel.getName());
        if (existingLevel.isPresent()) {
            throw new ResourceAlreadyExistsException(String.format("Level with name %s already exists", newLevel.getName()));
        }
        newLevel.setInstitution(institution);
        return levelsRepository.save(newLevel);
    }

    public List<Level> getAllLevelsForInstitution(UUID id) {
        Institution institution = institutionRepository.findById(id).orElseThrow(HttpInstitutionNotFoundException::new);
        return institution.getLevels();
    }
}
