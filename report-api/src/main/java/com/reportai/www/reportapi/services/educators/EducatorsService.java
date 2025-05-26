package com.reportai.www.reportapi.services.educators;

import com.reportai.www.reportapi.api.v1.educators.requests.CreateEducatorRequestDTO;
import com.reportai.www.reportapi.entities.attachments.EducatorCourseAttachment;
import com.reportai.www.reportapi.entities.attachments.LevelEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.OutletEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.SubjectEducatorAttachment;
import com.reportai.www.reportapi.entities.educators.Educator;
import com.reportai.www.reportapi.entities.helpers.Attachment;
import com.reportai.www.reportapi.repositories.EducatorCourseAttachmentRepository;
import com.reportai.www.reportapi.repositories.EducatorRepository;
import com.reportai.www.reportapi.repositories.LevelEducatorAttachmentRepository;
import com.reportai.www.reportapi.repositories.OutletEducatorAttachmentRepository;
import com.reportai.www.reportapi.repositories.SubjectEducatorAttachmentRepository;
import com.reportai.www.reportapi.services.common.ISimpleRead;
import com.reportai.www.reportapi.services.courses.CoursesService;
import com.reportai.www.reportapi.services.institutions.InstitutionsService;
import com.reportai.www.reportapi.services.levels.LevelsService;
import com.reportai.www.reportapi.services.outlets.OutletsService;
import com.reportai.www.reportapi.services.subjects.SubjectsService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class EducatorsService implements ISimpleRead<Educator> {
    private final EducatorRepository educatorRepository;
    private final InstitutionsService institutionsService;
    private final OutletsService outletsService;

    private final OutletEducatorAttachmentRepository outletEducatorAttachmentRepository;
    private final LevelsService levelsService;
    private final SubjectsService subjectsService;
    private final LevelEducatorAttachmentRepository levelEducatorAttachmentRepository;
    private final SubjectEducatorAttachmentRepository subjectEducatorAttachmentRepository;
    private final ModelMapper modelMapper;
    private final CoursesService coursesService;
    private final EducatorCourseAttachmentRepository educatorCourseAttachmentRepository;

    @Autowired
    public EducatorsService(EducatorRepository educatorRepository, InstitutionsService institutionsService, OutletsService outletsService, OutletEducatorAttachmentRepository outletEducatorAttachmentRepository, LevelsService levelsService, SubjectsService subjectsService, LevelEducatorAttachmentRepository levelEducatorAttachmentRepository, SubjectEducatorAttachmentRepository subjectEducatorAttachmentRepository, ModelMapper modelMapper, @Lazy CoursesService coursesService, EducatorCourseAttachmentRepository educatorCourseAttachmentRepository) {
        this.educatorRepository = educatorRepository;
        this.institutionsService = institutionsService;
        this.outletsService = outletsService;
        this.outletEducatorAttachmentRepository = outletEducatorAttachmentRepository;
        this.levelsService = levelsService;
        this.subjectsService = subjectsService;
        this.levelEducatorAttachmentRepository = levelEducatorAttachmentRepository;
        this.subjectEducatorAttachmentRepository = subjectEducatorAttachmentRepository;
        this.modelMapper = modelMapper;
        this.coursesService = coursesService;
        this.educatorCourseAttachmentRepository = educatorCourseAttachmentRepository;
    }

    @Override
    public JpaRepository<Educator, UUID> getRepository() {
        return educatorRepository;
    }

    @Transactional
    public Educator create(CreateEducatorRequestDTO createEducatorRequestDTO) {
        Educator educator = modelMapper.map(createEducatorRequestDTO, Educator.class);
        educatorRepository.saveAndFlush(educator);
        // register level educator can teach
        List<LevelEducatorAttachment> levels = levelsService.findByIds(createEducatorRequestDTO.getLevelIds())
                .stream()
                .map(level -> Attachment.createAndSync(level, educator, new LevelEducatorAttachment()))
                .toList();
        // register subjects educator can teach
        List<SubjectEducatorAttachment> subjects = subjectsService
                .findByIds(createEducatorRequestDTO.getSubjectIds())
                .stream()
                .map(subject -> Attachment.createAndSync(subject, educator, new SubjectEducatorAttachment()))
                .toList();

        // register outlets educator can teach in
        if (!createEducatorRequestDTO.getOutletIds().isEmpty()) {
            List<OutletEducatorAttachment> outlets = outletsService
                    .findByIds(createEducatorRequestDTO.getOutletIds())
                    .stream()
                    .map(outlet -> Attachment.createAndSync(outlet, educator, new OutletEducatorAttachment()))
                    .toList();
            outletEducatorAttachmentRepository.saveAll(outlets);

        }

        // register courses educator can teach
        if (!createEducatorRequestDTO.getCourseIds().isEmpty()) {
            List<EducatorCourseAttachment> courses = coursesService
                    .findByIds(createEducatorRequestDTO.getCourseIds())
                    .stream()
                    .map(course -> Attachment.createAndSync(educator, course, new EducatorCourseAttachment()))
                    .toList();
            educatorCourseAttachmentRepository.saveAll(courses);
        }

        // save all attachments
        levelEducatorAttachmentRepository.saveAll(levels);
        subjectEducatorAttachmentRepository.saveAll(subjects);

        return educator;
    }

}
