package com.reportai.www.reportapi.mappers.mapper.converters.accounts;

import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.mappers.mapper.ModelMapperConfigurer;
import com.reportai.www.reportapi.services.outlets.OutletsService;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class AccountToDisplayRolesConverter implements Converter<Account, List<String>>, ModelMapperConfigurer {
    private final UsersResource usersResource;
    private final ClientRepresentation clientRepresentation;

    private final OutletsService outletsService;

    @Autowired
    public AccountToDisplayRolesConverter(UsersResource usersResource, ClientRepresentation clientRepresentation, @Lazy OutletsService outletsService) {
        this.usersResource = usersResource;
        this.clientRepresentation = clientRepresentation;
        this.outletsService = outletsService;
    }

    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper.addConverter(this);
    }

    @Override
    public List<String> convert(MappingContext<Account, List<String>> mappingContext) {
        Account account = mappingContext.getSource();
        Objects.requireNonNull(account.getUserId(), "Account userId cannot be null");
        return usersResource
                .get(account.getUserId())
                .roles()
                .clientLevel(clientRepresentation.getId())
                .listEffective()
                .stream()
                .map(RoleRepresentation::getName)
                .map(this::convertToDisplayableRoles)
                .toList();

    }

    private String convertToDisplayableRoles(String role) {
        List<String> splits = Arrays.stream(role.split("_")).toList();
        if ("institution-admin".equals(splits.getLast())) {
            return "institution admin";
        }
        if ("educator-report-mobile".equals(splits.getLast())) {
            return "educator";
        }
        if ("student-report-mobile".equals(splits.getLast())) {
            return "student client";
        }
        // outlet admin
        UUID outletId = UUID.fromString(splits.get(1));
        Outlet outlet = outletsService.findById(outletId);
        return String.format("%s outlet admin", outlet.getName());
    }
}
