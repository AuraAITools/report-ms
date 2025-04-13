package com.reportai.www.reportapi.api.v1.accounts.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Builder
public class ExpandedAccountResponse {
    @NotEmpty
    public String id;

    @Email
    @NotEmpty
    public String email;

    @NotEmpty
    public String firstName;

    @NotEmpty
    public String lastName;

    @NotEmpty
    public String contact;

    @NotNull
    public List<PersonaResponseDTO> personas = new ArrayList<>();

    @Builder
    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PersonaResponseDTO {
        @NotEmpty
        public String id;

        @NotNull
        public List<String> displayRoles = new ArrayList<>();
    }

    @NotNull
    public List<String> pendingAccountActions = new ArrayList<>();
}
