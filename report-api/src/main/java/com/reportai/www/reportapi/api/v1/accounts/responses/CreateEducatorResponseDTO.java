package com.reportai.www.reportapi.api.v1.accounts.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.api.v1.levels.responses.CreateLevelsResponseDTO;
import com.reportai.www.reportapi.api.v1.outlets.responses.OutletResponseDto;
import com.reportai.www.reportapi.api.v1.subjects.responses.SubjectResponseDTO;
import com.reportai.www.reportapi.entities.Educator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Builder
public class CreateEducatorResponseDTO {

    @NotEmpty
    private String id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String email;

    @NotEmpty
    private List<CreateLevelsResponseDTO> levels;

    @NotEmpty
    private List<SubjectResponseDTO> subjects;

    @NotEmpty
    private List<OutletResponseDto> outlets;

    @NotEmpty
    private String startDate;

    @NotNull
    private Educator.EMPLOYMENT_TYPE employmentType;

}
