package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.accounts.requests.CreateStudentClientRequestDTO;
import com.reportai.www.reportapi.entities.personas.StudentClientPersona;
import java.util.UUID;

public class ClientMappers {
    private ClientMappers() {
    }

    public static StudentClientPersona convert(CreateStudentClientRequestDTO createStudentClientRequestDTO, UUID id) {
        return StudentClientPersona
                .builder()
                .relationship(createStudentClientRequestDTO.getRelationship())
                .tenantId(id.toString())
                .build();
    }
}
