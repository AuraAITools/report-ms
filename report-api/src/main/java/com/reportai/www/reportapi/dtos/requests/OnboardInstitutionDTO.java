package com.reportai.www.reportapi.dtos.requests;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Builder
public class OnboardInstitutionDTO {

    @Valid
    @NotNull(message = "institution field is required")
    private CreateInstitutionDTO institution;

    @Valid
    @NotNull(message = "admin_accounts field is required")
    @Size(min = 1, max = 5, message = "specify 1 to 5 admin accounts")
    private List<CreateAdminAccountDTO> adminAccounts;

}

