package com.reportai.www.reportapi.services.courses;

import com.reportai.www.reportapi.api.v1.courses.requests.CreateCourseRequestDTO;
import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.attachments.EducatorCourseAttachment;
import com.reportai.www.reportapi.entities.attachments.StudentCourseRegistration;
import com.reportai.www.reportapi.entities.attachments.SubjectCourseAttachment;
import com.reportai.www.reportapi.entities.courses.Course;
import com.reportai.www.reportapi.entities.educators.Educator;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import com.reportai.www.reportapi.exceptions.lib.ResourceAlreadyExistsException;
import com.reportai.www.reportapi.repositories.CourseRepository;
import com.reportai.www.reportapi.repositories.StudentCourseRegistrationRepository;
import com.reportai.www.reportapi.services.attachments.EducatorCourseAttachmentService;
import com.reportai.www.reportapi.services.attachments.StudentCourseRegistrationService;
import com.reportai.www.reportapi.services.attachments.SubjectCourseAttachmentService;
import com.reportai.www.reportapi.services.common.ISimpleCreate;
import com.reportai.www.reportapi.services.common.ISimpleRead;
import com.reportai.www.reportapi.services.educators.EducatorsService;
import com.reportai.www.reportapi.services.lessons.LessonsService;
import com.reportai.www.reportapi.services.levels.LevelsService;
import com.reportai.www.reportapi.services.outlets.OutletsService;
import com.reportai.www.reportapi.services.students.StudentsService;
import com.reportai.www.reportapi.services.subjects.SubjectsService;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


import static com.reportai.www.reportapi.mappers.CourseMappers.convert;
import static java.util.Collections.disjoint;

@Service
public class CoursesService implements ISimpleCreate<Course>, ISimpleRead<Course> {

    private final CourseRepository courseRepository;

    private final EducatorsService educatorsService;

    private final StudentsService studentsService;

    private final OutletsService outletsService;

    private final LevelsService levelsService;

    private final SubjectsService subjectsService;

    private final LessonsService lessonsService;

    private final ModelMapper modelMapper;

    private final StudentCourseRegistrationRepository studentCourseRegistrationRepository;

    private final SubjectCourseAttachmentService subjectCourseAttachmentService;

    private final StudentCourseRegistrationService studentCourseRegistrationService;

    private final EducatorCourseAttachmentService educatorCourseAttachmentService;

    @Autowired
    public CoursesService(CourseRepository courseRepository, EducatorsService educatorsService, StudentsService studentsService, OutletsService outletsService, LevelsService levelsService, SubjectsService subjectsService, LessonsService lessonsService, ModelMapper modelMapper,
                          StudentCourseRegistrationRepository studentCourseRegistrationRepository, @Lazy SubjectCourseAttachmentService subjectCourseAttachmentService, @Lazy StudentCourseRegistrationService studentCourseRegistrationService, @Lazy EducatorCourseAttachmentService educatorCourseAttachmentService) {
        this.courseRepository = courseRepository;
        this.educatorsService = educatorsService;
        this.studentsService = studentsService;
        this.outletsService = outletsService;
        this.levelsService = levelsService;
        this.subjectsService = subjectsService;
        this.lessonsService = lessonsService;
        this.modelMapper = modelMapper;
        this.studentCourseRegistrationRepository = studentCourseRegistrationRepository;
        this.subjectCourseAttachmentService = subjectCourseAttachmentService;
        this.studentCourseRegistrationService = studentCourseRegistrationService;
        this.educatorCourseAttachmentService = educatorCourseAttachmentService;
    }

    @Override
    public JpaRepository<Course, UUID> getRepository() {
        return this.courseRepository;
    }

    @Transactional
    public Course createCourseForOutlet(@NonNull Course course, @NonNull UUID outletId) {
        Outlet outlet = outletsService.getReference(outletId);
        course.addOutlet(outlet);
        return ISimpleCreate.super.create(course);
    }

    // TODO: refactor pending
    @Transactional
    public Course createWithRelations(@NonNull CreateCourseRequestDTO createCourseRequestDTO, UUID outletId) {
        Course createdCourse = createCourseForOutlet(convert(createCourseRequestDTO), outletId);
        updateCourseLevel(createdCourse, createCourseRequestDTO.getLevelId());
        createCourseRequestDTO.getSubjectIds().forEach(subjectId -> updateCourseSubjects(createdCourse, List.of(subjectId)));
        createCourseRequestDTO.getEducatorIds().forEach(educatorId -> addEducatorsToCourse(createdCourse, List.of(educatorId)));
        return createdCourse;
    }


    @Transactional
    public Course update(@NonNull UUID courseId, @NonNull Course updates) {
        Course course = findById(courseId);
        modelMapper.map(updates, course);
        return courseRepository.save(course);
    }

    @Transactional
    public List<StudentCourseRegistration> registerStudentsToCourse(UUID courseId, List<UUID> studentIds) {
        Course course = findById(courseId);
        List<Student> incomingRegistrations = studentsService.findByIds(studentIds);
        Set<Student> existingRegisteredStudents = course
                .getStudentCourseRegistrations()
                .stream()
                .map(StudentCourseRegistration::getStudent)
                .collect(Collectors.toSet());

        if (!disjoint(incomingRegistrations, existingRegisteredStudents)) {
            throw new ResourceAlreadyExistsException("some incoming students are already registered to course");
        }


        List<StudentCourseRegistration> newlyCreatedStudentRegistrations = incomingRegistrations
                .stream()
                .map(student -> studentCourseRegistrationService.attach(student.getId(), course.getId(), new StudentCourseRegistration()))
                .toList();

        return studentCourseRegistrationRepository.saveAll(newlyCreatedStudentRegistrations);
    }

    @Transactional
    public void deregisterStudentsFromCourse(UUID courseId, List<UUID> studentIds) {
        Course course = findById(courseId);
        List<Student> studentToDeregister = studentsService.findByIds(studentIds);
        Set<Student> existingRegisteredStudents = course
                .getStudentCourseRegistrations()
                .stream()
                .map(StudentCourseRegistration::getStudent)
                .collect(Collectors.toSet());

        if (disjoint(studentToDeregister, existingRegisteredStudents)) {
            throw new ResourceAlreadyExistsException("students are not registered to course");
        }

        course
                .getStudentCourseRegistrations()
                .stream()
                .filter(studentRegistration -> studentToDeregister.contains(studentRegistration.getStudent()))
                .forEach(studentRegistration -> studentCourseRegistrationService.detach(studentRegistration.getId()));
    }


    /**
     * Attach educators to course
     *
     * @param course
     * @param educatorIds
     * @return
     */
    @Transactional
    public List<EducatorCourseAttachment> addEducatorsToCourse(@NonNull Course course, @NonNull List<UUID> educatorIds) {
        List<Educator> incomingEducatorAttachments = educatorsService.findByIds(educatorIds);
        Set<Educator> existingAttachedEducators = course
                .getEducatorCourseAttachments()
                .stream()
                .map(EducatorCourseAttachment::getEducator)
                .collect(Collectors.toSet());

        if (!disjoint(incomingEducatorAttachments, existingAttachedEducators)) {
            throw new ResourceAlreadyExistsException("some incoming students are already registered to course");
        }


        return incomingEducatorAttachments
                .stream()
                .map(educator -> educatorCourseAttachmentService.attach(educator.getId(), course.getId(), new EducatorCourseAttachment()))
                .toList();
    }


    /**
     * Update the course level
     *
     * @param course  hibernate managed course entity
     * @param levelId level id
     * @return
     */
    @Transactional
    public Level updateCourseLevel(@NonNull Course course, @NonNull UUID levelId) {
        Level level = levelsService.getReference(levelId);
        course.addLevel(level);
        courseRepository.save(course);
        return level;
    }

    // TODO: refactor to be accurate
    @Transactional
    public void updateCourseSubjects(@NonNull Course course, @NonNull List<UUID> subjectIds) {
        List<Subject> subjects = subjectsService.findByIds(subjectIds);
        for (Subject subject : subjects) {
            // have to detach those not in the list and attach those in the list
            subjectCourseAttachmentService.attach(subject.getId(), course.getId(), new SubjectCourseAttachment());
        }
    }

    // TODO: refactor to be accurate
    @Transactional
    public Course addLessonsToCourse(@NonNull UUID courseId, @NonNull List<UUID> lessonIds) {
        Collection<Lesson> lessons = lessonsService.findByIds(lessonIds);
        Course course = findById(courseId);
        course.addLessons(lessons);
        return courseRepository.save(course);
    }

}
