package com.reportai.www.reportapi.api.v1.courses;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.courses.requests.CreateCourseRequestDTO;
import com.reportai.www.reportapi.api.v1.courses.requests.UpdateCourseRequestDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CourseResponseDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.ExpandedCourseResponse;
import com.reportai.www.reportapi.entities.PriceRecord;
import com.reportai.www.reportapi.entities.courses.Course;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import com.reportai.www.reportapi.services.courses.CoursesService;
import com.reportai.www.reportapi.services.lessons.LessonsService;
import com.reportai.www.reportapi.services.outlets.OutletsService;
import com.reportai.www.reportapi.utils.LessonViewProjectionDecorator;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Conditions;
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


import static com.reportai.www.reportapi.mappers.CourseMappers.convert;

//TODO: refactor and maybe use projection for lesson view instead of an actual view
@Tag(name = "Courses APIs", description = "APIs for managing a Courses resource. Courses must be created under an outlet")
@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class CoursesController {

    private final CoursesService coursesService;

    private final OutletsService outletsService;

    private final ModelMapper modelMapper;

    private final LessonViewProjectionDecorator lessonViewProjectionDecorator;

    private final LessonsService lessonsService;

    @Autowired
    public CoursesController(CoursesService coursesService, LessonsService lessonsService, OutletsService outletsService, ModelMapper modelMapper, LessonViewProjectionDecorator lessonViewProjectionDecorator) {
        this.coursesService = coursesService;
        this.lessonsService = lessonsService;
        this.outletsService = outletsService;
        this.modelMapper = modelMapper;
        this.lessonViewProjectionDecorator = lessonViewProjectionDecorator;
    }

    /**
     * Creates a course in an institution
     * Also links the level, subjects and educators to the created course
     *
     * @param createCourseRequestDTO
     * @param id
     * @return
     */
    @PostMapping("/institutions/{id}/outlets/{outlet_id}/courses")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses:create'")
    @Transactional
    public ResponseEntity<CourseResponseDTO> createCourseForOutlet(@RequestBody @Valid CreateCourseRequestDTO createCourseRequestDTO, @PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId) {

        Course createdCourse = coursesService.createWithRelations(createCourseRequestDTO, outletId);
        // INFO: batch create lessons
        createCourseRequestDTO
                .getLessons()
                .forEach(createLessonRequestDTO -> { // for each generated lesson request
                    Lesson lesson = modelMapper.map(createLessonRequestDTO, Lesson.class);
                    lessonsService.create(lesson, outletId, createdCourse.getId());

                    if (createLessonRequestDTO.getSubjectId() != null) {
                        lessonsService.updateLessonSubject(lesson.getId(), createLessonRequestDTO.getSubjectId()); //TODO: include in DTO and FE
                    }
                    lessonsService.updateOutletRooms(lesson.getId(), createLessonRequestDTO.getOutletRoomId());
                    lessonsService.updateStudentRegistrations(lesson.getId(), createLessonRequestDTO.getStudentIds());
                    lessonsService.updateEducatorAttachments(lesson.getId(), createLessonRequestDTO.getEducatorIds());
                });

        CourseResponseDTO courseResponseDTO = convert(createdCourse);
        return new ResponseEntity<>(courseResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/institutions/{id}/outlets/{outlet_id}/courses")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses:read'")
    @Transactional
    public ResponseEntity<List<CourseResponseDTO>> getAllCoursesOfOutlet(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId) {
        Collection<Course> courses = outletsService.getOutletCourses(outletId);
        List<CourseResponseDTO> courseResponseDTOS = courses
                .stream()
                .map(course -> modelMapper.map(course, CourseResponseDTO.class))
                .toList();
        return new ResponseEntity<>(courseResponseDTOS, HttpStatus.OK);
    }

    @GetMapping("/institutions/{id}/outlets/{outlet_id}/courses/expand")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses:read'")
    @Transactional
    public ResponseEntity<List<ExpandedCourseResponse>> getAllExpandedCoursesOfOutlet(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId) {
        Collection<Course> courses = outletsService.getOutletCourses(outletId);
        List<ExpandedCourseResponse> expandedCourseResponses = courses
                .stream()
                .map(course -> modelMapper.map(course, ExpandedCourseResponse.class))
                .toList();
        return new ResponseEntity<>(expandedCourseResponses, HttpStatus.OK);
    }

    // TODO: relook

    /**
     * updates a course in an institution
     * Also links the level, subjects and educators to the created course
     *
     * @param updateCourseRequestDTO
     * @param id
     * @return
     */
    @PatchMapping("/institutions/{id}/outlets/{outlet_id}/courses/{course_id}")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::courses:update'")
    @Transactional
    public ResponseEntity<CourseResponseDTO> updateCourseForOutlet(@RequestBody @Valid UpdateCourseRequestDTO updateCourseRequestDTO, @PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @PathVariable(name = "course_id") UUID courseId) {
        Course updates = Course.builder().build();
        ModelMapper customModelMapper = new ModelMapper();
        customModelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setPropertyCondition(Conditions.isNotNull());
        customModelMapper.createTypeMap(UpdateCourseRequestDTO.class, Course.class)
//                .addMappings(mapper -> {
//                    mapper.skip(UpdateCourseRequestDTO::getPrice, Course::setPrice);
//                    mapper.skip(UpdateCourseRequestDTO::getPriceFrequency, Course::setPriceFrequency);
//                })
                .setPostConverter(context -> {
                    UpdateCourseRequestDTO source = context.getSource();
                    Course destination = context.getDestination();

                    // Handle PriceRecord mapping
                    if (source.getPrice() != null || source.getPriceFrequency() != null) {
                        PriceRecord priceRecord = destination.getPriceRecord();
                        if (priceRecord == null) {
                            priceRecord = new PriceRecord();
                            destination.addPriceRecord(priceRecord);
                        }

                        if (source.getPrice() != null) {
                            priceRecord.setPrice(source.getPrice());
                        }
                        if (source.getPriceFrequency() != null) {
                            priceRecord.setFrequency(source.getPriceFrequency());
                        }
                        priceRecord.setTenantId(id.toString());
                    }

                    return destination;
                });
        customModelMapper.map(updateCourseRequestDTO, updates);
        Course updatedCourse = coursesService.update(courseId, updates);
        return new ResponseEntity<>(convert(updatedCourse), HttpStatus.OK);
    }

}
