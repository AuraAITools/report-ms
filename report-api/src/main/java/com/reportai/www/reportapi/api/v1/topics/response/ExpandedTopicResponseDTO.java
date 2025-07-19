package com.reportai.www.reportapi.api.v1.topics.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.api.v1.subjects.responses.SubjectResponseDTO;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExpandedTopicResponseDTO {
    @NotEmpty
    private String id;

    @NotEmpty
    private String name;

    @Builder.Default
    private List<SubjectResponseDTO> subjects = new ArrayList<>();
}
