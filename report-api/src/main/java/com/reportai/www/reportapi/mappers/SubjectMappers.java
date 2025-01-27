package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.subjects.requests.CreateSubjectDTO;
import com.reportai.www.reportapi.api.v1.subjects.responses.SubjectResponseDTO;
import com.reportai.www.reportapi.entities.Subject;
import java.util.UUID;

public class SubjectMappers {
    private SubjectMappers() {
    }

    public static Subject convert(CreateSubjectDTO createSubjectDTO, UUID id) {
        return Subject
                .builder()
                .name(createSubjectDTO.getName())
                .tenantId(id.toString())
                .build();
    }

    public static SubjectResponseDTO convert(Subject subject) {
        return SubjectResponseDTO
                .builder()
                .id(subject.getId().toString())
                .name(subject.getName())
                .build();
    }
}
