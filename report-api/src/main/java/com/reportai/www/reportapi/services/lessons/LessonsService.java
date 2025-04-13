package com.reportai.www.reportapi.services.lessons;

import com.reportai.www.reportapi.api.v1.lessons.requests.UpdateLessonRequestDTO;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.attachments.EducatorLessonAttachment;
import com.reportai.www.reportapi.entities.attachments.StudentLessonRegistration;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import com.reportai.www.reportapi.entities.courses.Course;
import com.reportai.www.reportapi.entities.educators.Educator;
import com.reportai.www.reportapi.entities.helpers.Attachment;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import com.reportai.www.reportapi.entities.lessons.LessonView;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.EducatorLessonAttachmentRepository;
import com.reportai.www.reportapi.repositories.LessonRepository;
import com.reportai.www.reportapi.repositories.StudentLessonRegistrationRepository;
import com.reportai.www.reportapi.repositories.views.LessonViewRepository;
import com.reportai.www.reportapi.services.common.BaseServiceTemplate;
import com.reportai.www.reportapi.services.courses.CoursesService;
import com.reportai.www.reportapi.services.educators.EducatorsService;
import com.reportai.www.reportapi.services.students.StudentsService;
import com.reportai.www.reportapi.services.subjects.SubjectsService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LessonsService implements BaseServiceTemplate<Lesson, UUID> {

    private final LessonRepository lessonRepository;

    private final LessonViewRepository lessonViewRepository;
    private final CoursesService coursesService;

    private final ModelMapper modelMapper;

    private final EducatorsService educatorsService;

    private final StudentsService studentsService;

    private final SubjectsService subjectsService;

    private final StudentLessonRegistrationRepository studentLessonRegistrationRepository;

    private final EducatorLessonAttachmentRepository educatorLessonAttachmentRepository;


    @Autowired
    public LessonsService(LessonRepository lessonRepository, @Lazy CoursesService coursesService, EducatorsService educatorsService, StudentsService studentsService, SubjectsService subjectsService, StudentLessonRegistrationRepository studentLessonRegistrationRepository, EducatorLessonAttachmentRepository educatorLessonAttachmentRepository, LessonViewRepository lessonViewRepository) {
        this.lessonRepository = lessonRepository;
        this.coursesService = coursesService;
        this.educatorsService = educatorsService;
        this.studentsService = studentsService;
        this.subjectsService = subjectsService;
        this.studentLessonRegistrationRepository = studentLessonRegistrationRepository;
        this.educatorLessonAttachmentRepository = educatorLessonAttachmentRepository;
        this.lessonViewRepository = lessonViewRepository;
        this.modelMapper = new ModelMapper();
        this.modelMapper
                .getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);// skip null values
        // TODO: refactor Converters and Configurers to be in separate files instead of in the constructor
        // Educators converter with null check
        Converter<List<UUID>, List<Educator>> educatorIdsToEducatorsConverter = context -> {
            if (context.getSource() == null) {
                return null;
            }
            List<UUID> educatorIds = context.getSource();
            return educatorIds.stream()
                    .map(educatorsService::findById)
                    .collect(Collectors.toList());
        };

        // Students converter with null check
        Converter<List<UUID>, List<Student>> studentIdsToStudentsConverter = context -> {
            if (context.getSource() == null) {
                return null;
            }
            List<UUID> studentIds = context.getSource();
            return studentIds.stream()
                    .map(studentsService::findById)
                    .collect(Collectors.toList());
        };

        // Subject converter with null check that properly looks up the subject
        Converter<UUID, Subject> subjectIdToSubjectConverter = context -> {
            if (context.getSource() == null) {
                return null;
            }
            UUID subjectId = context.getSource();
            return subjectsService.findById(subjectId);
        };
//
////        FIXME: fix this
//        this.modelMapper
//                .typeMap(UpdateLessonRequestDTO.class, Lesson.class);
    }

    @Override
    public JpaRepository<Lesson, UUID> getRepository() {
        return lessonRepository;
    }

    /**
     * Creates a lesson for a course
     *
     * @param courseId
     * @param lesson
     * @param studentIds
     * @param educatorIds
     * @return
     */
    @Transactional
    public Lesson createLessonForCourse(@NonNull UUID courseId, Lesson lesson, List<UUID> studentIds, List<UUID> educatorIds) {
        Course course = coursesService.findById(courseId);

        lesson.setOutlet(course.getOutlet());
        lesson.setCourse(course);

        Lesson createdLesson = lessonRepository.save(lesson);

        // register students if it exists
        if (studentIds != null && !studentIds.isEmpty()) {
            List<Student> students = studentsService.findByIds(studentIds);
            registerStudentsToLesson(students, createdLesson);
        }

        // register educators if it exists
        if (educatorIds != null && !educatorIds.isEmpty()) {
            List<Educator> educators = educatorsService.findByIds(educatorIds);
            attachEducatorsToLesson(educators, createdLesson);
        }

        return createdLesson;
    }


    @Transactional
    public void registerStudentsToLesson(List<Student> students, Lesson lesson) {
        List<StudentLessonRegistration> studentLessonRegistrations = students
                .stream()
                .map(student -> Attachment.createAndSync(student, lesson, new StudentLessonRegistration()))
                .toList();

        studentLessonRegistrationRepository.saveAll(studentLessonRegistrations).stream().map(StudentLessonRegistration::getStudent).toList();
    }

    @Transactional
    public void deregisterStudentsFromLesson(List<Student> students, Lesson lesson) {
        List<StudentLessonRegistration> studentLessonRegistrations = studentLessonRegistrationRepository.findAllByStudent_IdInAndLesson_Id(students.stream().map(TenantAwareBaseEntity::getId).toList(), lesson.getId());

        if (studentLessonRegistrations.size() != students.size()) {
            throw new ResourceNotFoundException("some students were not found");
        }

        studentLessonRegistrationRepository.delete(studentLessonRegistrations.getFirst());
        log.info("delete finished");
    }


    @Transactional
    public void attachEducatorsToLesson(List<Educator> educators, Lesson lesson) {
        List<EducatorLessonAttachment> educatorLessonAttachments = educators
                .stream()
                .map(educator -> Attachment.createAndSync(educator, lesson, new EducatorLessonAttachment()))
                .toList();
        educatorLessonAttachmentRepository.saveAll(educatorLessonAttachments).stream().map(EducatorLessonAttachment::getEducator).toList();
    }

    @Transactional
    public void detachEducatorFromLesson(List<Educator> educators, Lesson lesson) {
        List<EducatorLessonAttachment> educatorLessonAttachments = educatorLessonAttachmentRepository
                .findAllByEducator_IdInAndLesson_Id(educators.stream().map(TenantAwareBaseEntity::getId).toList(), lesson.getId());
        if (educatorLessonAttachments.size() != educators.size()) {
            throw new ResourceNotFoundException("some educators were not found");
        }

        educatorLessonAttachmentRepository.deleteAll(educatorLessonAttachments);
    }

    public List<StudentLessonRegistration> getAllStudentLessonRegistrationsInLesson(UUID lessonId) {
        return studentLessonRegistrationRepository.findAllByLesson_Id(lessonId);
    }

    public List<EducatorLessonAttachment> getAllEducatorLessonAttachmentsInLesson(UUID lessonId) {
        return educatorLessonAttachmentRepository.findAllByLesson_Id(lessonId);
    }

    /**
     * @return
     */
    @Transactional
    public List<Lesson> getLessonsInInstitution() {
        return lessonRepository.findAll();
    }

    /**
     * @param outletId
     * @return list of lessons
     */
    @Transactional
    public List<LessonView> getLessonsInOutlet(@NonNull UUID outletId) {
        return lessonViewRepository.findAllByOutletId(outletId);
    }

    /**
     * updates lesson
     * but this is the easiest way rn
     *
     * @param updates
     * @return
     */
    @Transactional
    public Lesson update(UUID lessonId, UpdateLessonRequestDTO updates) {
        Lesson lesson = findById(lessonId);
        // model map to overlay the updates onto an existing lesson
        modelMapper.map(updates, lesson);

        if (updates.getSubjectId() != null) {
            Subject updatedSubject = subjectsService.findById(updates.getSubjectId());
            lesson.setSubject(updatedSubject);
        }

        if (updates.getStudentIds() != null) {
            List<StudentLessonRegistration> studentLessonRegistrations = getAllStudentLessonRegistrationsInLesson(lessonId);
            List<UUID> existingStudentIds = studentLessonRegistrations.stream().map(studentLessonRegistration -> studentLessonRegistration.getStudent().getId()).toList();
            // register new students
            List<UUID> newStudentIds = updates.getStudentIds().stream().filter(id -> !existingStudentIds.contains(id)).toList();
            if (!newStudentIds.isEmpty()) {
                registerStudentsToLesson(studentsService.findByIds(newStudentIds), lesson);
            }
            // deregister students not in request
            List<UUID> deregisteredStudentIds = existingStudentIds.stream().filter(id -> !updates.getStudentIds().contains(id)).toList();
            if (!deregisteredStudentIds.isEmpty()) {
                deregisterStudentsFromLesson(studentsService.findByIds(deregisteredStudentIds), lesson);
            }
        }

        if (updates.getEducatorIds() != null) {
            List<EducatorLessonAttachment> educatorLessonAttachments = getAllEducatorLessonAttachmentsInLesson(lessonId);
            List<UUID> existingEducatorIds = educatorLessonAttachments.stream().map(educatorLessonAttachment -> educatorLessonAttachment.getEducator().getId()).toList();
            // attach new educators
            List<UUID> newEducatorIds = updates.getEducatorIds().stream().filter(id -> !existingEducatorIds.contains(id)).toList();
            if (!newEducatorIds.isEmpty()) {
                attachEducatorsToLesson(educatorsService.findByIds(newEducatorIds), lesson);
            }
            // detach students not in request
            List<UUID> detachedEducatorIds = existingEducatorIds.stream().filter(id -> !updates.getEducatorIds().contains(id)).toList();

            if (!detachedEducatorIds.isEmpty()) {
                detachEducatorFromLesson(educatorsService.findByIds(detachedEducatorIds), lesson);
            }
        }
        return lessonRepository.save(lesson);
    }

}
