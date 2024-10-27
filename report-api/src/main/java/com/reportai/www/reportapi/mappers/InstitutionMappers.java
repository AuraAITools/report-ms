package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.dtos.requests.CreateInstitutionDTO;
import com.reportai.www.reportapi.entities.Institution;

public class InstitutionMappers {
    public static Institution convert(CreateInstitutionDTO from) {
        return Institution
                .builder()
                .name(from.getName())
                .email(from.getEmail())
                .build();
    }
}
