package com.reportai.www.reportapi.services.lessons;

import com.reportai.www.reportapi.entities.Course;
import com.reportai.www.reportapi.entities.Lesson;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.CourseRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.LessonRepository;
import com.reportai.www.reportapi.repositories.OutletRepository;
import com.reportai.www.reportapi.repositories.specifications.TenantSpecification;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonsService {

    private final LessonRepository lessonRepository;

    private final CourseRepository courseRepository;

    private final InstitutionRepository institutionRepository;

    private final OutletRepository outletRepository;

    @Autowired
    public LessonsService(LessonRepository lessonRepository, CourseRepository courseRepository, InstitutionRepository institutionRepository, OutletRepository outletRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.institutionRepository = institutionRepository;
        this.outletRepository = outletRepository;
    }

    @Transactional
    public Lesson createLessonForCourse(UUID courseId, Lesson lesson) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("no course found"));
        lesson.setOutlet(course.getOutlet());
        lesson.setCourse(course);
        return lessonRepository.save(lesson);
    }

    @Transactional
    public List<Lesson> getExpandedLessonsInInstitution(UUID institutionId) {
        return lessonRepository.findAll(TenantSpecification.forTenant(institutionId.toString()));

    }
}
