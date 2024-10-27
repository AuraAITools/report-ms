package com.reportai.www.reportapi.clients.keycloak;

import com.reportai.www.reportapi.clients.keycloak.exceptions.KeycloakUserAccountAlreadyExistsException;
import com.reportai.www.reportapi.clients.keycloak.exceptions.KeycloakUserAccountCreationException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * keycloak client
 */
@Component
public class KeycloakAuthClient implements AuthClient<UserRepresentation> {

    private final Keycloak keycloakClient;

    private final String REALM = "aura";

    private final RealmResource realmResource;

    private final UsersResource usersResource;

    private Logger log = LoggerFactory.getLogger(KeycloakAuthClient.class);

    @Autowired
    public KeycloakAuthClient(Keycloak keycloakClient) {
        this.keycloakClient = keycloakClient;
        this.realmResource = keycloakClient.realm(REALM);
        this.usersResource = realmResource.users();
    }

    /**
     * Creates a default user, created user requires email verification and password reset to be enabled
     *
     * @param userRepresentation
     * @return the user id
     */
    @Override
    public String createUserAccount(UserRepresentation userRepresentation) throws KeycloakUserAccountAlreadyExistsException, KeycloakUserAccountCreationException {
        Response response = usersResource.create(withDefaultConfigurations(userRepresentation));

        if (response.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
            throw new KeycloakUserAccountAlreadyExistsException("keycloak user account already exists");
        }

        if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
            throw new KeycloakUserAccountCreationException("user account could not be created");
        }

        log.info("successfully created user with userId: {}", CreatedResponseUtil.getCreatedId(response));
        return CreatedResponseUtil.getCreatedId(response);
    }

    /**
     * Sends an email with the pending actions to the user
     *
     * @param userId
     */
    @Override
    public void sendPendingActionsToUserEmail(String userId) {
        usersResource.get(userId).sendVerifyEmail();
    }

    private static CredentialRepresentation defaultCredentials() {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType("password");
        credentialRepresentation.setValue("password");
        credentialRepresentation.setTemporary(true);
        return credentialRepresentation;
    }

    private static UserRepresentation withDefaultConfigurations(UserRepresentation userRepresentation) {
        CredentialRepresentation defaultCredentials = defaultCredentials();
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(false);
        userRepresentation.setRequiredActions(List.of("VERIFY_EMAIL", "UPDATE_PASSWORD"));
        userRepresentation.setCredentials(List.of(defaultCredentials));
        return userRepresentation;
    }
}
