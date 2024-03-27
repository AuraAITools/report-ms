package com.reportai.www.reportapi.dtos.responses.commons;

import com.reportai.www.reportapi.dtos.DTOSupport;
import com.reportai.www.reportapi.entities.Student;
import org.modelmapper.ModelMapper;

public class StudentDTO extends DTOSupport<Student> {
    public StudentDTO(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public Student toEntity() {
        return null;
    }
}
