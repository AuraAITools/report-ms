package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.courses.requests.CreateLessonGenerationTemplateRequestDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CreateLessonGenerationTemplateResponseDTO;
import com.reportai.www.reportapi.entities.LessonGenerationTemplate;

public class LessonGenerationTemplateMappers {
    private LessonGenerationTemplateMappers() {
    }

    public static LessonGenerationTemplate convert(CreateLessonGenerationTemplateRequestDTO createLessonGenerationTemplateRequestDTO, String tenantId) {
        return LessonGenerationTemplate
                .builder()
                .tenantId(tenantId)
                .startTime(createLessonGenerationTemplateRequestDTO.getStartTime())
                .endTime(createLessonGenerationTemplateRequestDTO.getEndTime())
                .dayOfWeek(createLessonGenerationTemplateRequestDTO.getDayOfWeek())
                .weekNumber(createLessonGenerationTemplateRequestDTO.getWeekNumber())
                .build();
    }

    public static CreateLessonGenerationTemplateResponseDTO convert(LessonGenerationTemplate lessonGenerationTemplate) {
        return CreateLessonGenerationTemplateResponseDTO
                .builder()
                .id(lessonGenerationTemplate.getId().toString())
                .startTime(lessonGenerationTemplate.getStartTime())
                .endTime(lessonGenerationTemplate.getEndTime())
                .dayOfWeek(lessonGenerationTemplate.getDayOfWeek())
                .weekNumber(lessonGenerationTemplate.getWeekNumber())
                .build();
    }
}
