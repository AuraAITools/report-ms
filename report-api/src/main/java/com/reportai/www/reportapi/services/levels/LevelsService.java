package com.reportai.www.reportapi.services.levels;

import com.reportai.www.reportapi.api.v1.levels.requests.UpdateLevelRequestDTO;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.School.SchoolCategory;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.attachments.SubjectLevelAttachment;
import com.reportai.www.reportapi.entities.helpers.Attachment;
import com.reportai.www.reportapi.exceptions.lib.ResourceAlreadyExistsException;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.LevelRepository;
import com.reportai.www.reportapi.repositories.SubjectLevelAttachmentRepository;
import com.reportai.www.reportapi.services.common.ISimpleRead;
import com.reportai.www.reportapi.services.institutions.InstitutionsService;
import com.reportai.www.reportapi.services.subjects.SubjectsService;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class LevelsService implements ISimpleRead<Level> {
    private final LevelRepository levelRepository;
    private final InstitutionsService institutionsService;
    private final SubjectsService subjectsService;
    private final ModelMapper modelMapper;
    private final SubjectLevelAttachmentRepository subjectLevelAttachmentRepository;

    public enum SchoolLevel {
        // Primary School
        PRIMARY_1("Primary 1", SchoolCategory.PRIMARY),
        PRIMARY_2("Primary 2", SchoolCategory.PRIMARY),
        PRIMARY_3("Primary 3", SchoolCategory.PRIMARY),
        PRIMARY_4("Primary 4", SchoolCategory.PRIMARY),
        PRIMARY_5("Primary 5", SchoolCategory.PRIMARY),
        PRIMARY_6("Primary 6", SchoolCategory.PRIMARY),

        // Secondary School
        SECONDARY_1("Secondary 1", SchoolCategory.SECONDARY),
        SECONDARY_2("Secondary 2", SchoolCategory.SECONDARY),
        SECONDARY_3("Secondary 3 ", SchoolCategory.SECONDARY),
        SECONDARY_4("Secondary 4 ", SchoolCategory.SECONDARY),
        SECONDARY_5("Secondary 5 ", SchoolCategory.SECONDARY),

        // Junior College
        JC_1("Junior College 1", SchoolCategory.JUNIOR_COLLEGE),
        JC_2("Junior College 2", SchoolCategory.JUNIOR_COLLEGE),
        JC_3("Junior College 3", SchoolCategory.JUNIOR_COLLEGE),

        // Polytechnic
        POLY_YEAR_1("Polytechnic Year 1", SchoolCategory.POLYTECHNIC),
        POLY_YEAR_2("Polytechnic Year 2 ", SchoolCategory.POLYTECHNIC),
        POLY_YEAR_3("Polytechnic Year 3", SchoolCategory.POLYTECHNIC),

        // ITE
        NITEC_YEAR_1("Nitec Year 1 ", SchoolCategory.TECHNICAL),
        NITEC_YEAR_2("Nitec Year 2", SchoolCategory.TECHNICAL),
        HIGHER_NITEC_YEAR_1("Higher Nitec Year 1", SchoolCategory.TECHNICAL),
        HIGHER_NITEC_YEAR_2("Higher Nitec Year 2", SchoolCategory.TECHNICAL),

        // University
        UNI_YEAR_1("University Year 1", SchoolCategory.UNIVERSITY),
        UNI_YEAR_2("University Year 2", SchoolCategory.UNIVERSITY),
        UNI_YEAR_3("University Year 3 ", SchoolCategory.UNIVERSITY),
        UNI_YEAR_4("University Year 4", SchoolCategory.UNIVERSITY),
        HONOURS_YEAR("Honours Year", SchoolCategory.UNIVERSITY),
        MASTERS("Masters", SchoolCategory.UNIVERSITY),
        PHD("Doctor of Philosophy", SchoolCategory.UNIVERSITY),

        // International School (examples from various systems)
        KINDERGARTEN_1("Kindergarten 1", SchoolCategory.INTERNATIONAL),
        KINDERGARTEN_2("Kindergarten 2", SchoolCategory.INTERNATIONAL),
        GRADE_1("Grade 1", SchoolCategory.INTERNATIONAL),
        GRADE_2("Grade 2", SchoolCategory.INTERNATIONAL),
        GRADE_3("Grade 3", SchoolCategory.INTERNATIONAL),
        GRADE_4("Grade 4", SchoolCategory.INTERNATIONAL),
        GRADE_5("Grade 5", SchoolCategory.INTERNATIONAL),
        GRADE_6("Grade 6", SchoolCategory.INTERNATIONAL),
        GRADE_7("Grade 7", SchoolCategory.INTERNATIONAL),
        GRADE_8("Grade 8", SchoolCategory.INTERNATIONAL),
        GRADE_9("Grade 9", SchoolCategory.INTERNATIONAL),
        GRADE_10("Grade 10", SchoolCategory.INTERNATIONAL),
        GRADE_11("Grade 11", SchoolCategory.INTERNATIONAL),
        GRADE_12("Grade 12", SchoolCategory.INTERNATIONAL);

        private final String name;
        private final SchoolCategory category;

        SchoolLevel(String name, SchoolCategory category) {
            this.name = name;
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public SchoolCategory getCategory() {
            return category;
        }

        /**
         * Get all levels for a specific school category.
         */
        public static List<SchoolLevel> getLevelsByCategory(SchoolCategory category) {
            return Arrays.stream(values())
                    .filter(level -> level.category == category)
                    .collect(Collectors.toList());
        }
    }

    public static final List<SchoolLevel> DEFAULT_SCHOOL_LEVELS = Arrays.asList(SchoolLevel.values());

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
    }

    /**
     * Creates default levels for a given institution.
     *
     * @param institutionId
     * @return
     */
    @Transactional
    public List<Level> createDefaultLevels(UUID institutionId) {
        Objects.requireNonNull(institutionId, "Institution ID cannot be null");
        List<Level> levels = DEFAULT_SCHOOL_LEVELS.stream()
                .map(defaultLevel -> {
                    Level level = new Level();
                    level.setName(defaultLevel.getName());
                    level.setCategory(defaultLevel.getCategory());
                    level.setTenantId(institutionId.toString());
                    return level;
                })
                .collect(Collectors.toList());
        return levelRepository.saveAllAndFlush(levels);
    }


    @Override
    public JpaRepository<Level, UUID> getRepository() {
        return levelRepository;
    }

    @Transactional
    public Level createLevelForInstitution(@NonNull Level newLevel, @NonNull List<UUID> subjectIds) {
        Institution institution = institutionsService.getInstitutionFromContext();
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

    @Transactional
    public Subject addSubject(@NonNull UUID levelId, @NonNull UUID subjectId) {
        Level level = findById(levelId);
        Subject subject = subjectsService.findById(subjectId);
        SubjectLevelAttachment subjectLevelAttachment = Attachment.createAndSync(subject, level, new SubjectLevelAttachment());
        subjectLevelAttachmentRepository.save(subjectLevelAttachment);
        return subject;
    }

    @Transactional
    public Level findByName(String name) {
        return levelRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException(String.format("level: name %s not found", name)));
    }

    @Transactional
    public List<Level> findByNameIn(List<String> names) {
        return levelRepository.findByNameIn(names);
    }
}
