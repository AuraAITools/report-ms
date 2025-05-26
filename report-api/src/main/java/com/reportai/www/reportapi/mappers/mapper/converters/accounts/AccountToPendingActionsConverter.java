package com.reportai.www.reportapi.mappers.mapper.converters.accounts;

import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.services.accounts.TenantAwareAccountsService;
import java.util.List;
import java.util.Objects;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class AccountToPendingActionsConverter implements Converter<Account, List<String>> {
    private final TenantAwareAccountsService tenantAwareAccountsService;

    @Autowired
    public AccountToPendingActionsConverter(@Lazy TenantAwareAccountsService tenantAwareAccountsService) {
        this.tenantAwareAccountsService = tenantAwareAccountsService;
    }

    @Override
    public List<String> convert(MappingContext<Account, List<String>> mappingContext) {
        Objects.requireNonNull(mappingContext.getSource().getId(), "account id cannot be null");
        return tenantAwareAccountsService.getPendingActionsOfTenantAwareAccount(mappingContext.getSource().getId());
    }
}
