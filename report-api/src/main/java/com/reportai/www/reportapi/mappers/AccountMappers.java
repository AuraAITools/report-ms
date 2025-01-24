package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.accounts.requests.CreateUserDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateAccountResponseDTO;
import com.reportai.www.reportapi.api.v1.outlets.requests.CreateInstitutionAdminAccountDTO;
import com.reportai.www.reportapi.entities.Account;
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

    public static Account convert(CreateUserDTO from, String tenantId) {
        return Account.builder().
                firstName(from.getFirstName())
                .lastName(from.getLastName())
                .email(from.getEmail())
                .relationship(from.getRelationship())
                .contact(from.getContact())
                .tenantId(tenantId)
                .build();
    }

    public static Account convert(CreateUserDTO from) {
        return Account.builder().
                firstName(from.getFirstName())
                .lastName(from.getLastName())
                .email(from.getEmail())
                .relationship(from.getRelationship())
                .contact(from.getContact())
                .build();
    }

    public static CreateAccountResponseDTO convert(Account from) {
        return CreateAccountResponseDTO
                .builder()
                .id(from.getId().toString())
                .relationship(from.getRelationship())
                .firstName(from.getFirstName())
                .lastName(from.getLastName())
                .email(from.getEmail())
                .contact(from.getContact())
                .build();

    }

    public static Account convert(CreateInstitutionAdminAccountDTO from) {
        return Account.builder().
                firstName(from.getFirstName())
                .lastName(from.getLastName())
                .email(from.getEmail())
                .contact(from.getContact())
                .build();
    }

}
