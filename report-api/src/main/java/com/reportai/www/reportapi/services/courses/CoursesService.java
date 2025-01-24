package com.reportai.www.reportapi.services.courses;

import com.reportai.www.reportapi.entities.Course;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.exceptions.lib.NotFoundException;
import com.reportai.www.reportapi.repositories.CourseRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoursesService {

    private final CourseRepository courseRepository;

    private final InstitutionRepository institutionRepository;

    @Autowired
    public CoursesService(CourseRepository courseRepository, InstitutionRepository institutionRepository) {
        this.courseRepository = courseRepository;
        this.institutionRepository = institutionRepository;
    }

    // Courses
    public List<Course> getAllCoursesFromInstitution(UUID institutionId) {
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(() -> new NotFoundException("no institution found"));
        return institution.getCourses();
    }

    @Transactional
    public Course createCourseForInstitution(Course course, UUID institutionId) {
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(() -> new NotFoundException("no institution found"));
        // owning side will have to set the reference to institution
        course.setInstitution(institution);
        return courseRepository.save(course);
    }

    @Transactional
    public List<Course> batchCreateCoursesForInstitution(List<Course> courses, UUID institutionId) {
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(() -> new NotFoundException("no institution found"));
        courses.forEach(c -> c.setInstitution(institution));
        return courseRepository.saveAll(courses);
    }
}
