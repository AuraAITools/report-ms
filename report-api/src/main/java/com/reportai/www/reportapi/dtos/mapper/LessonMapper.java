package com.reportai.www.reportapi.dtos.mapper;

import com.reportai.www.reportapi.dtos.responses.LessonDto;
import com.reportai.www.reportapi.entities.Lesson;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    LessonDto toDto(Lesson lesson);
    List<LessonDto> toDtoList(List<Lesson> lessons);
}

