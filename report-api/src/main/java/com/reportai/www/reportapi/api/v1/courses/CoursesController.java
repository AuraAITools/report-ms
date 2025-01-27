package com.reportai.www.reportapi.api.v1.courses;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.courses.requests.CreateCourseDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CreateCourseDTOResponse;
import com.reportai.www.reportapi.entities.Course;
import com.reportai.www.reportapi.services.courses.CoursesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static com.reportai.www.reportapi.mappers.CourseMappers.convert;

@Tag(name = "Courses APIs", description = "APIs for managing a Courses resource. Courses must be created under an outlet")
@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
// TODO: Courses need to be composed under Outlet
// plan is to create a singular course by itself
// then FE will repeatedly call endpoints to create lessons
public class CoursesController {

    private final CoursesService coursesService;

    @Autowired
    public CoursesController(CoursesService coursesService) {
        this.coursesService = coursesService;
    }

    /**
     * Creates a course in an institution
     * Also links the level, subjects and educators to the created course
     *
     * @param createCourseDTO
     * @param id
     * @return
     */

    @PostMapping("/institutions/{id}/outlets/{outlet_id}/courses")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses:create'")
    @Transactional
    public ResponseEntity<CreateCourseDTOResponse> createCourseForInstitution(@RequestBody CreateCourseDTO createCourseDTO, @PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId) {
        Course createdCourse = coursesService.createCourseForInstitution(convert(createCourseDTO, id), id);
        coursesService.addLevelToCourse(createCourseDTO.getLevelId(), createdCourse.getId());
        createCourseDTO.getSubjectIds().forEach(subjectId -> coursesService.addSubjectToCourse(subjectId, createdCourse.getId()));
        createCourseDTO.getEducatorIds().forEach(educatorId -> coursesService.addEducatorToCourse(educatorId, createdCourse.getId()));
        Course resultantCourse = coursesService.findById(createdCourse.getId());
        return new ResponseEntity<>(convert(resultantCourse), HttpStatus.CREATED);
    }

}
