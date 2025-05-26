package com.reportai.www.reportapi.services.subjects;

import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.School;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.attachments.SubjectLevelAttachment;
import com.reportai.www.reportapi.entities.helpers.Attachment;
import com.reportai.www.reportapi.repositories.SubjectLevelAttachmentRepository;
import com.reportai.www.reportapi.repositories.SubjectRepository;
import com.reportai.www.reportapi.services.common.ISimpleCreate;
import com.reportai.www.reportapi.services.common.ISimpleRead;
import com.reportai.www.reportapi.services.levels.LevelsService;
import com.reportai.www.reportapi.services.levels.LevelsService.SchoolLevel;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class SubjectsService implements ISimpleRead<Subject>, ISimpleCreate<Subject> {
    public enum SUBJECTS {
        // Primary School Subjects
        ENGLISH("English Language", SchoolLevel.PRIMARY_1, SchoolLevel.PRIMARY_2, SchoolLevel.PRIMARY_3, SchoolLevel.PRIMARY_4, SchoolLevel.PRIMARY_5, SchoolLevel.PRIMARY_6, SchoolLevel.SECONDARY_1, SchoolLevel.SECONDARY_2, SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),
        MATHEMATICS("Mathematics", SchoolLevel.PRIMARY_1, SchoolLevel.PRIMARY_2, SchoolLevel.PRIMARY_3, SchoolLevel.PRIMARY_4, SchoolLevel.PRIMARY_5, SchoolLevel.PRIMARY_6, SchoolLevel.SECONDARY_1, SchoolLevel.SECONDARY_2, SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),
        SCIENCE("Science", SchoolLevel.PRIMARY_3, SchoolLevel.PRIMARY_4, SchoolLevel.PRIMARY_5, SchoolLevel.PRIMARY_6, SchoolLevel.SECONDARY_1, SchoolLevel.SECONDARY_2),
        MOTHER_TONGUE("Mother Tongue", SchoolLevel.PRIMARY_1, SchoolLevel.PRIMARY_2, SchoolLevel.PRIMARY_3, SchoolLevel.PRIMARY_4, SchoolLevel.PRIMARY_5, SchoolLevel.PRIMARY_6, SchoolLevel.SECONDARY_1, SchoolLevel.SECONDARY_2, SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5, SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        CHINESE("Chinese", SchoolLevel.PRIMARY_1, SchoolLevel.PRIMARY_2, SchoolLevel.PRIMARY_3, SchoolLevel.PRIMARY_4, SchoolLevel.PRIMARY_5, SchoolLevel.PRIMARY_6, SchoolLevel.SECONDARY_1, SchoolLevel.SECONDARY_2, SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),
        MALAY("Malay", SchoolLevel.PRIMARY_1, SchoolLevel.PRIMARY_2, SchoolLevel.PRIMARY_3, SchoolLevel.PRIMARY_4, SchoolLevel.PRIMARY_5, SchoolLevel.PRIMARY_6, SchoolLevel.SECONDARY_1, SchoolLevel.SECONDARY_2, SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),
        TAMIL("Tamil", SchoolLevel.PRIMARY_1, SchoolLevel.PRIMARY_2, SchoolLevel.PRIMARY_3, SchoolLevel.PRIMARY_4, SchoolLevel.PRIMARY_5, SchoolLevel.PRIMARY_6, SchoolLevel.SECONDARY_1, SchoolLevel.SECONDARY_2, SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),
        SOCIAL_STUDIES("Social Studies", SchoolLevel.PRIMARY_1, SchoolLevel.PRIMARY_2, SchoolLevel.PRIMARY_3, SchoolLevel.PRIMARY_4, SchoolLevel.PRIMARY_5, SchoolLevel.PRIMARY_6, SchoolLevel.SECONDARY_1, SchoolLevel.SECONDARY_2, SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),
        ART("Art", SchoolLevel.PRIMARY_1, SchoolLevel.PRIMARY_2, SchoolLevel.PRIMARY_3, SchoolLevel.PRIMARY_4, SchoolLevel.PRIMARY_5, SchoolLevel.PRIMARY_6, SchoolLevel.SECONDARY_1, SchoolLevel.SECONDARY_2, SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),
        MUSIC("Music", SchoolLevel.PRIMARY_1, SchoolLevel.PRIMARY_2, SchoolLevel.PRIMARY_3, SchoolLevel.PRIMARY_4, SchoolLevel.PRIMARY_5, SchoolLevel.PRIMARY_6, SchoolLevel.SECONDARY_1, SchoolLevel.SECONDARY_2, SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),
        PHYSICAL_EDUCATION("Physical Education", SchoolLevel.PRIMARY_1, SchoolLevel.PRIMARY_2, SchoolLevel.PRIMARY_3, SchoolLevel.PRIMARY_4, SchoolLevel.PRIMARY_5, SchoolLevel.PRIMARY_6, SchoolLevel.SECONDARY_1, SchoolLevel.SECONDARY_2, SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),

        // Secondary School Core Subjects
        ADDITIONAL_MATHEMATICS("Additional Mathematics", SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),
        ELEMENTARY_MATHEMATICS("Elementary Mathematics", SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),
        PHYSICS("Physics", SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5, SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        CHEMISTRY("Chemistry", SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5, SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        BIOLOGY("Biology", SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5, SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        COMBINED_SCIENCE("Combined Science", SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),
        HIGHER_CHINESE("Higher Chinese", SchoolLevel.SECONDARY_1, SchoolLevel.SECONDARY_2, SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4),
        HIGHER_MALAY("Higher Malay", SchoolLevel.SECONDARY_1, SchoolLevel.SECONDARY_2, SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4),
        HIGHER_TAMIL("Higher Tamil", SchoolLevel.SECONDARY_1, SchoolLevel.SECONDARY_2, SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4),

        // Secondary School Humanities
        HISTORY("History", SchoolLevel.SECONDARY_1, SchoolLevel.SECONDARY_2, SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5, SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        GEOGRAPHY("Geography", SchoolLevel.SECONDARY_1, SchoolLevel.SECONDARY_2, SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5, SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        LITERATURE_ENGLISH("Literature in English", SchoolLevel.SECONDARY_1, SchoolLevel.SECONDARY_2, SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5, SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        LITERATURE_CHINESE("Literature in Chinese", SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),
        LITERATURE_MALAY("Literature in Malay", SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),
        LITERATURE_TAMIL("Literature in Tamil", SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),

        // Secondary School Arts and Technology
        DESIGN_AND_TECHNOLOGY("Design and Technology", SchoolLevel.SECONDARY_1, SchoolLevel.SECONDARY_2, SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),
        FOOD_AND_NUTRITION("Food and Nutrition", SchoolLevel.SECONDARY_1, SchoolLevel.SECONDARY_2, SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),
        COMPUTER_APPLICATIONS("Computer Applications", SchoolLevel.SECONDARY_1, SchoolLevel.SECONDARY_2, SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),
        COMPUTING("Computing", SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),

        // Business and Commerce
        PRINCIPLES_OF_ACCOUNTS("Principles of Accounts", SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),
        BUSINESS_STUDIES("Business Studies", SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5),
        ECONOMICS("Economics", SchoolLevel.SECONDARY_3, SchoolLevel.SECONDARY_4, SchoolLevel.SECONDARY_5, SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),

        // Junior College H1 Subjects
        GENERAL_PAPER("General Paper", SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        PROJECT_WORK("Project Work", SchoolLevel.JC_1),

        // Junior College H2 Subjects
        MATHEMATICS_H2("H2 Mathematics", SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        PHYSICS_H2("H2 Physics", SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        CHEMISTRY_H2("H2 Chemistry", SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        BIOLOGY_H2("H2 Biology", SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        ECONOMICS_H2("H2 Economics", SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        HISTORY_H2("H2 History", SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        GEOGRAPHY_H2("H2 Geography", SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        LITERATURE_H2("H2 Literature in English", SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        ART_H2("H2 Art", SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        MUSIC_H2("H2 Music", SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        COMPUTING_H2("H2 Computing", SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),

        // Junior College H1 Subjects
        MATHEMATICS_H1("H1 Mathematics", SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        PHYSICS_H1("H1 Physics", SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        CHEMISTRY_H1("H1 Chemistry", SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        BIOLOGY_H1("H1 Biology", SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        ECONOMICS_H1("H1 Economics", SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        HISTORY_H1("H1 History", SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        GEOGRAPHY_H1("H1 Geography", SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),
        LITERATURE_H1("H1 Literature in English", SchoolLevel.JC_1, SchoolLevel.JC_2, SchoolLevel.JC_3),

        // Junior College H3 Subjects
        MATHEMATICS_H3("H3 Mathematics", SchoolLevel.JC_2, SchoolLevel.JC_3),
        PHYSICS_H3("H3 Physics", SchoolLevel.JC_2, SchoolLevel.JC_3),
        CHEMISTRY_H3("H3 Chemistry", SchoolLevel.JC_2, SchoolLevel.JC_3),
        BIOLOGY_H3("H3 Biology", SchoolLevel.JC_2, SchoolLevel.JC_3),
        ECONOMICS_H3("H3 Economics", SchoolLevel.JC_2, SchoolLevel.JC_3),

        // International School Subjects (K1-Grade 12)
        MATHEMATICS_INTL("Mathematics Intl", SchoolLevel.KINDERGARTEN_1, SchoolLevel.KINDERGARTEN_2, SchoolLevel.GRADE_1, SchoolLevel.GRADE_2, SchoolLevel.GRADE_3, SchoolLevel.GRADE_4, SchoolLevel.GRADE_5, SchoolLevel.GRADE_6, SchoolLevel.GRADE_7, SchoolLevel.GRADE_8, SchoolLevel.GRADE_9, SchoolLevel.GRADE_10, SchoolLevel.GRADE_11, SchoolLevel.GRADE_12),
        SCIENCE_INTL("Science Intl", SchoolLevel.GRADE_1, SchoolLevel.GRADE_2, SchoolLevel.GRADE_3, SchoolLevel.GRADE_4, SchoolLevel.GRADE_5, SchoolLevel.GRADE_6, SchoolLevel.GRADE_7, SchoolLevel.GRADE_8),
        PHYSICS_INTL("Physics Intl", SchoolLevel.GRADE_9, SchoolLevel.GRADE_10, SchoolLevel.GRADE_11, SchoolLevel.GRADE_12),
        CHEMISTRY_INTL("Chemistry Intl", SchoolLevel.GRADE_9, SchoolLevel.GRADE_10, SchoolLevel.GRADE_11, SchoolLevel.GRADE_12),
        BIOLOGY_INTL("Biology Intl", SchoolLevel.GRADE_9, SchoolLevel.GRADE_10, SchoolLevel.GRADE_11, SchoolLevel.GRADE_12),
        SOCIAL_STUDIES_INTL("Social Studies Intl", SchoolLevel.GRADE_1, SchoolLevel.GRADE_2, SchoolLevel.GRADE_3, SchoolLevel.GRADE_4, SchoolLevel.GRADE_5, SchoolLevel.GRADE_6, SchoolLevel.GRADE_7, SchoolLevel.GRADE_8),
        WORLD_HISTORY("World History Intl", SchoolLevel.GRADE_9, SchoolLevel.GRADE_10, SchoolLevel.GRADE_11, SchoolLevel.GRADE_12),
        WORLD_GEOGRAPHY("World Geography Intl", SchoolLevel.GRADE_9, SchoolLevel.GRADE_10, SchoolLevel.GRADE_11, SchoolLevel.GRADE_12);

        private final String name;
        private final List<SchoolLevel> applicableLevels;

        SUBJECTS(String name, SchoolLevel... levels) {
            this.name = name;
            this.applicableLevels = Arrays.asList(levels);
        }

        public String getName() {
            return name;
        }

        public List<SchoolLevel> getApplicableLevels() {
            return applicableLevels;
        }

        public boolean isApplicableToLevel(SchoolLevel level) {
            return applicableLevels.contains(level);
        }

        /**
         * Get all subjects applicable to a specific school level.
         */
        public static List<SUBJECTS> getSubjectsByLevel(SchoolLevel level) {
            return Arrays.stream(values())
                    .filter(subject -> subject.isApplicableToLevel(level))
                    .toList();
        }

        /**
         * Get all subjects for a specific school category.
         */
        public static List<SUBJECTS> getSubjectsByCategory(School.SchoolCategory category) {
            return Arrays.stream(values())
                    .filter(subject -> subject.getApplicableLevels().stream()
                            .anyMatch(level -> level.getCategory() == category))
                    .collect(Collectors.toList());
        }
    }

    private final SubjectRepository subjectRepository;

    private final LevelsService levelsService;
    private final SubjectLevelAttachmentRepository subjectLevelAttachmentRepository;

    @Autowired
    public SubjectsService(SubjectRepository subjectRepository, @Lazy LevelsService levelsService,
                           SubjectLevelAttachmentRepository subjectLevelAttachmentRepository) {
        this.subjectRepository = subjectRepository;
        this.levelsService = levelsService;
        this.subjectLevelAttachmentRepository = subjectLevelAttachmentRepository;
    }

    @Override
    public JpaRepository<Subject, UUID> getRepository() {
        return subjectRepository;
    }

    @Transactional
    public List<Subject> createDefaultSubjects(UUID institutionId) {
        Objects.requireNonNull(institutionId);

        return Arrays.stream(SUBJECTS.values())
                .map(subjectEnum -> {
                    List<String> levelNames = subjectEnum.getApplicableLevels().stream().map(SchoolLevel::getName).toList();
                    List<Level> levels = levelsService.findByNameIn(levelNames);
                    Subject subject = Subject
                            .builder()
                            .tenantId(institutionId.toString())
                            .name(subjectEnum.getName())
                            .build();
                    create(subject);

                    // create subject level attachments
                    List<SubjectLevelAttachment> subjectLevelAttachments = levels
                            .stream()
                            .map(level -> Attachment.createAndSync(subject, level, new SubjectLevelAttachment()))
                            .toList();

                    subjectLevelAttachmentRepository.saveAll(subjectLevelAttachments);
                    return subject;
                })
                .toList();
    }

    @Transactional
    public Subject updateSubjectForInstitution(@NonNull UUID subjectId, @NonNull Subject updatedSubject) {
        Subject subject = findById(subjectId);
        subject.setName(updatedSubject.getName());
        return subjectRepository.save(subject);
    }

    @Transactional
    public Collection<Subject> getAllSubjectsForInstitution(@NonNull UUID id) {
        return subjectRepository.findAll();
    }
}
