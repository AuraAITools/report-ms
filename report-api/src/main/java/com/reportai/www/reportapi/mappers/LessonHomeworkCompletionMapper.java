package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.analytics.requests.CreateLessonHomeworkCompletionRequest;
import com.reportai.www.reportapi.entities.lessons.LessonHomeworkCompletion;

public class LessonHomeworkCompletionMapper {
    private LessonHomeworkCompletionMapper() {
    }

    public static LessonHomeworkCompletion convert(CreateLessonHomeworkCompletionRequest createLessonHomeworkCompletionRequest) {
        return LessonHomeworkCompletion
                .builder()
                .completion(createLessonHomeworkCompletionRequest.getRating())
                .build();
    }
}
