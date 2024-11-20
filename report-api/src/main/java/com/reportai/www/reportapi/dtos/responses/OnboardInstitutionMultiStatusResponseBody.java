package com.reportai.www.reportapi.dtos.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class OnboardInstitutionMultiStatusResponseBody extends MultiStatusResponseBody<IndividualStatus> {
    private InstitutionResponseDto institution;
}
