package com.reportai.www.reportapi.config.converters;

import com.reportai.www.reportapi.dtos.auth.KeycloakUserPrincipal;
import com.reportai.www.reportapi.repositories.OutletRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Converts a jwt token to an AuraAuthenticationToken
 */
public class KeycloakUserPrincipalConverter implements Converter<Jwt, AuraAuthenticationToken> {
    private final String CLIENT_ID = "aura-application-client";
    private final OutletRepository outletRepository;

    public KeycloakUserPrincipalConverter(OutletRepository outletRepository) {
        this.outletRepository = outletRepository;
    }

    @Override
    public AuraAuthenticationToken convert(Jwt jwt) {
        KeycloakUserPrincipal keycloakUserPrincipal = KeycloakUserPrincipal.from(jwt, CLIENT_ID);
        return new AuraAuthenticationToken(keycloakUserPrincipal, jwt, outletRepository);
    }
}
