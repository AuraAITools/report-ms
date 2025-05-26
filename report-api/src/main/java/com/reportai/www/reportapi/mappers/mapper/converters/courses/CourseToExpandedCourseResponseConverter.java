package com.reportai.www.reportapi.mappers.mapper.converters.courses;

import com.reportai.www.reportapi.api.v1.courses.responses.ExpandedCourseResponse;
import com.reportai.www.reportapi.api.v1.lessons.responses.LessonResponseDTO;
import com.reportai.www.reportapi.api.v1.levels.responses.LevelsResponseDTO;
import com.reportai.www.reportapi.api.v1.outlets.responses.OutletResponseDTO;
import com.reportai.www.reportapi.api.v1.subjects.responses.SubjectResponseDTO;
import com.reportai.www.reportapi.entities.attachments.SubjectCourseAttachment;
import com.reportai.www.reportapi.entities.courses.Course;
import com.reportai.www.reportapi.mappers.mapper.ModelMapperConfigurer;
import com.reportai.www.reportapi.mappers.mapper.converters.lessons.LessonToLessonResponseDTOConverter;
import java.util.List;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseToExpandedCourseResponseConverter implements Converter<Course, ExpandedCourseResponse>, ModelMapperConfigurer {


    private final LessonToLessonResponseDTOConverter lessonToLessonResponseDTOConverter;

    @Autowired
    public CourseToExpandedCourseResponseConverter(LessonToLessonResponseDTOConverter lessonToLessonResponseDTOConverter) {
        this.lessonToLessonResponseDTOConverter = lessonToLessonResponseDTOConverter;
    }

    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper.addConverter(this);
    }

    @Override
    public ExpandedCourseResponse convert(MappingContext<Course, ExpandedCourseResponse> mappingContext) {
        Course course = mappingContext.getSource();
        List<SubjectResponseDTO> subjectResponseDTOS = course
                .getSubjectCourseAttachments()
                .stream()
                .map(SubjectCourseAttachment::getSubject)
                .map(subject -> mappingContext.getMappingEngine().map(mappingContext.create(course, SubjectResponseDTO.class)))
                .toList();
        LevelsResponseDTO levelsResponseDTO = mappingContext
                .getMappingEngine()
                .map(mappingContext.create(course.getLevel(), LevelsResponseDTO.class));

        OutletResponseDTO outletResponseDTO = mappingContext
                .getMappingEngine()
                .map(mappingContext.create(course.getOutlet(), OutletResponseDTO.class));
        List<LessonResponseDTO> lessonResponseDTOS = course
                .getLessons()
                .stream()
                .map(lesson -> lessonToLessonResponseDTOConverter.convert(mappingContext.create(lesson, LessonResponseDTO.class)))
                .toList();
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
                .subjects(subjectResponseDTOS)
                .lessons(lessonResponseDTOS)
                .level(levelsResponseDTO)
                .outlet(outletResponseDTO)
                .build();
    }
}
