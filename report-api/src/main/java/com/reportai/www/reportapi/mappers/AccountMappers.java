package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.accounts.requests.CreateBlankAccountRequestDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateInstitutionAdminAccountRequestDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateStudentClientRequestDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.AccountResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.ExpandedAccountResponse;
import com.reportai.www.reportapi.api.v1.accounts.responses.StudentClientResponseDTO;
import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.personas.EducatorClientPersona;
import com.reportai.www.reportapi.entities.personas.InstitutionAdminPersona;
import com.reportai.www.reportapi.entities.personas.OutletAdminPersona;
import com.reportai.www.reportapi.entities.personas.Persona;
import com.reportai.www.reportapi.entities.personas.StudentClientPersona;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

public class AccountMappers {
    public static UserRepresentation convert(Account from, Map<String, List<String>> attributes) {
        // default credentials
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType("password");
        credentialRepresentation.setValue("password");
        credentialRepresentation.setTemporary(true);

        UserRepresentation partiallyFilledUser = new UserRepresentation();
        partiallyFilledUser.setEmail(from.getEmail());
        partiallyFilledUser.setFirstName(from.getFirstName());
        partiallyFilledUser.setLastName(from.getLastName());
        partiallyFilledUser.setAttributes(attributes);
        partiallyFilledUser.setEnabled(true);
        partiallyFilledUser.setEmailVerified(false);
        partiallyFilledUser.setRequiredActions(List.of("VERIFY_EMAIL", "UPDATE_PASSWORD"));
        partiallyFilledUser.setCredentials(List.of(credentialRepresentation));
        return partiallyFilledUser;
    }

    public static Account convert(CreateBlankAccountRequestDTO from, String tenantId) {
        return Account.builder().
                firstName(from.getFirstName())
                .lastName(from.getLastName())
                .email(from.getEmail())
                .contact(from.getContact())
                .tenantId(tenantId)
                .build();
    }

    public static Account convert(CreateBlankAccountRequestDTO from) {
        return Account.builder().
                firstName(from.getFirstName())
                .lastName(from.getLastName())
                .email(from.getEmail())
                .contact(from.getContact())
                .build();
    }

    public static AccountResponseDTO convert(Account from) {
        return AccountResponseDTO
                .builder()
                .id(from.getId().toString())
                .firstName(from.getFirstName())
                .lastName(from.getLastName())
                .email(from.getEmail())
                .contact(from.getContact())
                .build();

    }

    public static Account convert(CreateInstitutionAdminAccountRequestDTO from) {
        return Account.builder().
                firstName(from.getFirstName())
                .lastName(from.getLastName())
                .email(from.getEmail())
                .contact(from.getContact())
                .build();
    }

    public static Account convert(CreateStudentClientRequestDTO from, String tenantId) {
        return Account.builder().
                firstName(from.getFirstName())
                .lastName(from.getLastName())
                .email(from.getEmail())
                .contact(from.getContact())
                .tenantId(tenantId)
                .build();
    }

    public static StudentClientResponseDTO convert(Account from, StudentClientPersona.RELATIONSHIP relationship) {
        return StudentClientResponseDTO
                .builder()
                .id(from.getId().toString())
                .firstName(from.getFirstName())
                .lastName(from.getLastName())
                .email(from.getEmail())
                .contact(from.getContact())
                .relationship(relationship)
                .build();
    }

    public static ExpandedAccountResponse convertExpanded(Account account) {
        return ExpandedAccountResponse
                .builder()
                .contact(account.getContact())
                .email(account.getEmail())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .id(account.getId().toString())
                .personas(account.getPersonas() == null ? Collections.emptyList() :
                        account
                                .getPersonas()
                                .stream()
                                .map(AccountMappers::convert)
                                .toList())
                .pendingAccountActions(Collections.emptyList())
                .build();
    }

    private static ExpandedAccountResponse.PersonaResponseDTO convert(Persona persona) {
        return ExpandedAccountResponse.PersonaResponseDTO
                .builder()
                .displayRoles(extractRolesFromPersona(persona))
                .id(persona.getId().toString())
                .build();
    }

    public static List<String> extractRolesFromPersona(Persona persona) {
        if (persona instanceof StudentClientPersona) {
            return List.of("student");
        }

        if (persona instanceof EducatorClientPersona) {
            return List.of("educator");
        }

        if (persona instanceof OutletAdminPersona) {
            return ((OutletAdminPersona) persona)
                    .getOutlets()
                    .stream()
                    .map(outlet -> String.format("%s outlet-admin", outlet.getName()))
                    .toList();
        }

        if (persona instanceof InstitutionAdminPersona) {
            return List.of("institution-admin");
        }

        return List.of("none");
    }


}
