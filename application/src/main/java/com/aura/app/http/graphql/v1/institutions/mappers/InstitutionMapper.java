package com.aura.app.http.graphql.v1.institutions.mappers;

import com.aura.app.codegen.types.CreateInstitutionInput;
import com.aura.app.codegen.types.InstitutionResponse;
import com.aura.core.entities.institutions.Institution;
import com.aura.details.tables.institutions.InstitutionTable;
import java.time.Instant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InstitutionMapper {
    InstitutionMapper INSTANCE = Mappers.getMapper(InstitutionMapper.class);

    Institution from(CreateInstitutionInput createInstitutionInput);

    InstitutionResponse from(Institution institution);

    InstitutionResponse from(InstitutionTable institutionTable);

    // MapStruct will use this method to convert Instant -> Long (epoch millis)
    default Long map(Instant instant) {
        return instant == null ? null : instant.toEpochMilli();
    }
}
