package com.reportai.www.reportapi.mappers.mapper.converters.lessons;

import com.reportai.www.reportapi.api.v1.lessons.responses.LessonResponseDTO;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import com.reportai.www.reportapi.entities.views.LessonView;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.mappers.mapper.ModelMapperConfigurer;
import com.reportai.www.reportapi.repositories.views.LessonViewRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessonToLessonResponseDTOConverter implements Converter<Lesson, LessonResponseDTO>, ModelMapperConfigurer {

    private final LessonViewRepository lessonViewRepository;

    @Autowired
    public LessonToLessonResponseDTOConverter(
            LessonViewRepository lessonViewRepository) {
        this.lessonViewRepository = lessonViewRepository;
    }

    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper.addConverter(this);
    }

    @Override
    public LessonResponseDTO convert(MappingContext<Lesson, LessonResponseDTO> mappingContext) {
        Lesson lesson = mappingContext.getSource();
        LessonView lessonView = lessonViewRepository.findById(lesson.getId()).orElseThrow(() -> new ResourceNotFoundException("no lesson view found during mapping"));
        return LessonResponseDTO
                .builder()
                .id(lessonView.getId().toString())
                .name(lessonView.getName())
                .description(lessonView.getDescription())
                .lessonStatus(lessonView.getLessonStatus())
                .lessonReviewStatus(lessonView.getLessonReviewStatus())
                .lessonPlanStatus(lessonView.getLessonPlanStatus())
                .lessonStartTimestamptz(lessonView.getLessonStartTimestamptz())
                .lessonEndTimestamptz(lessonView.getLessonEndTimestamptz())
                .build();
    }
}
