package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.courses.requests.CreateLessonGenerationTemplateDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CreateLessonGenerationTemplateResponse;
import com.reportai.www.reportapi.entities.LessonGenerationTemplate;

public class LessonGenerationTemplateMappers {
    private LessonGenerationTemplateMappers() {
    }

    public static LessonGenerationTemplate convert(CreateLessonGenerationTemplateDTO createLessonGenerationTemplateDTO, String tenantId) {
        return LessonGenerationTemplate
                .builder()
                .tenantId(tenantId)
                .startTime(createLessonGenerationTemplateDTO.getStartTime())
                .endTime(createLessonGenerationTemplateDTO.getEndTime())
                .dayOfWeek(createLessonGenerationTemplateDTO.getDayOfWeek())
                .weekNumber(createLessonGenerationTemplateDTO.getWeekNumber())
                .build();
    }

    public static CreateLessonGenerationTemplateResponse convert(LessonGenerationTemplate lessonGenerationTemplate) {
        return CreateLessonGenerationTemplateResponse
                .builder()
                .id(lessonGenerationTemplate.getId().toString())
                .startTime(lessonGenerationTemplate.getStartTime())
                .endTime(lessonGenerationTemplate.getEndTime())
                .dayOfWeek(lessonGenerationTemplate.getDayOfWeek())
                .weekNumber(lessonGenerationTemplate.getWeekNumber())
                .build();
    }
}
