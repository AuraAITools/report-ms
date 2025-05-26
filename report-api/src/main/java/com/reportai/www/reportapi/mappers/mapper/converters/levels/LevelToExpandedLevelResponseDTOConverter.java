package com.reportai.www.reportapi.mappers.mapper.converters.levels;

import com.reportai.www.reportapi.api.v1.accounts.responses.EducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CourseResponseDTO;
import com.reportai.www.reportapi.api.v1.levels.responses.ExpandedLevelsResponseDTO;
import com.reportai.www.reportapi.api.v1.students.responses.StudentResponseDTO;
import com.reportai.www.reportapi.api.v1.subjects.responses.SubjectResponseDTO;
import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.attachments.LevelEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.SubjectLevelAttachment;
import com.reportai.www.reportapi.mappers.mapper.ModelMapperConfigurer;
import com.reportai.www.reportapi.mappers.mapper.converters.students.StudentToStudentResponseDTOConverter;
import java.util.List;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LevelToExpandedLevelResponseDTOConverter implements Converter<Level, ExpandedLevelsResponseDTO>, ModelMapperConfigurer {
    private StudentToStudentResponseDTOConverter studentToStudentResponseDTOConverter;

    @Autowired
    public LevelToExpandedLevelResponseDTOConverter(StudentToStudentResponseDTOConverter studentToStudentResponseDTOConverter) {
        this.studentToStudentResponseDTOConverter = studentToStudentResponseDTOConverter;
    }

    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper.addConverter(this);
    }

    @Override
    public ExpandedLevelsResponseDTO convert(MappingContext<Level, ExpandedLevelsResponseDTO> mappingContext) {
        Level level = mappingContext.getSource();
        List<CourseResponseDTO> courseResponseDTOs = level
                .getCourses()
                .stream()
                .map(course -> mappingContext.getMappingEngine().map(mappingContext.create(course, CourseResponseDTO.class)))
                .toList();
        List<SubjectResponseDTO> subjectResponseDTOS = level
                .getSubjectLevelAttachments()
                .stream()
                .map(SubjectLevelAttachment::getSubject)
                .map(subject -> mappingContext.getMappingEngine().map(mappingContext.create(subject, SubjectResponseDTO.class)))
                .toList();
        List<StudentResponseDTO> studentResponseDTOS = level
                .getStudents()
                .stream()
                .map(student -> studentToStudentResponseDTOConverter.convert(mappingContext.create(student, StudentResponseDTO.class)))
                .toList();
        List<EducatorResponseDTO> educatorResponseDTOS = level
                .getLevelEducatorAttachments()
                .stream()
                .map(LevelEducatorAttachment::getEducator)
                .map(educator -> mappingContext.getMappingEngine().map(mappingContext.create(educator, EducatorResponseDTO.class)))
                .toList();
        return ExpandedLevelsResponseDTO
                .builder()
                .id(level.getId().toString())
                .name(level.getName())
                .courses(courseResponseDTOs)
                .students(studentResponseDTOS)
                .subjects(subjectResponseDTOS)
                .educators(educatorResponseDTOS)
                .build();
    }
}
