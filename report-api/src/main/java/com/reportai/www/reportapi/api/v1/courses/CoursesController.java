package com.reportai.www.reportapi.api.v1.courses;

import com.reportai.www.reportapi.entities.Course;
import com.reportai.www.reportapi.services.courses.CoursesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Courses APIs", description = "APIs for managing a Courses resource")
@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class CoursesController {

    private final CoursesService coursesService;

    @Autowired
    public CoursesController(CoursesService coursesService) {
        this.coursesService = coursesService;
    }

    //Courses
    @GetMapping("/institutions/{id}/courses")
    @PreAuthorize("hasRole(#id + '_institution-admin')")
    public ResponseEntity<List<Course>> getAllCourses(@PathVariable UUID id) {
        List<Course> courses = coursesService.getAllCoursesFromInstitution(id);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping("/institutions/{id}/courses")
    @PreAuthorize("hasRole(#id + '_institution-admin')")
    public ResponseEntity<Course> createCourse(@RequestBody Course newCourse, @PathVariable UUID id) {
        Course createdCourse = coursesService.createCourseForInstitution(newCourse, id);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @PostMapping("/institutions/{id}/courses/batch")
    @PreAuthorize("hasRole(#id + '_institution-admin')")
    public ResponseEntity<List<Course>> batchCreateCourses(@RequestBody List<Course> newCourses, @PathVariable UUID id) {
        List<Course> createdCourses = coursesService.batchCreateCoursesForInstitution(
                newCourses, id);
        return new ResponseEntity<>(createdCourses, HttpStatus.CREATED);
    }
}
