package com.reportai.www.reportapi.api.v1.courses;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.courses.requests.CreateCourseDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CreateCourseDTOResponse;
import com.reportai.www.reportapi.entities.Course;
import com.reportai.www.reportapi.entities.LessonGenerationTemplate;
import com.reportai.www.reportapi.mappers.CourseMappers;
import com.reportai.www.reportapi.mappers.LessonGenerationTemplateMappers;
import com.reportai.www.reportapi.services.courses.CoursesService;
import com.reportai.www.reportapi.services.outlets.OutletsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
// TODO: plan is to create a singular course by itself
// then FE will repeatedly call endpoints to create lessons
public class CoursesController {

    private final CoursesService coursesService;

    private final OutletsService outletsService;

    @Autowired
    public CoursesController(CoursesService coursesService, OutletsService outletsService) {
        this.coursesService = coursesService;
        this.outletsService = outletsService;
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
    public ResponseEntity<CreateCourseDTOResponse> createCourseForInstitution(@RequestBody @Valid CreateCourseDTO createCourseDTO, @PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId) {
        // TODO: add lesson generation template creation
        Course createdCourse = coursesService.createCourseForOutlet(convert(createCourseDTO, id), outletId);
        List<LessonGenerationTemplate> lessonGenerationTemplates = coursesService.createLessonGenerationTemplates(createdCourse.getId(), createCourseDTO
                .getLessonGenerationTemplates()
                .stream()
                .map(createLessonGenerationTemplateDTO -> LessonGenerationTemplateMappers.convert(createLessonGenerationTemplateDTO, id.toString()))
                .toList());
        Course courseWithLevel = coursesService.addLevelToCourse(createCourseDTO.getLevelId(), createdCourse.getId());
        createCourseDTO.getSubjectIds().forEach(subjectId -> coursesService.addSubjectToCourse(subjectId, courseWithLevel.getId()));
        createCourseDTO.getEducatorIds().ifPresent(educatorIds -> educatorIds.forEach(educatorId -> coursesService.addEducatorToCourse(educatorId, courseWithLevel.getId())));
        Course resultantCourse = coursesService.findById(courseWithLevel.getId());
        CreateCourseDTOResponse createCourseDTOResponse = convert(resultantCourse);
        createCourseDTOResponse.setLessonGenerationTemplates(lessonGenerationTemplates.stream().map(LessonGenerationTemplateMappers::convert).toList());
        return new ResponseEntity<>(createCourseDTOResponse, HttpStatus.CREATED);
    }

    @GetMapping("/institutions/{id}/outlets/{outlet_id}/courses")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses:read'")
    @Transactional
    public ResponseEntity<List<CreateCourseDTOResponse>> createCourseForInstitution(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId) {
        List<Course> courses = outletsService.getOutletCourses(outletId);
        return new ResponseEntity<>(courses.stream().map(CourseMappers::convert).toList(), HttpStatus.OK);
    }

}
