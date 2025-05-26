package com.reportai.www.reportapi.mappers.mapper.converters.accounts;

import com.reportai.www.reportapi.api.v1.accounts.responses.ExpandedAccountResponse;
import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.mappers.mapper.ModelMapperConfigurer;
import java.util.List;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountToExpandedAccountResponseConverter implements Converter<Account, ExpandedAccountResponse>, ModelMapperConfigurer {

    private final AccountToStudentClientSubaccounResponseDTOConverter accountToStudentClientSubaccounResponseDTOConverter;

    private final AccountToDisplayRolesConverter accountToDisplayRolesConverter;

    private final AccountToEducatorClientSubaccountResponseDTOConverter accountToEducatorClientSubaccountResponseDTOConverter;

    private final AccountToPendingActionsConverter accountToPendingActionsConverter;

    @Autowired
    public AccountToExpandedAccountResponseConverter(AccountToStudentClientSubaccounResponseDTOConverter accountToStudentClientSubaccounResponseDTOConverter, AccountToDisplayRolesConverter accountToDisplayRolesConverter, AccountToEducatorClientSubaccountResponseDTOConverter accountToEducatorClientSubaccountResponseDTOConverter, AccountToPendingActionsConverter accountToPendingActionsConverter) {
        this.accountToStudentClientSubaccounResponseDTOConverter = accountToStudentClientSubaccounResponseDTOConverter;
        this.accountToDisplayRolesConverter = accountToDisplayRolesConverter;
        this.accountToEducatorClientSubaccountResponseDTOConverter = accountToEducatorClientSubaccountResponseDTOConverter;
        this.accountToPendingActionsConverter = accountToPendingActionsConverter;
    }

    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper.addConverter(this);
    }

    @Override
    public ExpandedAccountResponse convert(MappingContext<Account, ExpandedAccountResponse> mappingContext) {
        return ExpandedAccountResponse
                .builder()
                .id(mappingContext.getSource().getId().toString())
                .firstName(mappingContext.getSource().getFirstName())
                .lastName(mappingContext.getSource().getLastName())
                .contact(mappingContext.getSource().getContact())
                .email(mappingContext.getSource().getEmail())
                .studentClientSubaccount(
                        accountToStudentClientSubaccounResponseDTOConverter.convert(mappingContext.create(mappingContext.getSource(), ExpandedAccountResponse.StudentClientSubaccountResponseDTO.class))
                )
                .displayRoles(accountToDisplayRolesConverter.convert(mappingContext.create(mappingContext.getSource(), new TypeToken<List<String>>() {
                }.getType())))
                .educatorClientSubaccount(
                        accountToEducatorClientSubaccountResponseDTOConverter.convert(mappingContext.create(mappingContext.getSource(), ExpandedAccountResponse.EducatorClientSubaccountResponseDTO.class))
                )
                .pendingAccountActions(accountToPendingActionsConverter.convert(mappingContext.create(mappingContext.getSource(), new TypeToken<List<String>>() {
                }.getType())))
                .build();
    }
}