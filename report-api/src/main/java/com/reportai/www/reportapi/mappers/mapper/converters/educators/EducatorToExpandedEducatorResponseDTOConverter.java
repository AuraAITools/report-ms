package com.reportai.www.reportapi.mappers.mapper.converters.educators;

import com.reportai.www.reportapi.api.v1.accounts.responses.AccountResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.ExpandedEducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.courses.responses.CourseResponseDTO;
import com.reportai.www.reportapi.api.v1.levels.responses.LevelsResponseDTO;
import com.reportai.www.reportapi.api.v1.outlets.responses.OutletResponseDTO;
import com.reportai.www.reportapi.api.v1.subjects.responses.SubjectResponseDTO;
import com.reportai.www.reportapi.entities.attachments.AccountEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.EducatorCourseAttachment;
import com.reportai.www.reportapi.entities.attachments.LevelEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.OutletEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.SubjectEducatorAttachment;
import com.reportai.www.reportapi.entities.educators.Educator;
import com.reportai.www.reportapi.mappers.mapper.ModelMapperConfigurer;
import com.reportai.www.reportapi.mappers.mapper.converters.courses.CourseToCourseResponseConverter;
import java.util.List;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EducatorToExpandedEducatorResponseDTOConverter implements Converter<Educator, ExpandedEducatorResponseDTO>, ModelMapperConfigurer {

    private final CourseToCourseResponseConverter courseToCourseResponseConverter;

    @Autowired
    public EducatorToExpandedEducatorResponseDTOConverter(CourseToCourseResponseConverter courseToCourseResponseConverter) {
        this.courseToCourseResponseConverter = courseToCourseResponseConverter;
    }

    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper.addConverter(this);
    }

    @Override
    public ExpandedEducatorResponseDTO convert(MappingContext<Educator, ExpandedEducatorResponseDTO> mappingContext) {
        Educator educator = mappingContext.getSource();
        List<AccountResponseDTO> accountResponseDTOS = educator
                .getAccountEducatorAttachments()
                .stream()
                .map(AccountEducatorAttachment::getAccount)
                .map(account -> mappingContext.getMappingEngine().map(mappingContext.create(account, AccountResponseDTO.class)))
                .toList();
        List<LevelsResponseDTO> levelsResponseDTO = educator
                .getLevelEducatorAttachments()
                .stream()
                .map(LevelEducatorAttachment::getLevel)
                .map(level -> mappingContext.getMappingEngine().map(mappingContext.create(level, LevelsResponseDTO.class)))
                .toList();
        List<SubjectResponseDTO> subjectResponseDTOS = educator
                .getSubjectEducatorAttachments()
                .stream()
                .map(SubjectEducatorAttachment::getSubject)
                .map(subject -> mappingContext.getMappingEngine().map(mappingContext.create(subject, SubjectResponseDTO.class)))
                .toList();
        List<OutletResponseDTO> outletResponseDTOS = educator
                .getOutletEducatorAttachments()
                .stream()
                .map(OutletEducatorAttachment::getOutlet)
                .map(outlet -> mappingContext.getMappingEngine().map(mappingContext.create(outlet, OutletResponseDTO.class)))
                .toList();

        List<CourseResponseDTO> courseResponseDTOS = educator
                .getEducatorCourseAttachments()
                .stream()
                .map(EducatorCourseAttachment::getCourse)
                .map(course -> courseToCourseResponseConverter.convert(mappingContext.create(course, CourseResponseDTO.class)))
                .toList();

        return ExpandedEducatorResponseDTO
                .builder()
                .id(educator.getId().toString())
                .name(educator.getName())
                .email(educator.getEmail())
                .dateOfBirth(educator.getDateOfBirth())
                .startDate(educator.getStartDate().toString())
                .employmentType(educator.getEmploymentType())
                .levels(levelsResponseDTO)
                .subjects(subjectResponseDTOS)
                .outlets(outletResponseDTOS)
                .accounts(accountResponseDTOS)
                .courses(courseResponseDTOS)
                .build();
    }
}
