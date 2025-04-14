package com.reportai.www.reportapi.api.v1.lessons;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.lessons.requests.CreateLessonRequestDTO;
import com.reportai.www.reportapi.api.v1.lessons.requests.UpdateLessonRequestDTO;
import com.reportai.www.reportapi.api.v1.lessons.responses.ExpandedLessonResponseDTO;
import com.reportai.www.reportapi.api.v1.lessons.responses.LessonResponseDTO;
import com.reportai.www.reportapi.entities.courses.Course;
import com.reportai.www.reportapi.entities.views.LessonView;
import com.reportai.www.reportapi.mappers.LessonMappers;
import com.reportai.www.reportapi.services.courses.CoursesService;
import com.reportai.www.reportapi.services.lessons.LessonsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Lessons APIs", description = "APIs for creating lessons for a course")
@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class LessonsController {
    private final LessonsService lessonService;

    private final CoursesService coursesService;

    @Autowired
    public LessonsController(LessonsService lessonService, CoursesService coursesService) {
        this.lessonService = lessonService;
        this.coursesService = coursesService;
    }

    @Operation(summary = "create lesson of course", description = "create a lesson in a course")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/outlets/{outlet_id}/courses/{course_id}/lessons")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses::lessons:create'")
    @Transactional
    public ResponseEntity<LessonResponseDTO> createLessonForCourse(@RequestBody @Valid CreateLessonRequestDTO createLessonRequestDTO, @PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @PathVariable(name = "course_id") UUID courseId) {
        LessonView lessonView = lessonService.createLessonForCourse(courseId, LessonMappers.convert(createLessonRequestDTO, id), createLessonRequestDTO.getStudentIds(), createLessonRequestDTO.getEducatorIds());
        return new ResponseEntity<>(LessonMappers.convert(lessonView), HttpStatus.OK);
    }

    @Operation(summary = "patch lesson of course", description = "patch a lesson in a course")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("/institutions/{id}/outlets/{outlet_id}/courses/{course_id}/lessons/{lesson_id}")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses::lessons:update'")
    @Transactional
    public ResponseEntity<ExpandedLessonResponseDTO> updateLessonForCourse(@RequestBody UpdateLessonRequestDTO updateLessonRequestDTO, @PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @PathVariable(name = "course_id") UUID courseId, @PathVariable(name = "lesson_id") UUID lessonId) {
        LessonView lessonView = lessonService.update(lessonId, updateLessonRequestDTO);
        return new ResponseEntity<>(LessonMappers.convertToExpanded(lessonView), HttpStatus.OK);
    }

    @Operation(summary = "get lessons of a course", description = "get lessons of a course")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/outlets/{outlet_id}/courses/{course_id}/lessons")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses::lessons:read'")
    @Transactional
    public ResponseEntity<List<LessonResponseDTO>> getAllLessonsOfCourse(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @PathVariable(name = "course_id") UUID courseId) {
        Course course = coursesService.findById(courseId);
        List<LessonResponseDTO> lessons = course.getLessonsView().stream().map(LessonMappers::convert).toList();
        return new ResponseEntity<>(lessons, HttpStatus.OK);
    }

    @Operation(summary = "get expanded lessons in an outlet", description = "get lessons in an outlet")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/outlets/{outlet_id}/lessons/expand")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::lessons:read'")
    @Transactional
    public ResponseEntity<List<ExpandedLessonResponseDTO>> getAllLessonsOfOutlet(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId) {
        List<LessonView> lessons = lessonService.getLessonsInOutlet(outletId);
        return new ResponseEntity<>(lessons.stream().map(LessonMappers::convertToExpanded).toList(), HttpStatus.OK);
    }
}
