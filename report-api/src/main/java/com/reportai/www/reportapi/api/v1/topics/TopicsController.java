package com.reportai.www.reportapi.api.v1.topics;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.topics.requests.CreateTopicRequestDTO;
import com.reportai.www.reportapi.api.v1.topics.response.ExpandedTopicResponseDTO;
import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.services.topics.TopicsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Topic APIs", description = "APIs for managing a Topic resource")
@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class TopicsController {

    private final TopicsService topicsService;

    private final ModelMapper modelMapper;

    @Autowired
    public TopicsController(TopicsService topicsService, ModelMapper modelMapper) {
        this.topicsService = topicsService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/institutions/{id}/topics")
    @HasResourcePermission(permission = "'institutions::' + #id + '::topics:create'")
    @Transactional
    public ResponseEntity<ExpandedTopicResponseDTO> createTopicInInstitution(@RequestBody CreateTopicRequestDTO createTopicRequestDTO, @PathVariable UUID id) {
        Topic topic = modelMapper.map(createTopicRequestDTO, Topic.class);
        topicsService.create(topic);
        if (!createTopicRequestDTO.getSubjectIds().isEmpty()) {
            createTopicRequestDTO.getSubjectIds()
                    .forEach(subjectId -> topicsService.attach(topic.getId(), subjectId));
        }
        return new ResponseEntity<>(modelMapper.map(topic, ExpandedTopicResponseDTO.class), HttpStatus.CREATED);
    }

    @GetMapping("/institutions/{id}/topics/expand")
    @HasResourcePermission(permission = "'institutions::' + #id + '::topics:read'")
    public ResponseEntity<List<ExpandedTopicResponseDTO>> getAllExpandedTopicsInInstitution(@PathVariable UUID id) {
        List<Topic> topics = topicsService.getAllTopics();
        List<ExpandedTopicResponseDTO> expandedTopicResponseDTOS = topics.stream()
                .map(topic -> modelMapper.map(topic, ExpandedTopicResponseDTO.class))
                .toList();
        return new ResponseEntity<>(expandedTopicResponseDTOS, HttpStatus.OK);
    }

    @PatchMapping("/institutions/{id}/subjects/{subject_id}/topics/{topic_id}")
    @HasResourcePermission(permission = "'institutions::' + #id + '::subjects::topics:attach'")
    @Transactional
    public ResponseEntity<Void> linkTopicToSubject(@PathVariable UUID id, @PathVariable(name = "subject_id") UUID subjectId, @PathVariable(name = "topic_id") UUID topicId) {
        topicsService.attach(topicId, subjectId);
        return ResponseEntity.ok().build();
    }
}
