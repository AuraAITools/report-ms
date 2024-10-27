package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.dtos.requests.CreateAdminAccountDTO;
import com.reportai.www.reportapi.entities.Account;
import org.keycloak.representations.idm.UserRepresentation;

public class AccountMappers {
    public static UserRepresentation convert(Account from) {
        UserRepresentation partiallyFilledUser = new UserRepresentation();
        partiallyFilledUser.setEmail(from.getEmail());
        partiallyFilledUser.setFirstName(from.getFirstName());
        partiallyFilledUser.setLastName(from.getLastName());
        return partiallyFilledUser;
    }

    public static Account convert(CreateAdminAccountDTO from) {
        return Account
                .builder()
                .firstName(from.getFirstName())
                .lastName(from.getLastName())
                .email(from.getEmail())
                .build();
    }
}
