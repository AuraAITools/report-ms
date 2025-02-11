package com.reportai.www.reportapi.services.courses;

import com.reportai.www.reportapi.entities.Course;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.LessonGenerationTemplate;
import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.PriceRecord;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.exceptions.lib.NotFoundException;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.CourseRepository;
import com.reportai.www.reportapi.repositories.EducatorRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.LessonGenerationTemplateRepository;
import com.reportai.www.reportapi.repositories.LevelRepository;
import com.reportai.www.reportapi.repositories.OutletRepository;
import com.reportai.www.reportapi.repositories.PriceRecordRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import com.reportai.www.reportapi.repositories.SubjectRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoursesService {

    private final CourseRepository courseRepository;

    private final InstitutionRepository institutionRepository;

    private final EducatorRepository educatorRepository;

    private final LevelRepository levelRepository;

    private final SubjectRepository subjectRepository;

    private final PriceRecordRepository priceRecordRepository;

    private final OutletRepository outletRepository;

    private final LessonGenerationTemplateRepository lessonGenerationTemplateRepository;

    private final StudentRepository studentRepository;

    @Autowired
    public CoursesService(CourseRepository courseRepository, InstitutionRepository institutionRepository, EducatorRepository educatorRepository, LevelRepository levelRepository, SubjectRepository subjectRepository, PriceRecordRepository priceRecordRepository, OutletRepository outletRepository, LessonGenerationTemplateRepository lessonGenerationTemplateRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.institutionRepository = institutionRepository;
        this.educatorRepository = educatorRepository;
        this.levelRepository = levelRepository;
        this.subjectRepository = subjectRepository;
        this.priceRecordRepository = priceRecordRepository;
        this.outletRepository = outletRepository;
        this.lessonGenerationTemplateRepository = lessonGenerationTemplateRepository;
        this.studentRepository = studentRepository;
    }


    // Courses
    public List<Course> getAllCoursesFromInstitution(UUID institutionId) {
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(() -> new NotFoundException("no institution found"));
        return institution.getCourses();
    }

    public Course findById(UUID id) {
        return courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("course does not exist"));
    }

    @Transactional
    public Course createCourseForOutlet(Course course, UUID outletId) {
        Outlet outlet = outletRepository.findById(outletId).orElseThrow(() -> new ResourceNotFoundException("outlet not found"));
        Institution institution = outlet.getInstitution();

        PriceRecord priceRecord = course.getPriceRecord();
        PriceRecord createdPriceRecord = priceRecordRepository.save(priceRecord);

        // owning side will have to set the reference to institution
        course.setInstitution(institution);
        course.setOutlet(outlet);
        course.setPriceRecord(createdPriceRecord);
        return courseRepository.save(course);
    }

    @Transactional
    public List<Course> batchCreateCoursesForInstitution(List<Course> courses, UUID institutionId) {
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(() -> new NotFoundException("no institution found"));
        courses.forEach(c -> c.setInstitution(institution));
        return courseRepository.saveAll(courses);
    }

    @Transactional
    public Course addEducatorToCourse(UUID educatorId, UUID courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("course does not exist"));
        Educator educator = educatorRepository.findById(educatorId).orElseThrow(() -> new ResourceNotFoundException("Educator does not exist"));
        if (course.getEducators() == null) {
            course.setEducators(new ArrayList<>(List.of(educator)));
        } else {
            course.getEducators().add(educator);
        }
        return courseRepository.save(course);
    }

    @Transactional
    public Course addLevelToCourse(UUID levelId, UUID courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("course does not exist"));
        Level level = levelRepository.findById(levelId).orElseThrow(() -> new ResourceNotFoundException("level does not exist"));
        course.setLevel(level);
        return courseRepository.save(course);
    }

    @Transactional
    public Course addSubjectToCourse(UUID subjectId, UUID courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("course does not exist"));
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new ResourceNotFoundException("subject does not exist"));
        if (course.getSubjects() == null) {
            course.setSubjects(new ArrayList<>(List.of(subject)));
        } else {
            course.getSubjects().add(subject);
        }
        return courseRepository.save(course);
    }

    /**
     * Creates lesson generation templates
     *
     * @return
     */
    @Transactional
    public List<LessonGenerationTemplate> createLessonGenerationTemplates(UUID courseId, List<LessonGenerationTemplate> lessonGenerationTemplates) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("course not found"));
        lessonGenerationTemplates.forEach(lessonGenerationTemplate -> lessonGenerationTemplate.setCourse(course));
        return lessonGenerationTemplateRepository.saveAll(lessonGenerationTemplates);
    }

    @Transactional
    public List<Course> enrollStudentToCourses(UUID studentId, List<UUID> courseIds) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("no student found"));
        List<Course> courses = courseRepository.findAllById(courseIds);
        List<Course> cour = student.getCourses();
        student.getCourses().addAll(courses);
        studentRepository.save(student);
        return courses;
    }
}
