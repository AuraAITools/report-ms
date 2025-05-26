package com.reportai.www.reportapi.mappers.mapper.converters.courses;

import com.reportai.www.reportapi.api.v1.courses.responses.CourseResponseDTO;
import com.reportai.www.reportapi.entities.courses.Course;
import com.reportai.www.reportapi.mappers.mapper.ModelMapperConfigurer;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class CourseToCourseResponseConverter implements Converter<Course, CourseResponseDTO>, ModelMapperConfigurer {
    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper.addConverter(this);
    }

    @Override
    public CourseResponseDTO convert(MappingContext<Course, CourseResponseDTO> mappingContext) {
        Course course = mappingContext.getSource();
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
