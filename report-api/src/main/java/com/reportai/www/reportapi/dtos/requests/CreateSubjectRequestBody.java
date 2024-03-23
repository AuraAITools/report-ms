package com.reportai.www.reportapi.dtos.requests;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class CreateSubjectRequestBody {
    @JsonProperty("institution_id")
    private UUID institutionId;
    private String name;
}
