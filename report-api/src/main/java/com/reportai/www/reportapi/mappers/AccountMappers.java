package com.reportai.www.reportapi.mappers;

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

}
