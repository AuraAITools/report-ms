package com.reportai.www.reportapi.services.levels;

import com.reportai.www.reportapi.api.v1.levels.requests.UpdateLevelRequestDTO;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.attachments.SubjectLevelAttachment;
import com.reportai.www.reportapi.entities.helpers.Attachment;
import com.reportai.www.reportapi.exceptions.lib.ResourceAlreadyExistsException;
import com.reportai.www.reportapi.repositories.LevelRepository;
import com.reportai.www.reportapi.repositories.SubjectLevelAttachmentRepository;
import com.reportai.www.reportapi.services.common.BaseServiceTemplate;
import com.reportai.www.reportapi.services.institutions.InstitutionsService;
import com.reportai.www.reportapi.services.subjects.SubjectsService;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class LevelsService implements BaseServiceTemplate<Level, UUID> {
    private final LevelRepository levelRepository;
    private final InstitutionsService institutionsService;
    private final SubjectsService subjectsService;
    private final ModelMapper modelMapper;
    private final SubjectLevelAttachmentRepository subjectLevelAttachmentRepository;

    @Autowired
    public LevelsService(LevelRepository levelRepository, InstitutionsService institutionsService, SubjectsService subjectsService,
                         SubjectLevelAttachmentRepository subjectLevelAttachmentRepository) {
        this.levelRepository = levelRepository;
        this.institutionsService = institutionsService;
        this.subjectsService = subjectsService;
        this.modelMapper = new ModelMapper();
        this.modelMapper
                .getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);// skip null values
        this.subjectLevelAttachmentRepository = subjectLevelAttachmentRepository;
        // TODO: refactor Converters and Configurers to be in separate files instead of in the constructor
        // Educators converter with null check
//        Converter<List<UUID>, List<Subject>> subjectIdsToSubjectsConverter = context -> {
//            if (context.getSource() == null) {
//                this.subjectLevelAttachmentRepository = subjectLevelAttachmentRepository;
//                return null;
//            }
//            List<UUID> subjectIds = context.getSource();
//            return subjectIds.stream()
//                    .map(subjectsService::findById)
//                    .collect(Collectors.toList());
//        };


//        this.modelMapper
//                .typeMap(UpdateLevelRequestDTO.class, Level.class)
//                .addMappings(mapper -> {
//                    mapper.using(subjectIdsToSubjectsConverter)
//                            .map(UpdateLevelRequestDTO::getSubjectIds, Level::setSubjects);
//                });
    }


    @Override
    public JpaRepository<Level, UUID> getRepository() {
        return levelRepository;
    }

    @Transactional
    public Level createLevelForInstitution(@NonNull UUID institutionId, @NonNull Level newLevel, @NonNull List<UUID> subjectIds) {
        Institution institution = institutionsService.getCurrentInstitution();
        Optional<Level> existingLevel = levelRepository.findByNameAndTenantId(newLevel.getName(), institution.getId().toString());
        if (existingLevel.isPresent()) {
            throw new ResourceAlreadyExistsException(String.format("Level with name %s already exists", newLevel.getName()));
        }
        Level savedLevel = levelRepository.save(newLevel);
        // attach existing subjects to level
        List<Subject> subjects = subjectsService.findByIds(subjectIds);
        List<SubjectLevelAttachment> subjectLevelAttachments = subjects.stream().map(subject -> Attachment.createAndSync(subject, savedLevel, new SubjectLevelAttachment())).toList();
        subjectLevelAttachmentRepository.saveAll(subjectLevelAttachments);
        return savedLevel;
    }

    @Transactional
    public Level update(@NonNull UUID levelId, @NonNull UpdateLevelRequestDTO update) {
        Level level = findById(levelId);
        this.modelMapper.map(update, level);
        return level;
    }


    public Collection<Level> getAllLevelsForInstitution() {
        return levelRepository.findAll();
    }

    @Transactional
    public Subject addSubject(@NonNull UUID levelId, @NonNull UUID subjectId) {
        Level level = findById(levelId);
        Subject subject = subjectsService.findById(subjectId);
        SubjectLevelAttachment subjectLevelAttachment = Attachment.createAndSync(subject, level, new SubjectLevelAttachment());
        subjectLevelAttachmentRepository.save(subjectLevelAttachment);
        return subject;
    }
}
