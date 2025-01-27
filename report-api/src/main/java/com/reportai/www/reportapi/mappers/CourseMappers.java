package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.courses.requests.CreateCourseDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CreateCourseDTOResponse;
import com.reportai.www.reportapi.entities.Course;
import com.reportai.www.reportapi.entities.PriceRecord;
import java.util.UUID;

public class CourseMappers {
    private CourseMappers() {
    }

    /**
     * does not deal with converting subjectids,level ids and educatorids
     *
     * @param createCourseDTO
     * @return
     */
    public static Course convert(CreateCourseDTO createCourseDTO, UUID id) {
        return Course
                .builder()
                .name(createCourseDTO.getName())
                .maxSize(createCourseDTO.getMaxSize())
                .priceRecord(PriceRecord
                        .builder()
                        .price(createCourseDTO.getPrice())
                        .frequency(createCourseDTO.getPriceFrequency())
                        .tenantId(id.toString())
                        .build())
                .lessonFrequency(createCourseDTO.getLessonFrequency())
                .startDate(createCourseDTO.getStartDate())
                .endDate(createCourseDTO.getEndDate())
                .startTime(createCourseDTO.getStartTime())
                .endTime(createCourseDTO.getEndTime())
                .tenantId(id.toString())
                .build();
    }

    public static CreateCourseDTOResponse convert(Course course) {
        return CreateCourseDTOResponse
                .builder()
                .id(course.getId().toString())
                .name(course.getName())
                .maxSize(course.getMaxSize())
                .price(course.getPriceRecord().getPrice())
                .priceFrequency(course.getPriceRecord().getFrequency())
                .lessonFrequency(course.getLessonFrequency())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .startTime(course.getStartTime())
                .endTime(course.getEndTime())
                .build();
    }
}
