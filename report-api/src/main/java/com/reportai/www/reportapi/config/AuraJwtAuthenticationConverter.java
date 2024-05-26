package com.reportai.www.reportapi.config;

import com.reportai.www.reportapi.dtos.responses.AccountDTO;
import com.reportai.www.reportapi.dtos.responses.AccountDTO.ACCOUNT_TYPES;
import com.reportai.www.reportapi.exceptions.NotFoundException;
import com.reportai.www.reportapi.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class AuraJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final Logger log = LoggerFactory.getLogger(AuraJwtAuthenticationConverter.class);

    private final AccountService accountService;

    private final Map<ACCOUNT_TYPES, String> accountTypeStringToRoleStringMap = Map.ofEntries(
            Map.entry(ACCOUNT_TYPES.EDUCATOR, "ROLE_EDU"),
            Map.entry(ACCOUNT_TYPES.INSTITUTION, "ROLE_INST"),
            Map.entry(ACCOUNT_TYPES.PARENT, "ROLE_PAR"),
            Map.entry(ACCOUNT_TYPES.STUDENT, "ROLE_STU")
    );

    public AuraJwtAuthenticationConverter(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        return new JwtAuthenticationToken(source, extractResourceRoles(source));
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt)
    {
        UUID userId;
        List<SimpleGrantedAuthority> resourceRoles = new ArrayList<>();

        try {
            userId = UUID.fromString(jwt.getSubject());
            List<AccountDTO> accounts = accountService.getAllAccountsOfUser(userId);
            accounts.forEach(accountDTO -> {
                if (accountTypeStringToRoleStringMap.containsKey(accountDTO.accountType)) {
                    String role = accountTypeStringToRoleStringMap.get(accountDTO.accountType);
                    resourceRoles.add(new SimpleGrantedAuthority(role));
                }
            });
        }catch (IllegalArgumentException ie) {
            // TODO: define more granular exception classes later on
            throw new NotFoundException("invalid user id");
        }catch (Exception e){
            throw new NotFoundException("something bad happened");
        }

        log.info("user {} has roles [{}]",userId, resourceRoles
                .stream()
                .map(SimpleGrantedAuthority::getAuthority).collect(Collectors.joining(",")));
        return resourceRoles;
    }
}
