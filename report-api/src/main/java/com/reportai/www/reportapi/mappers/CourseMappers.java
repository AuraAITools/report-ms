package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.courses.requests.CreateCourseRequestDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CourseResponseDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.ExpandedCourseResponse;
import com.reportai.www.reportapi.entities.PriceRecord;
import com.reportai.www.reportapi.entities.courses.Course;
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
//                .lessonNumberFrequency(createCourseRequestDTO.getLessonNumberFrequency())
                .lessonFrequency(createCourseRequestDTO.getLessonFrequency())
                .courseStartTimestamptz(createCourseRequestDTO.getCourseStartTimestamptz())
                .courseEndTimestamptz(createCourseRequestDTO.getCourseEndTimestamptz())
                .tenantId(id.toString())
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

    public static ExpandedCourseResponse convertExpanded(Course course) {

        return ExpandedCourseResponse
                .builder()
                .id(course.getId().toString())
                .name(course.getName())
                .maxSize(course.getMaxSize())
                .price(course.getPriceRecord().getPrice())
                .priceFrequency(course.getPriceRecord().getFrequency())
                .lessonFrequency(course.getLessonFrequency())
                .courseStartTimestamptz(course.getCourseStartTimestamptz())
                .courseEndTimestamptz(course.getCourseEndTimestamptz())
                .subjects(
                        course
                                .getSubjectCourseAttachments()
                                .stream()
                                .map(subjectCourseAttachment -> SubjectMappers.convert(subjectCourseAttachment.getSubject()))
                                .toList())
                .level(LevelMappers.convert(course.getLevel()))
                .outlet(OutletMappers.convert(course.getOutlet()))
                .build();
    }

}
