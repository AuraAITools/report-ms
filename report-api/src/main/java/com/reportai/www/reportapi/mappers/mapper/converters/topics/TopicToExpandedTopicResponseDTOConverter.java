package com.reportai.www.reportapi.mappers.mapper.converters.topics;

import com.reportai.www.reportapi.api.v1.subjects.responses.SubjectResponseDTO;
import com.reportai.www.reportapi.api.v1.topics.response.ExpandedTopicResponseDTO;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.mappers.mapper.ModelMapperConfigurer;
import java.util.Collections;
import java.util.List;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class TopicToExpandedTopicResponseDTOConverter implements ModelMapperConfigurer, Converter<Topic, ExpandedTopicResponseDTO> {
    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper.addConverter(this);
    }

    @Override
    public ExpandedTopicResponseDTO convert(MappingContext<Topic, ExpandedTopicResponseDTO> mappingContext) {
        Topic topic = mappingContext.getSource();
        List<SubjectResponseDTO> subjectResponseDTOS = topic.getTopicSubjectAttachments().isEmpty() ? Collections.emptyList() : topic
                .getTopicSubjectAttachments()
                .stream()
                .map(attachment -> {
                    Subject subject = attachment.getSubject();
                    return mappingContext.getMappingEngine().map(mappingContext.create(subject, SubjectResponseDTO.class));
                })
                .toList();
        return ExpandedTopicResponseDTO
                .builder()
                .id(topic.getId().toString())
                .name(topic.getName())
                .subjects(subjectResponseDTOS)
                .build();
    }
}
