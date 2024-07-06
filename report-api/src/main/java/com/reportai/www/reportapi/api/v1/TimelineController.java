package com.reportai.www.reportapi.api.v1;

import com.reportai.www.reportapi.entities.Timeline;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.TimelineRepository;
import com.reportai.www.reportapi.services.TimelineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping("v1")
@RestController
@Slf4j
public class TimelineController {


    TimelineService timelineService;

    public TimelineController(TimelineService timelineService) {
        this.timelineService = timelineService;
    }

    @PostMapping("/institutions/{institution_id}/timeline")
    public ResponseEntity<Timeline> createTimeline(@RequestBody Timeline timeline, @PathVariable(name = "institution_id")UUID institutionId) {
        Timeline createdTimeline = timelineService.createTimeline(timeline,institutionId);
        return new ResponseEntity<>(createdTimeline, HttpStatus.CREATED);
    }

    @GetMapping("/institutions/{institution_id}/timelines")
    public ResponseEntity<List<Timeline>> getTimelinesByInstitution(@PathVariable(name = "institution_id") UUID institutionId) {
        List<Timeline> timelines =timelineService.getTimelinesByInstitution(institutionId);
        return new ResponseEntity<>(timelines, HttpStatus.OK);
    }


    @GetMapping("/timelines/{timeline_id}")
    public ResponseEntity<Timeline> findTimelineById(@PathVariable(name = "timeline_id") UUID timelineId) {
        Timeline timeline = timelineService.findTimelineById(timelineId);
        return new ResponseEntity<>(timeline, HttpStatus.OK);
    }

}
