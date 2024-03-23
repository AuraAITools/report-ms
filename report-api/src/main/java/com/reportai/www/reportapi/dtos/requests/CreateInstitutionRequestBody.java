package com.reportai.www.reportapi.dtos.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateInstitutionRequestBody {
    private CreateUserRequestBody user;
}
