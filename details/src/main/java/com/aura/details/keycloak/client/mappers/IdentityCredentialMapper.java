package com.aura.details.keycloak.client.mappers;

import com.aura.core.clients.IdentityCredential;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IdentityCredentialMapper {
    IdentityCredentialMapper INSTANCE = Mappers.getMapper(IdentityCredentialMapper.class);

    @Mapping(target = "isTemporary", source = "temporary")
    IdentityCredential toIdentityCredential(CredentialRepresentation credential);
}
