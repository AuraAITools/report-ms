package com.reportai.www.reportapi.mappers.mapper.converters.outlets;

import com.reportai.www.reportapi.api.v1.accounts.responses.EducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CourseResponseDTO;
import com.reportai.www.reportapi.api.v1.outlets.responses.ExpandedOutletsResponseDTO;
import com.reportai.www.reportapi.api.v1.students.responses.StudentResponseDTO;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.attachments.OutletEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.StudentOutletRegistration;
import com.reportai.www.reportapi.mappers.mapper.ModelMapperConfigurer;
import com.reportai.www.reportapi.mappers.mapper.converters.students.StudentToStudentResponseDTOConverter;
import java.util.List;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OutletToExpandedOutletResponseDTO implements Converter<Outlet, ExpandedOutletsResponseDTO>, ModelMapperConfigurer {
    private StudentToStudentResponseDTOConverter studentToStudentResponseDTOConverter;

    @Autowired
    public OutletToExpandedOutletResponseDTO(StudentToStudentResponseDTOConverter studentToStudentResponseDTOConverter) {
        this.studentToStudentResponseDTOConverter = studentToStudentResponseDTOConverter;
    }

    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper.addConverter(this);
    }

    @Override
    public ExpandedOutletsResponseDTO convert(MappingContext<Outlet, ExpandedOutletsResponseDTO> mappingContext) {
        Outlet outlet = mappingContext.getSource();
        List<CourseResponseDTO> courseResponseDTOs = outlet
                .getCourses()
                .stream()
                .map(course -> mappingContext.getMappingEngine().map(mappingContext.create(course, CourseResponseDTO.class)))
                .toList();
        List<StudentResponseDTO> studentResponseDTOS = outlet
                .getStudentOutletRegistrations()
                .stream()
                .map(StudentOutletRegistration::getStudent)
                .map(student -> studentToStudentResponseDTOConverter.convert(mappingContext.create(student, StudentResponseDTO.class)))
                .toList();
        List<EducatorResponseDTO> educatorResponseDTOS = outlet
                .getOutletEducatorAttachments()
                .stream()
                .map(OutletEducatorAttachment::getEducator)
                .map(educator -> mappingContext.getMappingEngine().map(mappingContext.create(educator, EducatorResponseDTO.class)))
                .toList();
        return ExpandedOutletsResponseDTO
                .builder()
                .id(outlet.getId().toString())
                .address(outlet.getAddress())
                .contactNumber(outlet.getContactNumber())
                .postalCode(outlet.getPostalCode())
                .name(outlet.getName())
                .email(outlet.getEmail())
                .description(outlet.getDescription())
                .educators(educatorResponseDTOS)
                .students(studentResponseDTOS)
                .courses(courseResponseDTOs)
                .build();
    }
}
