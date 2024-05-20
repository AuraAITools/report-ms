package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Timeline;
import com.reportai.www.reportapi.exceptions.NotFoundException;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.TimelineRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TimelineService {
    private final InstitutionRepository institutionRepository;
    private final TimelineRepository timelineRepository;

    public TimelineService(InstitutionRepository institutionRepository, TimelineRepository timelineRepository) {
        this.institutionRepository = institutionRepository;
        this.timelineRepository = timelineRepository;
    }

    @Transactional
    public Timeline createTimeline(Timeline timeline, UUID institutionId) {
        Institution institution = institutionRepository
                .findById(institutionId)
                .orElseThrow(()-> new NotFoundException("Institution not found"));

        Timeline savedTimeline = timelineRepository.save(timeline);

        institution.getTimelines().add(savedTimeline);
        institutionRepository.save(institution);
        return timeline;
    }

    public Timeline findTimelineById(UUID timelineId) {
        return timelineRepository.findById(timelineId).orElseThrow(
                ()-> new NotFoundException("Timeline not found")
        );
    }

    public List<Timeline> getTimelinesByInstitution(UUID institutionId) {
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(
                ()-> new NotFoundException("Institution not found")
        );

        return institution.getTimelines();
    }
}
