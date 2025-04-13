package com.reportai.www.reportapi.services.educators;

import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.attachments.LevelEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.OutletEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.SubjectEducatorAttachment;
import com.reportai.www.reportapi.entities.educators.Educator;
import com.reportai.www.reportapi.entities.helpers.Attachment;
import com.reportai.www.reportapi.entities.personas.EducatorClientPersona;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.EducatorClientPersonaRepository;
import com.reportai.www.reportapi.repositories.EducatorLessonAttachmentRepository;
import com.reportai.www.reportapi.repositories.EducatorRepository;
import com.reportai.www.reportapi.repositories.LevelEducatorAttachmentRepository;
import com.reportai.www.reportapi.repositories.OutletEducatorAttachmentRepository;
import com.reportai.www.reportapi.repositories.SubjectEducatorAttachmentRepository;
import com.reportai.www.reportapi.services.common.BaseServiceTemplate;
import com.reportai.www.reportapi.services.institutions.InstitutionsService;
import com.reportai.www.reportapi.services.levels.LevelsService;
import com.reportai.www.reportapi.services.outlets.OutletsService;
import com.reportai.www.reportapi.services.subjects.SubjectsService;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class EducatorsService implements BaseServiceTemplate<Educator, UUID> {
    private final EducatorRepository educatorRepository;
    private final InstitutionsService institutionsService;
    private final OutletsService outletsService;

    private final EducatorClientPersonaRepository educatorClientPersonaRepository;
    private final OutletEducatorAttachmentRepository outletEducatorAttachmentRepository;
    private final LevelsService levelsService;
    private final SubjectsService subjectsService;
    private final EducatorLessonAttachmentRepository educatorLessonAttachmentRepository;
    private final LevelEducatorAttachmentRepository levelEducatorAttachmentRepository;
    private final SubjectEducatorAttachmentRepository subjectEducatorAttachmentRepository;

    @Autowired
    public EducatorsService(EducatorRepository educatorRepository, InstitutionsService institutionsService, OutletsService outletsService, EducatorClientPersonaRepository educatorClientPersonaRepository, OutletEducatorAttachmentRepository outletEducatorAttachmentRepository, LevelsService levelsService, SubjectsService subjectsService,
                            EducatorLessonAttachmentRepository educatorLessonAttachmentRepository,
                            LevelEducatorAttachmentRepository levelEducatorAttachmentRepository,
                            SubjectEducatorAttachmentRepository subjectEducatorAttachmentRepository) {
        this.educatorRepository = educatorRepository;
        this.institutionsService = institutionsService;
        this.outletsService = outletsService;
        this.educatorClientPersonaRepository = educatorClientPersonaRepository;
        this.outletEducatorAttachmentRepository = outletEducatorAttachmentRepository;
        this.levelsService = levelsService;
        this.subjectsService = subjectsService;
        this.educatorLessonAttachmentRepository = educatorLessonAttachmentRepository;
        this.levelEducatorAttachmentRepository = levelEducatorAttachmentRepository;
        this.subjectEducatorAttachmentRepository = subjectEducatorAttachmentRepository;
    }

    @Override
    public JpaRepository<Educator, UUID> getRepository() {
        return educatorRepository;
    }

    @Transactional
    public Educator create(@NonNull Educator educator, @Nullable List<UUID> levelIds, @Nullable List<UUID> subjectIds) {
        Educator createdEducator = educatorRepository.save(educator);
        if (levelIds != null && !levelIds.isEmpty()) {
            List<Level> levels = levelsService.findByIds(levelIds);

            List<LevelEducatorAttachment> levelEducatorAttachment = levels.stream().map(level -> Attachment.createAndSync(level, createdEducator, new LevelEducatorAttachment())).toList();
            levelEducatorAttachmentRepository.saveAll(levelEducatorAttachment);
        }

        if (subjectIds != null && !subjectIds.isEmpty()) {
            List<Subject> subjects = subjectsService.findByIds(subjectIds);
            List<SubjectEducatorAttachment> subjectEducatorAttachments = subjects.stream().map(subject -> Attachment.createAndSync(subject, createdEducator, new SubjectEducatorAttachment())).toList();
            subjectEducatorAttachmentRepository.saveAll(subjectEducatorAttachments);
        }
        return createdEducator;
    }

    public Educator addInstitution(@NonNull UUID educatorId, @NonNull UUID institutionId) {
        Educator educator = findById(educatorId);
        Institution institution = institutionsService.getCurrentInstitution();
        return educatorRepository.save(educator);
    }

    public Educator addEducatorClientPersona(@NonNull UUID educatorId, @NonNull UUID educatorClientPersonaId) {
        Educator educator = findById(educatorId);
        EducatorClientPersona educatorClientPersona = educatorClientPersonaRepository.findById(educatorClientPersonaId).orElseThrow(() -> new ResourceNotFoundException("educator client persona not found"));
        educator.addEducatorClientPersona(educatorClientPersona);
        return educatorRepository.save(educator);
    }

    public Educator attachToOutlets(UUID educatorId, List<UUID> outletIds) {
        Educator educator = findById(educatorId);
        List<Outlet> outlets = outletsService.findByIds(outletIds);
        List<OutletEducatorAttachment> outletEducatorAttachments = outlets.stream().map(outlet -> Attachment.createAndSync(outlet, educator, new OutletEducatorAttachment())).toList();
        outletEducatorAttachmentRepository.saveAll(outletEducatorAttachments);
        return educator;
    }

}
