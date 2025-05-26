package com.reportai.www.reportapi.services.lessons;

import com.reportai.www.reportapi.api.v1.lessons.requests.CreateLessonRequestDTO;
import com.reportai.www.reportapi.api.v1.lessons.requests.UpdateLessonRequestDTO;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.attachments.EducatorLessonAttachment;
import com.reportai.www.reportapi.entities.attachments.StudentLessonRegistration;
import com.reportai.www.reportapi.entities.educators.Educator;
import com.reportai.www.reportapi.entities.helpers.Attachment;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import com.reportai.www.reportapi.entities.lessons.LessonHomeworkCompletion;
import com.reportai.www.reportapi.entities.lessons.LessonOutletRoomBooking;
import com.reportai.www.reportapi.entities.lessons.LessonParticipationReview;
import com.reportai.www.reportapi.entities.lessons.StudentLessonAttendance;
import com.reportai.www.reportapi.exceptions.lib.ResourceAlreadyExistsException;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.EducatorLessonAttachmentRepository;
import com.reportai.www.reportapi.repositories.LessonRepository;
import com.reportai.www.reportapi.repositories.StudentLessonRegistrationRepository;
import com.reportai.www.reportapi.repositories.TopicSubjectAttachmentRepository;
import com.reportai.www.reportapi.services.common.ISimpleCreate;
import com.reportai.www.reportapi.services.common.ISimpleRead;
import com.reportai.www.reportapi.services.common.ISimpleUpdate;
import com.reportai.www.reportapi.services.courses.CoursesService;
import com.reportai.www.reportapi.services.educators.EducatorsService;
import com.reportai.www.reportapi.services.outlets.OutletRoomService;
import com.reportai.www.reportapi.services.outlets.OutletsService;
import com.reportai.www.reportapi.services.students.StudentsService;
import com.reportai.www.reportapi.services.subjects.SubjectsService;
import com.reportai.www.reportapi.services.topics.TopicsService;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


// TODO: rewrite to use UUID instead of entity objects

/**
 * Use of ExistingResource Monad is to at compile time prevent transient entities from being passed into these methods that require the entity to be managed
 */
@Slf4j
@Service
public class LessonsService implements ISimpleCreate<Lesson>, ISimpleRead<Lesson>, ISimpleUpdate<Lesson> {
    private final TopicSubjectAttachmentRepository topicSubjectAttachmentRepository;

    private final LessonRepository lessonRepository;

    private final CoursesService coursesService;

    private final EducatorsService educatorsService;

    private final StudentsService studentsService;

    private final SubjectsService subjectsService;

    private final StudentLessonRegistrationRepository studentLessonRegistrationRepository;

    private final EducatorLessonAttachmentRepository educatorLessonAttachmentRepository;

    private final OutletRoomService outletRoomService;

    private final OutletsService outletsService;

    private final TopicsService topicsService;

    private final ModelMapper modelMapper;


    @Autowired
    public LessonsService(LessonRepository lessonRepository, @Lazy CoursesService coursesService, EducatorsService educatorsService, StudentsService studentsService, SubjectsService subjectsService, StudentLessonRegistrationRepository studentLessonRegistrationRepository, EducatorLessonAttachmentRepository educatorLessonAttachmentRepository, OutletRoomService outletRoomService, OutletsService outletsService, TopicsService topicsService, ModelMapper modelMapper,
                          TopicSubjectAttachmentRepository topicSubjectAttachmentRepository) {
        this.lessonRepository = lessonRepository;
        this.coursesService = coursesService;
        this.educatorsService = educatorsService;
        this.studentsService = studentsService;
        this.subjectsService = subjectsService;
        this.studentLessonRegistrationRepository = studentLessonRegistrationRepository;
        this.educatorLessonAttachmentRepository = educatorLessonAttachmentRepository;
        this.outletRoomService = outletRoomService;
        this.outletsService = outletsService;
        this.topicsService = topicsService;
        this.modelMapper = modelMapper;
        this.topicSubjectAttachmentRepository = topicSubjectAttachmentRepository;
    }


    @Override
    public JpaRepository<Lesson, UUID> getRepository() {
        return lessonRepository;
    }

    /**
     * Creates a pure lesson with outlet and course
     * relations in a lesson such as subjects, student registrations, educator attachments, outlet rooms are not created
     *
     * @param entity
     * @return created hibernate managed lesson entity
     */
    public Lesson create(Lesson entity, UUID outletId, UUID courseId) {
        Objects.requireNonNull(outletId);
        Objects.requireNonNull(courseId);
        entity.setOutlet(outletsService.findById(outletId));
        entity.setCourse(coursesService.findById(courseId));
        return ISimpleCreate.super.create(entity);
    }

    /**
     * creates a lesson in a course under an outlet
     * creates subject if requested
     * creates outlet room if requested
     * creates student registrations if requested
     * creates educator attachments if requested
     *
     * @param createLessonRequestDTO
     * @param outletId
     * @param courseId
     * @return Lesson that is created
     */
    @Transactional
    public Lesson create(CreateLessonRequestDTO createLessonRequestDTO, UUID outletId, UUID courseId) {
        Objects.requireNonNull(outletId, "outletId cannot be null");
        Objects.requireNonNull(courseId, "courseId cannot be null");

        Lesson lesson = modelMapper.map(createLessonRequestDTO, Lesson.class);
        create(lesson, outletId, courseId);
        if (createLessonRequestDTO.getSubjectId() != null) {
            updateLessonSubject(lesson.getId(), createLessonRequestDTO.getSubjectId()); //TODO: include in FE
        }
        if (createLessonRequestDTO.getOutletRoomId() != null) {
            updateOutletRooms(lesson.getId(), createLessonRequestDTO.getOutletRoomId());
        }
        if (!createLessonRequestDTO.getStudentIds().isEmpty()) {
            updateStudentRegistrations(lesson.getId(), createLessonRequestDTO.getStudentIds());
        }

        if (!createLessonRequestDTO.getEducatorIds().isEmpty()) {
            updateEducatorAttachments(lesson.getId(), createLessonRequestDTO.getEducatorIds());
        }

        return lesson;
    }

    /**
     * @param lessonId     a hibernate managed lesson entity
     * @param outletRoomId an existing outlet room id
     * @return
     */
    @Transactional
    public LessonOutletRoomBooking updateOutletRooms(UUID lessonId, UUID outletRoomId) {
        Lesson lesson = findById(lessonId);
        Objects.requireNonNull(outletRoomId);
        return outletRoomService.bookRoom(outletRoomId, lesson);
    }


    @Transactional
    public List<StudentLessonRegistration> updateStudentRegistrations(UUID lessonId, List<UUID> studentIds) {
        // register students if it exists
        Objects.requireNonNull(studentIds);
        Lesson lesson = findById(lessonId);
        if (!studentIds.isEmpty()) {
            return registerStudentsToLesson(studentIds, lesson.getId());
        }

        return Collections.emptyList();
    }

    @Transactional
    public List<EducatorLessonAttachment> updateEducatorAttachments(UUID lessonId, List<UUID> educatorIds) {
        // register educators if it exists
        Objects.requireNonNull(educatorIds);
        if (!educatorIds.isEmpty()) {
            attachEducatorsToLesson(educatorIds, lessonId);
        }
        return Collections.emptyList();
    }


    /**
     * @param outletId
     * @return list of lessons
     */
    @Transactional
    public List<Lesson> getLessonsInOutlet(UUID outletId) {
        return lessonRepository.findAllByOutletId(outletId);
    }

    /**
     * updates lesson
     * To add subject to lesson, add id into subject relation in lesson, if subject is null, the subject of the lesson will be removed
     * if studentLessonRegistration is null, no changes are made, if studentLessonRegistration is emptyList all students will be deregistered
     * if educatorLessonAttachment is null, no changes are made, if educatorLessonAttachment is emptyList all educators will be deregistered
     *
     * @param lessonId
     * @param updates
     * @return
     */
    @Transactional
    public Lesson updateLessonAndRelationships(UUID lessonId, UpdateLessonRequestDTO updates) {

        Lesson lesson = findById(lessonId);

        updateLessonSubject(lesson.getId(), updates.getSubjectId());

        // TODO: can make more performant with native JPQL queries
        // FIXME: registering double students, one with null lesson_id
        replaceStudentRegistrationsOfLesson(lesson.getId(), updates.getStudentIds());

        // TODO: make more performant by using native queries
        // deregister educators not in request
        replaceEducatorAttachmentsOfLesson(lesson.getId(), updates.getEducatorIds());

        // overlays incoming updates on existing lesson
        modelMapper.map(updates, lesson);
        return lessonRepository.save(lesson);
    }

    /**
     * replace current educator attachments of the target lesson
     * to the new educator lesson attachments in the request
     *
     * @param lessonId
     * @param educatorIds
     */
    @Transactional
    public void replaceEducatorAttachmentsOfLesson(UUID lessonId, List<UUID> educatorIds) {
        Lesson lesson = findById(lessonId);
        // if studentIds are empty, deregister all students
        if (educatorIds.isEmpty()) {
            educatorLessonAttachmentRepository.deleteAllByLesson_Id(lesson.getId());
            return;
        }

        // register students that are not already registered
        int registered = educatorLessonAttachmentRepository.registerNewEducatorAttachments(educatorIds, lesson.getId(), lesson.getTenantId());
        log.info("attached {} educators", registered);

        // deregister students that are not in the request
        int deregistered = educatorLessonAttachmentRepository.deleteEducatorLessonAttachmentsNotIn(educatorIds, lesson.getId(), lesson.getTenantId());
        log.info("deregistered {} educators", deregistered);
        educatorLessonAttachmentRepository.flush();

    }

    /**
     * replace current student registrations of the target lesson
     * to the new student registrations in the request
     *
     * @param lessonId
     * @param studentIds
     */
    @Transactional
    public void replaceStudentRegistrationsOfLesson(UUID lessonId, List<UUID> studentIds) {
        Lesson lesson = findById(lessonId);
        // if studentIds are empty, deregister all students
        if (studentIds.isEmpty()) {
            studentLessonRegistrationRepository.deleteAllByLesson_Id(lesson.getId());
            return;
        }

        // register students that are not already registered
        int registered = studentLessonRegistrationRepository.registerNewStudentLessonRegistrations(studentIds, lesson.getId(), lesson.getTenantId());
        log.info("registered {} students", registered);

        // deregister students that are not in the request
        int deregistered = studentLessonRegistrationRepository.deleteStudentLessonRegistrationsNotIn(studentIds, lesson.getId(), lesson.getTenantId());
        log.info("deregistered {} students", deregistered);
        studentLessonRegistrationRepository.flush();
    }

    /**
     * update lesson subject
     *
     * @param lessonId  a hibernate managed lesson entity
     * @param subjectId if subject id is null, the subject of the lesson will be removed
     * @return
     */
    @Transactional
    public Lesson updateLessonSubject(UUID lessonId, UUID subjectId) {
        Lesson lesson = findById(lessonId);
        if (subjectId != null) {
            Subject subject = subjectsService.findById(subjectId);
            lesson.setSubject(subject);
            lessonRepository.save(lesson);
            return lesson;
        }

        lesson.setSubject(null);
        return lesson;
    }

    //    public List<StudentLessonRegistration> getAllStudentLessonRegistrationsInLesson(UUID lessonId) {
//        return studentLessonRegistrationRepository.findAllByLesson_Id(lessonId);
//    }
    public List<EducatorLessonAttachment> getAllEducatorLessonAttachmentsInLesson(UUID lessonId) {
        return educatorLessonAttachmentRepository.findAllByLesson_Id(lessonId);
    }

    /**
     * removes educatorsAttachments from a lesson
     *
     * @param educatorIds
     * @param lessonId
     */
    @Transactional
    public void detachEducatorsFromLesson(List<UUID> educatorIds, UUID lessonId) {
        List<EducatorLessonAttachment> educatorLessonAttachments = educatorLessonAttachmentRepository.findAllByEducator_IdInAndLesson_Id(educatorIds, lessonId);
        if (educatorLessonAttachments.size() != educatorIds.size()) {
            throw new ResourceNotFoundException("some educators were not found");
        }

        educatorLessonAttachmentRepository.deleteAll(educatorLessonAttachments);
    }

    @Transactional
    public List<StudentLessonRegistration> registerStudentsToLesson(List<UUID> studentIds, UUID lessonId) {
        List<Student> students = studentsService.findByIds(studentIds);
        Lesson lesson = findById(lessonId);
        List<StudentLessonRegistration> studentLessonRegistrations = students
                .stream()
                .map(student -> {

                    // if student already has a lesson scheduled at this time fail registration
                    List<StudentLessonRegistration> overlappingLessonRegistrations = studentLessonRegistrationRepository.findOverlappingLessonRegistrationsByStudentId(student.getId(), lesson.getLessonStartTimestamptz(), lesson.getLessonEndTimestamptz());

                    if (!overlappingLessonRegistrations.isEmpty()) {
                        throw new ResourceAlreadyExistsException(String.format("student %s already has overlapping lesson", student.getName()));
                    }

                    StudentLessonRegistration studentLessonRegistration = Attachment.createAndSync(student, lesson, new StudentLessonRegistration());
                    LessonParticipationReview lessonParticipationReview = createLessonParticipationReview(studentLessonRegistration);
                    LessonHomeworkCompletion lessonHomeworkCompletion = createLessonHomeworkCompletion(studentLessonRegistration);
                    StudentLessonAttendance studentLessonAttendance = createStudentLessonAttendance(studentLessonRegistration);
                    studentLessonRegistration.setLessonParticipationReview(lessonParticipationReview);
                    studentLessonRegistration.setLessonHomeworkCompletion(lessonHomeworkCompletion);
                    studentLessonRegistration.setStudentLessonAttendance(studentLessonAttendance);
                    return studentLessonRegistration;
                })
                .toList();

        return studentLessonRegistrationRepository.saveAllAndFlush(studentLessonRegistrations);
    }


    @Transactional
    public void attachEducatorsToLesson(List<UUID> educatorIds, UUID lessonId) {
        List<Educator> educators = educatorsService.findByIds(educatorIds);
        Lesson lesson = findById(lessonId);
        List<EducatorLessonAttachment> educatorLessonAttachments = educators

                .stream()
                .map(educator -> {
                    // if educator has conflicting classes at that time fail attachment
                    List<EducatorLessonAttachment> overlappingLessonAttachments = educatorLessonAttachmentRepository.findOverlappingLessonAttachmentsByEducatorId(educator.getId(), lesson.getLessonStartTimestamptz(), lesson.getLessonEndTimestamptz());

                    if (!overlappingLessonAttachments.isEmpty()) {
                        throw new ResourceAlreadyExistsException(String.format("educator %s already teaching a lesson", educator.getName()));
                    }

                    return Attachment.createAndSync(educator, lesson, new EducatorLessonAttachment());
                })
                .toList();
        educatorLessonAttachmentRepository.saveAll(educatorLessonAttachments).stream().map(EducatorLessonAttachment::getEducator).toList();
    }

    private LessonParticipationReview createLessonParticipationReview(StudentLessonRegistration studentLessonRegistration) {
        return LessonParticipationReview
                .builder()
                .participationType(null)
                .studentLessonRegistration(studentLessonRegistration)
                .build();
    }

    private LessonHomeworkCompletion createLessonHomeworkCompletion(StudentLessonRegistration studentLessonRegistration) {
        return LessonHomeworkCompletion
                .builder()
                .studentLessonRegistration(studentLessonRegistration)
                .completion(null)
                .build();
    }

    private StudentLessonAttendance createStudentLessonAttendance(StudentLessonRegistration studentLessonRegistration) {
        return StudentLessonAttendance
                .builder()
                .studentLessonRegistration(studentLessonRegistration)
                .attended(false)
                .build();
    }

}
