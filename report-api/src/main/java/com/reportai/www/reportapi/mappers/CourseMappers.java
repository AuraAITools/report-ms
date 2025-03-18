package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.courses.requests.CreateCourseRequestDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CreateCourseDTOResponseDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.ExpandedCourseResponse;
import com.reportai.www.reportapi.entities.Course;
import com.reportai.www.reportapi.entities.PriceRecord;
import java.util.UUID;

public class CourseMappers {
    private CourseMappers() {
    }

    /**
     * does not deal with converting subjectids,level ids and educatorids
     *
     * @param createCourseRequestDTO
     * @return
     */
    public static Course convert(CreateCourseRequestDTO createCourseRequestDTO, UUID id) {
        return Course
                .builder()
                .name(createCourseRequestDTO.getName())
                .maxSize(createCourseRequestDTO.getMaxSize())
                .priceRecord(PriceRecord
                        .builder()
                        .price(createCourseRequestDTO.getPrice())
                        .frequency(createCourseRequestDTO.getPriceFrequency())
                        .tenantId(id.toString())
                        .build())
                .lessonNumberFrequency(createCourseRequestDTO.getLessonNumberFrequency())
                .lessonWeeklyFrequency(createCourseRequestDTO.getLessonWeeklyFrequency())
                .startDate(createCourseRequestDTO.getStartDate())
                .endDate(createCourseRequestDTO.getEndDate())
                .startTime(createCourseRequestDTO.getStartTime())
                .endTime(createCourseRequestDTO.getEndTime())
                .tenantId(id.toString())
                .build();
    }

    public static CreateCourseDTOResponseDTO convert(Course course) {
        return CreateCourseDTOResponseDTO
                .builder()
                .id(course.getId().toString())
                .name(course.getName())
                .maxSize(course.getMaxSize())
                .price(course.getPriceRecord().getPrice())
                .priceFrequency(course.getPriceRecord().getFrequency())
                .lessonNumberFrequency(course.getLessonNumberFrequency())
                .lessonWeeklyFrequency(course.getLessonWeeklyFrequency())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .startTime(course.getStartTime())
                .endTime(course.getEndTime())
                .build();
    }

    public static ExpandedCourseResponse convertExpanded(Course course) {
        return ExpandedCourseResponse
                .builder()
                .id(course.getId().toString())
                .name(course.getName())
                .maxSize(course.getMaxSize())
                .price(course.getPriceRecord().getPrice())
                .priceFrequency(course.getPriceRecord().getFrequency())
                .lessonNumberFrequency(course.getLessonNumberFrequency())
                .lessonWeeklyFrequency(course.getLessonWeeklyFrequency())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .startTime(course.getStartTime())
                .endTime(course.getEndTime())
                .subjects(course.getSubjects().stream().map(SubjectMappers::convert).toList())
                .lessons(course.getLessons().stream().map(LessonMappers::convert).toList())
                .level(LevelMappers.convert(course.getLevel()))
                .outlet(OutletMappers.convert(course.getOutlet()))
                .build();
    }

}
