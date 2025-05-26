package com.reportai.www.reportapi.api.v1.lessons;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.lessons.requests.CreateLessonRequestDTO;
import com.reportai.www.reportapi.api.v1.lessons.requests.UpdateLessonRequestDTO;
import com.reportai.www.reportapi.api.v1.lessons.responses.ExpandedLessonResponseDTO;
import com.reportai.www.reportapi.api.v1.lessons.responses.LessonResponseDTO;
import com.reportai.www.reportapi.entities.courses.Course;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import com.reportai.www.reportapi.entities.views.LessonView;
import com.reportai.www.reportapi.repositories.views.LessonViewRepository;
import com.reportai.www.reportapi.services.courses.CoursesService;
import com.reportai.www.reportapi.services.lessons.LessonsService;
import com.reportai.www.reportapi.utils.LessonViewProjectionDecorator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final LessonViewRepository lessonViewRepository;

    private final LessonsService lessonService;

    private final CoursesService coursesService;

    private final LessonViewProjectionDecorator lessonViewProjectionDecorator;

    private final ModelMapper modelMapper;

    @Autowired
    public LessonsController(LessonViewRepository lessonViewRepository, LessonsService lessonService, CoursesService coursesService, LessonViewProjectionDecorator lessonViewProjectionDecorator, ModelMapper modelMapper) {
        this.lessonViewRepository = lessonViewRepository;
        this.lessonService = lessonService;
        this.coursesService = coursesService;
        this.lessonViewProjectionDecorator = lessonViewProjectionDecorator;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "create lesson of course", description = "create a lesson in a course")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/outlets/{outlet_id}/courses/{course_id}/lessons")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses::lessons:create'")
    @Transactional
    public ResponseEntity<ExpandedLessonResponseDTO> createLessonForCourseInOutlet(@RequestBody @Valid CreateLessonRequestDTO createLessonRequestDTO, @PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @PathVariable(name = "course_id") UUID courseId) {
        Lesson lesson = lessonService.create(createLessonRequestDTO, outletId, courseId);
        return new ResponseEntity<>(modelMapper.map(lesson, ExpandedLessonResponseDTO.class), HttpStatus.OK);
    }

    @Operation(summary = "create a batch of lessons of course", description = "create a batch of lessons in a course")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/outlets/{outlet_id}/courses/{course_id}/lessons/batch")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses::lessons:create'")
    @Transactional
    public ResponseEntity<List<ExpandedLessonResponseDTO>> batchCreateLessonForCourseInOutlet(@RequestBody @Valid List<CreateLessonRequestDTO> createLessonRequestDTOs, @PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @PathVariable(name = "course_id") UUID courseId) {

        List<ExpandedLessonResponseDTO> expandedLessonResponseDTOs = createLessonRequestDTOs
                .stream()
                .map(createLessonRequestDTO -> lessonService.create(createLessonRequestDTO, outletId, courseId))
                .map(lesson -> modelMapper.map(lesson, ExpandedLessonResponseDTO.class))
                .toList();

        return new ResponseEntity<>(expandedLessonResponseDTOs, HttpStatus.OK);
    }

    @Operation(summary = "update lesson of course", description = "update a lesson in a course")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("/institutions/{id}/outlets/{outlet_id}/courses/{course_id}/lessons/{lesson_id}")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses::lessons:update'")
    @Transactional
    public ResponseEntity<ExpandedLessonResponseDTO> updateLessonForCourseInOutlet(@RequestBody UpdateLessonRequestDTO updateLessonRequestDTO, @PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @PathVariable(name = "course_id") UUID courseId, @PathVariable(name = "lesson_id") UUID lessonId) {
        Lesson lesson = lessonService.updateLessonAndRelationships(lessonId, updateLessonRequestDTO);
        LessonView lessonView = lessonViewProjectionDecorator.project(lesson);
        return new ResponseEntity<>(modelMapper.map(lessonView, ExpandedLessonResponseDTO.class), HttpStatus.OK);
    }

    @Operation(summary = "get lessons of a course", description = "get lessons of a course")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/outlets/{outlet_id}/courses/{course_id}/lessons")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses::lessons:read'")
    @Transactional
    public ResponseEntity<List<LessonResponseDTO>> getAllLessonsOfCourseInOutlet(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @PathVariable(name = "course_id") UUID courseId) {
        Course course = coursesService.findById(courseId);
        List<LessonView> lessonViews = lessonViewProjectionDecorator.projectAll(course.getLessons().stream().toList());
        List<LessonResponseDTO> lessonResponseDTOs = lessonViews
                .stream()
                .map(lesson -> modelMapper.map(lesson, LessonResponseDTO.class))
                .toList();
        return new ResponseEntity<>(lessonResponseDTOs, HttpStatus.OK);
    }

    @Operation(summary = "get expanded lessons in an outlet", description = "get lessons in an outlet")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/outlets/{outlet_id}/lessons/expand")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::lessons:read'")
    @Transactional
    public ResponseEntity<List<ExpandedLessonResponseDTO>> getAllLessonsInOutlet(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId) {

        List<ExpandedLessonResponseDTO> expandedLessonResponseDTOs = lessonService
                .getLessonsInOutlet(outletId)
                .stream()
                .map(lesson -> modelMapper.map(lesson, ExpandedLessonResponseDTO.class))
                .toList();
        return new ResponseEntity<>(expandedLessonResponseDTOs, HttpStatus.OK);
    }
}
