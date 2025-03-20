package com.reportai.www.reportapi.api.v1.lessons;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.lessons.requests.CreateLessonRequestDTO;
import com.reportai.www.reportapi.api.v1.lessons.requests.UpdateLessonRequestDTO;
import com.reportai.www.reportapi.api.v1.lessons.responses.CreateLessonResponseDTO;
import com.reportai.www.reportapi.api.v1.lessons.responses.ExpandedLessonResponseDTO;
import com.reportai.www.reportapi.entities.Course;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Lesson;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.mappers.LessonMappers;
import com.reportai.www.reportapi.services.courses.CoursesService;
import com.reportai.www.reportapi.services.educators.EducatorsService;
import com.reportai.www.reportapi.services.lessons.LessonsService;
import com.reportai.www.reportapi.services.students.StudentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
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

    private final StudentsService studentsService;

    private final EducatorsService educatorsService;

    @Autowired
    public LessonsController(LessonsService lessonService, CoursesService coursesService, StudentsService studentsService, EducatorsService educatorsService) {
        this.lessonService = lessonService;
        this.coursesService = coursesService;
        this.studentsService = studentsService;
        this.educatorsService = educatorsService;
    }

    @Operation(summary = "create lesson of course", description = "create a lesson in a course")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/outlets/{outlet_id}/courses/{course_id}/lessons")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses::lessons:create'")
    @Transactional
    public ResponseEntity<CreateLessonResponseDTO> createLessonForCourse(@RequestBody CreateLessonRequestDTO createLessonRequestDTO, @PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @PathVariable(name = "course_id") UUID courseId) {
        Course course = coursesService.findById(courseId);
        List<Student> students = studentsService.findByIds(createLessonRequestDTO.getStudentIds());
        List<Educator> educators = educatorsService.findByIds(createLessonRequestDTO.getEducatorIds());
        Lesson lesson = lessonService.createLessonForCourse(course.getId(), LessonMappers.convert(createLessonRequestDTO, id), students, educators);
        return new ResponseEntity<>(LessonMappers.convert(lesson), HttpStatus.OK);
    }

    @Operation(summary = "patch lesson of course", description = "patch a lesson in a course")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("/institutions/{id}/outlets/{outlet_id}/courses/{course_id}/lessons/{lesson_id}")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses::lessons:update'")
    @Transactional
    public ResponseEntity<ExpandedLessonResponseDTO> updateLessonForCourse(@RequestBody UpdateLessonRequestDTO updateLessonRequestDTO, @PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @PathVariable(name = "course_id") UUID courseId, @PathVariable(name = "lesson_id") UUID lessonId) {
        Lesson lesson = lessonService.update(lessonId, updateLessonRequestDTO);
        return new ResponseEntity<>(LessonMappers.convertToExpanded(lesson), HttpStatus.OK);
    }

    @Operation(summary = "get lessons of a course", description = "get lessons of a course")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/outlets/{outlet_id}/courses/{course_id}/lessons")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses::lessons:read'")
    @Transactional
    public ResponseEntity<List<CreateLessonResponseDTO>> getAllLessonsOfCourse(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @PathVariable(name = "course_id") UUID courseId) {
        Course course = coursesService.findById(courseId);
        List<CreateLessonResponseDTO> lessons = course.getLessons().stream().map(LessonMappers::convert).toList();
        return new ResponseEntity<>(lessons, HttpStatus.OK);
    }

    @Operation(summary = "get expanded lessons of a course", description = "get lessons of a course")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/outlets/{outlet_id}/lessons/expand")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::lessons:read'")
    @Transactional
    public ResponseEntity<List<ExpandedLessonResponseDTO>> getAllLessonsOfInstitution(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId) {
        List<Lesson> lessons = lessonService.getExpandedLessonsInInstitution(id);
        return new ResponseEntity<>(lessons.stream().map(LessonMappers::convertToExpanded).toList(), HttpStatus.OK);
    }
}
