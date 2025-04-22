package com.reportai.www.reportapi.api.v1.lessons;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.lessons.requests.CreateLessonRequestDTO;
import com.reportai.www.reportapi.api.v1.lessons.requests.UpdateLessonRequestDTO;
import com.reportai.www.reportapi.api.v1.lessons.responses.ExpandedLessonResponseDTO;
import com.reportai.www.reportapi.api.v1.lessons.responses.LessonResponseDTO;
import com.reportai.www.reportapi.api.v1.outletrooms.requests.OutletRoomResponseDTO;
import com.reportai.www.reportapi.entities.courses.Course;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import com.reportai.www.reportapi.entities.views.LessonView;
import com.reportai.www.reportapi.mappers.LessonMappers;
import com.reportai.www.reportapi.repositories.LessonOutletRoomBookingRepository;
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
    private final LessonOutletRoomBookingRepository lessonOutletRoomBookingRepository;
    private final LessonsService lessonService;

    private final CoursesService coursesService;

    private final LessonViewProjectionDecorator lessonViewProjectionDecorator;


    private final ModelMapper modelMapper;

    @Autowired
    public LessonsController(LessonViewRepository lessonViewRepository, LessonOutletRoomBookingRepository lessonOutletRoomBookingRepository, LessonsService lessonService, CoursesService coursesService, LessonViewProjectionDecorator lessonViewProjectionDecorator, ModelMapper modelMapper) {
        this.lessonViewRepository = lessonViewRepository;
        this.lessonOutletRoomBookingRepository = lessonOutletRoomBookingRepository;
        this.lessonService = lessonService;
        this.coursesService = coursesService;
        this.lessonViewProjectionDecorator = lessonViewProjectionDecorator;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "create lesson of course", description = "create a lesson in a course")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/outlets/{outlet_id}/courses/{course_id}/lessons")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses::lessons:create'")
    public ResponseEntity<LessonResponseDTO> createLessonForCourse(@RequestBody @Valid CreateLessonRequestDTO createLessonRequestDTO, @PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @PathVariable(name = "course_id") UUID courseId) {
        LessonsService.CreateLessonWithOutletRoomParams createLessonWithOutletRoomParams = new LessonsService.CreateLessonWithOutletRoomParams(courseId, LessonMappers.convert(createLessonRequestDTO), createLessonRequestDTO.getStudentIds(), createLessonRequestDTO.getEducatorIds(), createLessonRequestDTO.getOutletRoomId());
        Lesson lesson = lessonService.createLessonWithOutletRoom(createLessonWithOutletRoomParams);
        LessonView lessonView = lessonViewProjectionDecorator.project(lesson);
        LessonResponseDTO lessonResponseDTO = LessonMappers.convert(lessonView);
        lessonResponseDTO.setOutletRoom(modelMapper.map(lesson.getLessonOutletRoomBooking().getOutletRoom(), OutletRoomResponseDTO.class));
        return new ResponseEntity<>(lessonResponseDTO, HttpStatus.OK);
    }

    @Operation(summary = "create a batch of lessons of course", description = "create a batch of lessons in a course")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/outlets/{outlet_id}/courses/{course_id}/lessons/batch")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses::lessons:create'")
    public ResponseEntity<List<LessonResponseDTO>> batchCreateLessonForCourse(@RequestBody @Valid List<CreateLessonRequestDTO> createLessonRequestDTOs, @PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @PathVariable(name = "course_id") UUID courseId) {
        List<LessonsService.CreateLessonWithOutletRoomParams> createLessonWithOutletRoomParamsList = createLessonRequestDTOs
                .stream()
                .map(createLessonRequestDTO -> new LessonsService.CreateLessonWithOutletRoomParams(courseId, LessonMappers.convert(createLessonRequestDTO), createLessonRequestDTO.getStudentIds(), createLessonRequestDTO.getEducatorIds(), createLessonRequestDTO.getOutletRoomId()))
                .toList();

        List<Lesson> lessons = lessonService.batchCreateLessonForCourse(createLessonWithOutletRoomParamsList);
        // TODO: find an elegant way to insert outletRoom into the DTO
        List<LessonResponseDTO> lessonResponseDTOs = lessons.stream().map(lesson -> {
            LessonView lessonView = lessonViewProjectionDecorator.project(lesson);
            LessonResponseDTO lessonResponseDTO = LessonMappers.convert(lessonView);
            lessonResponseDTO.setOutletRoom(modelMapper.map(lesson.getLessonOutletRoomBooking().getOutletRoom(), OutletRoomResponseDTO.class));
            return lessonResponseDTO;
        }).toList();
        return new ResponseEntity<>(lessonResponseDTOs, HttpStatus.OK);
    }

    @Operation(summary = "patch lesson of course", description = "patch a lesson in a course")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("/institutions/{id}/outlets/{outlet_id}/courses/{course_id}/lessons/{lesson_id}")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses::lessons:update'")
    @Transactional
    public ResponseEntity<ExpandedLessonResponseDTO> updateLessonForCourse(@RequestBody UpdateLessonRequestDTO updateLessonRequestDTO, @PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @PathVariable(name = "course_id") UUID courseId, @PathVariable(name = "lesson_id") UUID lessonId) {
        Lesson lesson = lessonService.update(lessonId, updateLessonRequestDTO);
        LessonView lessonView = lessonViewProjectionDecorator.project(lesson); // TODO: project dont work for some reasoin Entity `com.reportai.www.reportapi.entities.lessons.LessonOutletRoomBooking` with identifier value `58d079a2-7c1a-45b4-9f7e-784ab987d77d` is filtered for association `com.reportai.www.reportapi.entities.views.LessonView.lessonOutletRoomBooking
        return new ResponseEntity<>(LessonMappers.convertToExpanded(lessonView), HttpStatus.OK);
    }

    @Operation(summary = "get lessons of a course", description = "get lessons of a course")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/outlets/{outlet_id}/courses/{course_id}/lessons")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses::lessons:read'")
    public ResponseEntity<List<LessonResponseDTO>> getAllLessonsOfCourse(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @PathVariable(name = "course_id") UUID courseId) {
        Course course = coursesService.findById(courseId);
        List<LessonView> lessonViews = lessonViewProjectionDecorator.projectAll(course.getLessons().stream().toList());
        List<LessonResponseDTO> lessonResponseDTOS = lessonViews.stream().map(lessonView -> {
            LessonResponseDTO temp = LessonMappers.convert(lessonView);
            lessonOutletRoomBookingRepository.findByLessonIdAndTenantId(lessonView.getId(), lessonView.getTenantId())
                    .ifPresent(booking -> {
                        temp.setOutletRoom(modelMapper.map(booking.getOutletRoom(), OutletRoomResponseDTO.class));
                    }); // TODO: find an alternative way
            return temp;
        }).toList();
        return new ResponseEntity<>(lessonResponseDTOS, HttpStatus.OK);
    }

    @Operation(summary = "get expanded lessons in an outlet", description = "get lessons in an outlet")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/outlets/{outlet_id}/lessons/expand")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::lessons:read'")
    @Transactional
    public ResponseEntity<List<ExpandedLessonResponseDTO>> getAllLessonsOfOutlet(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId) {
        List<Lesson> lessons = lessonService.getLessonsInOutlet(outletId);

        List<ExpandedLessonResponseDTO> lessonViews = lessons
                .stream()
                .map(lesson -> {
                    LessonView lessonView = lessonViewRepository.findById(lesson.getId()).orElseThrow();
                    ExpandedLessonResponseDTO lessonResponseDTO = LessonMappers.convertToExpanded(lessonView);
                    lessonOutletRoomBookingRepository.findByLessonIdAndTenantId(lessonView.getId(), lessonView.getTenantId())
                            .ifPresent(booking -> lessonResponseDTO.setOutletRoom(modelMapper.map(booking.getOutletRoom(), OutletRoomResponseDTO.class)));
                    return lessonResponseDTO;
                })
                .toList(); // TODO: throws error Entity `com.reportai.www.reportapi.entities.lessons.LessonOutletRoomBooking` with identifier value `f7f22cd4-99b3-43d8-bcf0-3f382d0d5ef7` is filtered for association `com.reportai.www.reportapi.entities.views.LessonView.lessonOutletRoomBooking

        return new ResponseEntity<>(lessonViews, HttpStatus.OK);
    }
}
