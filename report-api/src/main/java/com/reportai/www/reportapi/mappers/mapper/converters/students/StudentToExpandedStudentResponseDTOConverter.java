package com.reportai.www.reportapi.mappers.mapper.converters.students;

import com.reportai.www.reportapi.api.v1.accounts.responses.StudentClientAccountResponseDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CourseResponseDTO;
import com.reportai.www.reportapi.api.v1.levels.responses.LevelsResponseDTO;
import com.reportai.www.reportapi.api.v1.outlets.responses.OutletResponseDTO;
import com.reportai.www.reportapi.api.v1.students.responses.ExpandedStudentResponseDTO;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.attachments.AccountStudentAttachment;
import com.reportai.www.reportapi.entities.attachments.StudentCourseRegistration;
import com.reportai.www.reportapi.entities.attachments.StudentOutletRegistration;
import com.reportai.www.reportapi.mappers.mapper.ModelMapperConfigurer;
import com.reportai.www.reportapi.mappers.mapper.converters.courses.CourseToCourseResponseConverter;
import java.util.List;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentToExpandedStudentResponseDTOConverter implements Converter<Student, ExpandedStudentResponseDTO>, ModelMapperConfigurer {
    private CourseToCourseResponseConverter courseToCourseResponseConverter;

    @Autowired
    public StudentToExpandedStudentResponseDTOConverter(CourseToCourseResponseConverter courseToCourseResponseConverter) {
        this.courseToCourseResponseConverter = courseToCourseResponseConverter;
    }

    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper.addConverter(this);
    }

    @Override
    public ExpandedStudentResponseDTO convert(MappingContext<Student, ExpandedStudentResponseDTO> mappingContext) {
        Student student = mappingContext.getSource();
        LevelsResponseDTO levelsResponseDTO = mappingContext.getMappingEngine().map(mappingContext.create(student.getLevel(), LevelsResponseDTO.class));
        List<StudentClientAccountResponseDTO> studentClientAccountResponseDTOs = student
                .getAccountStudentAttachments()
                .stream()
                .map(AccountStudentAttachment::getAccount)
                .map(account -> mappingContext.getMappingEngine().map(mappingContext.create(account, StudentClientAccountResponseDTO.class)))
                .toList();
        List<OutletResponseDTO> outletResponseDTOS = student
                .getStudentOutletRegistrations()
                .stream()
                .map(StudentOutletRegistration::getOutlet)
                .map(outlet -> mappingContext.getMappingEngine().map(mappingContext.create(outlet, OutletResponseDTO.class)))
                .toList();

        List<CourseResponseDTO> courseResponseDTOS = student
                .getStudentCourseRegistrations()
                .stream()
                .map(StudentCourseRegistration::getCourse)
                .map(course -> courseToCourseResponseConverter.convert(mappingContext.create(course, CourseResponseDTO.class)))
                .toList();
        return ExpandedStudentResponseDTO
                .builder()
                .id(mappingContext.getSource().getId().toString())
                .name(mappingContext.getSource().getName())
                .email(mappingContext.getSource().getEmail())
                .dateOfBirth(mappingContext.getSource().getDateOfBirth())
                .school(mappingContext.getSource().getSchool().getName())
                .level(levelsResponseDTO)
                .accounts(studentClientAccountResponseDTOs)
                .outlets(outletResponseDTOS)
                .courses(courseResponseDTOS)
                .build();
    }
}
