package com.aura.details.keycloak.client.mappers;

import com.aura.core.clients.UserIdentity;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {IdentityCredentialMapper.class})
public interface UserIdentityMapper {
    UserIdentityMapper INSTANCE = Mappers.getMapper(UserIdentityMapper.class);

    @Mapping(target = "roles", source = "clientRoles")
    UserIdentity toUserIdentity(UserRepresentation userRepresentation); // TODO: find a way to map clientRoles to roles

    @Mapping(target = "userProfileMetadata", ignore = true)
    @Mapping(target = "totp", ignore = true)
    @Mapping(target = "socialLinks", ignore = true)
    @Mapping(target = "serviceAccountClientId", ignore = true)
    @Mapping(target = "self", ignore = true)
    @Mapping(target = "realmRoles", ignore = true)
    @Mapping(target = "rawAttributes", ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "notBefore", ignore = true)
    @Mapping(target = "groups", ignore = true)
    @Mapping(target = "federationLink", ignore = true)
    @Mapping(target = "federatedIdentities", ignore = true)
    @Mapping(target = "disableableCredentialTypes", ignore = true)
    @Mapping(target = "clientRoles", ignore = true)
    @Mapping(target = "clientConsents", ignore = true)
    @Mapping(target = "applicationRoles", ignore = true)
    @Mapping(target = "access", ignore = true)
    UserRepresentation toUserRepresentation(UserIdentity userIdentity);

}
