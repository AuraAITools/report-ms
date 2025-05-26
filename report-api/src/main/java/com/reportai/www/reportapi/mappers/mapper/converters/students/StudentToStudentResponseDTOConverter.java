package com.reportai.www.reportapi.mappers.mapper.converters.students;

import com.reportai.www.reportapi.api.v1.students.responses.StudentResponseDTO;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.mappers.mapper.ModelMapperConfigurer;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class StudentToStudentResponseDTOConverter implements Converter<Student, StudentResponseDTO>, ModelMapperConfigurer {
    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper.addConverter(this);
    }

    @Override
    public StudentResponseDTO convert(MappingContext<Student, StudentResponseDTO> mappingContext) {
        return StudentResponseDTO
                .builder()
                .id(mappingContext.getSource().getId().toString())
                .name(mappingContext.getSource().getName())
                .email(mappingContext.getSource().getEmail())
                .dateOfBirth(mappingContext.getSource().getDateOfBirth())
                .school(mappingContext.getSource().getSchool().getName())
                .build();
    }
}
