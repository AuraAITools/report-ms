package com.reportai.www.reportapi.services.lessons;

import com.reportai.www.reportapi.entities.Course;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Lesson;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.repositories.LessonRepository;
import com.reportai.www.reportapi.repositories.specifications.TenantSpecification;
import com.reportai.www.reportapi.services.common.BaseServiceTemplate;
import com.reportai.www.reportapi.services.courses.CoursesService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class LessonsService implements BaseServiceTemplate<Lesson, UUID> {

    private final LessonRepository lessonRepository;

    private final CoursesService coursesService;

    @Autowired
    public LessonsService(LessonRepository lessonRepository, @Lazy CoursesService coursesService) {
        this.lessonRepository = lessonRepository;
        this.coursesService = coursesService;
    }

    @Override
    public JpaRepository<Lesson, UUID> getRepository() {
        return lessonRepository;
    }

    /**
     * TODO: should be just create lesson not to course
     *
     * @param courseId
     * @param lesson
     * @param students
     * @param educators
     * @return
     */
    @Transactional
    public Lesson createLessonForCourse(@NonNull UUID courseId, Lesson lesson, List<Student> students, List<Educator> educators) {
        Course course = coursesService.findById(courseId);
        lesson.setOutlet(course.getOutlet());
        lesson.setCourse(course);
        lesson.setStudents(students);

        // Make sure educators are attached to the persistence context
        lesson.addEducators(educators);
        return lessonRepository.save(lesson);
    }

    /**
     * TODO: refactor
     *
     * @param institutionId
     * @return
     */
    @Transactional
    public List<Lesson> getExpandedLessonsInInstitution(@NonNull UUID institutionId) {
        return lessonRepository.findAll(TenantSpecification.forTenant(institutionId.toString()));
    }

}
