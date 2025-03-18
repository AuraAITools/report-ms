package com.reportai.www.reportapi.services.courses;

import com.reportai.www.reportapi.entities.Course;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Lesson;
import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.repositories.CourseRepository;
import com.reportai.www.reportapi.services.common.BaseServiceTemplate;
import com.reportai.www.reportapi.services.educators.EducatorsService;
import com.reportai.www.reportapi.services.institutions.InstitutionsService;
import com.reportai.www.reportapi.services.lessons.LessonsService;
import com.reportai.www.reportapi.services.levels.LevelsService;
import com.reportai.www.reportapi.services.outlets.OutletsService;
import com.reportai.www.reportapi.services.students.StudentsService;
import com.reportai.www.reportapi.services.subjects.SubjectsService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CoursesService implements BaseServiceTemplate<Course, UUID> {

    private final CourseRepository courseRepository;

    private final InstitutionsService institutionsService;

    private final EducatorsService educatorsService;

    private final StudentsService studentsService;

    private final OutletsService outletsService;

    private final LevelsService levelsService;

    private final SubjectsService subjectsService;

    private final LessonsService lessonsService;

    private final ModelMapper modelMapper;

    @Autowired
    public CoursesService(CourseRepository courseRepository, InstitutionsService institutionsService, EducatorsService educatorsService, StudentsService studentsService, OutletsService outletsService, LevelsService levelsService, SubjectsService subjectsService, LessonsService lessonsService, ModelMapper modelMapper) {
        this.courseRepository = courseRepository;
        this.institutionsService = institutionsService;
        this.educatorsService = educatorsService;
        this.studentsService = studentsService;
        this.outletsService = outletsService;
        this.levelsService = levelsService;
        this.subjectsService = subjectsService;
        this.lessonsService = lessonsService;
        this.modelMapper = modelMapper;
    }

    @Override
    public JpaRepository<Course, UUID> getRepository() {
        return this.courseRepository;
    }

    // Courses
    public List<Course> getAllCoursesFromInstitution(@NonNull UUID institutionId) {
        Institution institution = institutionsService.findById(institutionId);
        return institution.getCourses();
    }

    @Transactional
    public Course createCourseForOutlet(@NonNull Course course, @NonNull UUID outletId) {
        Outlet outlet = outletsService.findById(outletId);// this method might throw assertion error for the id not being null
        course.addOutlet(outlet).addInstitution(outlet.getInstitution());
        return courseRepository.save(course);
    }

    @Transactional
    public Course updateCourseForOutlet(@NonNull UUID courseId, @NonNull Course updatedCourse) {
        Course course = findById(courseId);
        modelMapper.map(updatedCourse, course);
        return courseRepository.save(course);
    }


    @Transactional
    public Course addStudentsToCourse(@NonNull UUID courseId, @NonNull List<UUID> studentIds) {
        Course course = findById(courseId);
        List<Student> students = studentsService.findByIds(studentIds);
        course.addStudents(students);
        return courseRepository.save(course);
    }

    @Transactional
    public Course addEducatorsToCourse(@NonNull UUID courseId, @NonNull List<UUID> educatorIds) {
        List<Educator> educators = educatorsService.findByIds(educatorIds);
        Course course = findById(courseId);
        course.addEducators(educators);
        return courseRepository.save(course);
    }


    @Transactional
    public Course addLevelToCourse(@NonNull UUID courseId, @NonNull UUID levelId) {
        Level level = levelsService.findById(levelId);
        Course course = findById(courseId);
        course.addLevel(level);
        return courseRepository.save(course);
    }

    @Transactional
    public Course addSubjectsToCourse(@NonNull UUID courseId, @NonNull List<UUID> subjectIds) {
        List<Subject> subjects = subjectsService.findByIds(subjectIds);
        Course course = findById(courseId);
        course.addSubjects(subjects);
        return courseRepository.save(course);
    }

    @Transactional
    public Course addLessonsToCourse(@NonNull UUID courseId, @NonNull List<UUID> lessonIds) {
        List<Lesson> lessons = lessonsService.findByIds(lessonIds);
        Course course = findById(courseId);
        course.addLessons(lessons);
        return courseRepository.save(course);
    }

}
