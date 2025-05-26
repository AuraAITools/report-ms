package com.reportai.www.reportapi.api.v1.topics;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.topics.requests.CreateTopicRequestDTO;
import com.reportai.www.reportapi.api.v1.topics.response.TopicResponseDTO;
import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.services.topics.TopicsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper = new ModelMapper();

    public TopicsController(TopicsService topicsService) {
        this.topicsService = topicsService;
    }

    @PostMapping("/institutions/{id}/topics")
    @HasResourcePermission(permission = "'institutions::' + #id + '::topics:create'")
    public ResponseEntity<TopicResponseDTO> createTopicInInstitution(@RequestBody CreateTopicRequestDTO topic, @PathVariable UUID id) {
        Topic newTopic = new Topic();
        modelMapper.map(topic, newTopic);
        Topic createdTopic = topicsService.create(newTopic);
        TopicResponseDTO topicResponseDTO = new TopicResponseDTO();
        modelMapper.map(createdTopic, topicResponseDTO);
        return new ResponseEntity<>(topicResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/institutions/{id}/topics")
    @HasResourcePermission(permission = "'institutions::' + #id + '::topics:read'")
    public ResponseEntity<List<Topic>> getAllTopicsInInstitution(@PathVariable UUID id) {
        Collection<Topic> topics = topicsService.getAllTopicsFromInstitution();
        return new ResponseEntity<>(topics.stream().toList(), HttpStatus.OK);
    }

    @PatchMapping("/institutions/{id}/subjects/{subject_id}/topics/{topic_id}")
    @HasResourcePermission(permission = "'institutions::' + #id + '::subjects::topics:attach'")
    public ResponseEntity<Void> linkTopicToSubject(@PathVariable UUID id, @PathVariable(name = "subject_id") UUID subjectId, @PathVariable(name = "topic_id") UUID topicId) {
        topicsService.attach(topicId, subjectId);
        return ResponseEntity.ok().build();
    }
}
