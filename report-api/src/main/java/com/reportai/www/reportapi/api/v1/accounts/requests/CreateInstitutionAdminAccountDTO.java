<<<<<<<< HEAD:report-api/src/main/java/com/reportai/www/reportapi/api/v1/accounts/requests/CreateInstitutionAdminAccountDTO.java
package com.reportai.www.reportapi.api.v1.accounts.requests;
========
package com.reportai.www.reportapi.api.v1.outlets.requests;
>>>>>>>> b5a20925c51209be1d8c827377bcbccd371c82b0:report-api/src/main/java/com/reportai/www/reportapi/api/v1/outlets/requests/CreateInstitutionAdminAccountDTO.java

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class CreateInstitutionAdminAccountDTO {

    @Email
    @NotEmpty
    public String email;

    @NotEmpty
    public String firstName;

    @NotEmpty
    public String lastName;

    @NotEmpty
    public String contact;


}
