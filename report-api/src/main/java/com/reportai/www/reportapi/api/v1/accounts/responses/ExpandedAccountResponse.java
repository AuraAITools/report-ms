package com.reportai.www.reportapi.api.v1.accounts.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.reportai.www.reportapi.api.v1.students.responses.StudentResponseDTO;
import com.reportai.www.reportapi.entities.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpandedAccountResponse {
    @Builder
    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentClientSubaccountResponseDTO {
        @NotEmpty
        public Account.RELATIONSHIP relationship;

        @NotNull
        @Builder.Default
        public List<StudentResponseDTO> students = new ArrayList<>();

    }

    @Builder
    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EducatorClientSubaccountResponseDTO {

        @NotNull
        @Builder.Default
        public List<EducatorResponseDTO> educators = new ArrayList<>();
    }

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
    @Builder.Default
    public List<String> displayRoles = new ArrayList<>();

    @NotNull
    public ExpandedAccountResponse.StudentClientSubaccountResponseDTO studentClientSubaccount;

    @NotNull
    public ExpandedAccountResponse.EducatorClientSubaccountResponseDTO educatorClientSubaccount;

    @NotNull
    @Builder.Default
    public List<String> pendingAccountActions = new ArrayList<>();
}
