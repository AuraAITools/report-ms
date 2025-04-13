package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.levels.requests.CreateLevelsRequestDTO;
import com.reportai.www.reportapi.api.v1.levels.responses.ExpandedLevelsResponseDTO;
import com.reportai.www.reportapi.api.v1.levels.responses.LevelsResponseDTO;
import com.reportai.www.reportapi.entities.Level;
import java.util.UUID;
import java.util.stream.Collectors;

public class LevelMappers {
    private LevelMappers() {
    }

    public static Level convert(UUID id, CreateLevelsRequestDTO createLevelsRequestDTO) {
        return Level
                .builder()
                .name(createLevelsRequestDTO.getName())
                .tenantId(id.toString())
                .build();
    }

    public static LevelsResponseDTO convert(Level from) {
        return LevelsResponseDTO
                .builder()
                .id(from.getId().toString())
                .name(from.getName())
                .build();
    }

    public static ExpandedLevelsResponseDTO convertExpanded(Level from) {
        return ExpandedLevelsResponseDTO
                .builder()
                .id(from.getId().toString())
                .name(from.getName())
                .courses(from.getCourses().stream().map(CourseMappers::convert).collect(Collectors.toSet()))
                .students(from.getStudents().stream().map(StudentMappers::convert).collect(Collectors.toSet()))
                .educators(from.getLevelEducatorAttachments().stream().map(levelEducatorAttachment -> EducatorMappers.convert(levelEducatorAttachment.getEducator())).collect(Collectors.toSet()))
                .subjects(from.getSubjectLevelAttachments().stream().map(subjectLevelAttachment -> SubjectMappers.convert(subjectLevelAttachment.getSubject())).collect(Collectors.toSet()))
                .build();
    }
}
