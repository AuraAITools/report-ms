package com.reportai.www.reportapi.api.v1.analytics;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.analytics.requests.CreateLessonHomeworkCompletionRequest;
import com.reportai.www.reportapi.entities.analytics.LessonHomeworkCompletion;
import com.reportai.www.reportapi.mappers.LessonHomeworkCompletionMapper;
import com.reportai.www.reportapi.services.analytics.AnalyticsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Analytics APIs", description = "APIs for writing and reading lesson analytics")
@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class LessonAnalyticsController {

    private final AnalyticsService analyticsService;

    @Autowired
    public LessonAnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    /**
     * Creates a LessonHomeworkCompletion with ratings of a lesson for a student
     *
     * @param lessonId
     * @param studentId
     * @param createLessonHomeworkCompletionRequest
     * @return
     */
    @PostMapping("/institutions/{id}/outlets/{outlet_id}/lessons/{lesson_id}/students/{student_id}/homework-completion")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::lessons::homework-completion:create'")
    @Transactional
    public ResponseEntity<Void> createLessonHomeworkCompletion(@PathVariable(name = "id") UUID id, @PathVariable(name = "outlet_id") UUID outletId, @PathVariable(name = "lesson_id") UUID lessonId, @PathVariable(name = "student_id") UUID studentId, @RequestBody @Valid CreateLessonHomeworkCompletionRequest createLessonHomeworkCompletionRequest) {

        LessonHomeworkCompletion lessonHomeworkCompletion = analyticsService.createLessonHomeworkCompletion(studentId, lessonId, LessonHomeworkCompletionMapper.convert(createLessonHomeworkCompletionRequest));
        return ResponseEntity.ok().build();
    }
}
