package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.dtos.requests.CreateAdminAccountDTO;
import com.reportai.www.reportapi.dtos.requests.CreateInstitutionDTO;
import com.reportai.www.reportapi.entities.Institution;
import org.keycloak.representations.idm.UserRepresentation;

public class InstitutionMappers {
    public static Institution convert(CreateInstitutionDTO from) {
        return Institution
                .builder()
                .name(from.getName())
                .email(from.getEmail())
                .build();
    }

    public static UserRepresentation convert(CreateAdminAccountDTO from) {
        UserRepresentation partiallyFilledUser = new UserRepresentation();
        partiallyFilledUser.setEmail(from.getEmail());
        partiallyFilledUser.setLastName(from.getLastName());
        partiallyFilledUser.setFirstName(from.getFirstName());
        return partiallyFilledUser;
    }
}
