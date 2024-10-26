package com.reportai.www.reportapi.dtos.mapper;

import com.reportai.www.reportapi.dtos.responses.SubjectDto;
import com.reportai.www.reportapi.entities.Subject;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = LessonMapper.class)
public interface SubjectMapper {
    SubjectDto toDto(Subject subject);
    List<SubjectDto> toDtoList(List<Subject> subjects);
}