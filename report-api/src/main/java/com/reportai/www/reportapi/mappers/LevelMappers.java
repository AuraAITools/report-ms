package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.levels.requests.CreateLevelsRequestDTO;
import com.reportai.www.reportapi.api.v1.levels.responses.CreateLevelsResponseDTO;
import com.reportai.www.reportapi.api.v1.levels.responses.ExpandedLevelsResponseDTO;
import com.reportai.www.reportapi.entities.Level;
import java.util.UUID;

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

    public static CreateLevelsResponseDTO convert(Level from) {
        return CreateLevelsResponseDTO
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
                .courses(from.getCourses().stream().map(CourseMappers::convert).toList())
                .students(from.getStudents().stream().map(StudentMappers::convert).toList())
                .educators(from.getEducators().stream().map(EducatorMappers::convert).toList())
                .subjects(from.getSubjects().stream().map(SubjectMappers::convert).toList())
                .build();
    }
}
