package com.reportai.www.reportapi.api.v1.analytics.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateLessonHomeworkCompletionRequest {
    @NotNull
    @Min(value = 0, message = "Minimum value of 0")
    @Max(value = 5, message = "Maximum value of 5")
    private int rating;
}
