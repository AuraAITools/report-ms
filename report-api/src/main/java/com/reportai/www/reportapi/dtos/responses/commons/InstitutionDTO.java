package com.reportai.www.reportapi.dtos.responses.commons;

import com.reportai.www.reportapi.dtos.DTOSupport;
import com.reportai.www.reportapi.entities.Institution;
import org.modelmapper.ModelMapper;

public class InstitutionDTO extends DTOSupport<Institution> {

    public InstitutionDTO(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public Institution toEntity() {
        return null;
    }
}
