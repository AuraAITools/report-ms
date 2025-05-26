package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.courses.requests.CreateCourseRequestDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CourseResponseDTO;
import com.reportai.www.reportapi.entities.PriceRecord;
import com.reportai.www.reportapi.entities.courses.Course;

public class CourseMappers {
    private CourseMappers() {
    }

    /**
     * does not deal with converting subjectids,level ids and educatorids
     *
     * @param createCourseRequestDTO
     * @return
     */
    public static Course convert(CreateCourseRequestDTO createCourseRequestDTO) {
        return Course
                .builder()
                .name(createCourseRequestDTO.getName())
                .maxSize(createCourseRequestDTO.getMaxSize())
                .priceRecord(PriceRecord
                        .builder()
                        .price(createCourseRequestDTO.getPrice())
                        .frequency(createCourseRequestDTO.getPriceFrequency())
                        .build())
                .lessonFrequency(createCourseRequestDTO.getLessonFrequency())
                .courseStartTimestamptz(createCourseRequestDTO.getCourseStartTimestamptz())
                .courseEndTimestamptz(createCourseRequestDTO.getCourseEndTimestamptz())
                .build();
    }

    public static CourseResponseDTO convert(Course course) {
        return CourseResponseDTO
                .builder()
                .id(course.getId().toString())
                .name(course.getName())
                .maxSize(course.getMaxSize())
                .price(course.getPriceRecord().getPrice())
                .priceFrequency(course.getPriceRecord().getFrequency())
                .lessonFrequency(course.getLessonFrequency())
                .courseStartTimestamptz(course.getCourseStartTimestamptz())
                .courseEndTimestamptz(course.getCourseEndTimestamptz())
                .build();
    }

}
