package com.reportai.www.reportapi.api.v1.topics;

import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.services.topics.TopicsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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

    public TopicsController(TopicsService topicsService) {
        this.topicsService = topicsService;
    }

    @PostMapping("/institutions/{id}/topics")
    @PreAuthorize("hasRole(#id + '_institution-admin')")
    public ResponseEntity<Topic> createTopicInInstitution(@RequestBody Topic topic, @PathVariable UUID id) {
        Topic createdTopic = topicsService.createTopicForInstitution(topic, id);
        return new ResponseEntity<>(createdTopic, HttpStatus.CREATED);
    }

    @GetMapping("/institutions/{id}/topics")
    @PreAuthorize("hasRole(#id + '_institution-admin')")
    public ResponseEntity<List<Topic>> getAllTopicsInInstitution(@PathVariable UUID id) {
        List<Topic> topics = topicsService.getAllTopicsFromInstitution(id);
        return new ResponseEntity<>(topics, HttpStatus.OK);
    }
}
