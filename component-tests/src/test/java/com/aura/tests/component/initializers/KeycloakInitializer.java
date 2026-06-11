package com.aura.tests.component.initializers;

import com.aura.tests.component.containers.keycloak.KeycloakTestContainer;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.jspecify.annotations.NonNull;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;

// TODO: extract variables to a separate properties
public class KeycloakInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
        KeycloakContainer keycloakContainer = KeycloakTestContainer.getSingleton();
        String keycloakServerUrl = keycloakContainer.getAuthServerUrl();
        String realm = "aura";
        String jwkSetUri = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/certs";
        String clientId = "aura-application-client";
        String clientSecret = getClientSecret(keycloakContainer, realm, clientId);
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "oauth2.security.resource-server.jwk-set-uri=" + jwkSetUri,
                "oauth2.security.resource-server.enabled=true",
                "keycloak.client.serverUrl=" + keycloakServerUrl,
                "keycloak.client.realm=aura",
                "keycloak.client.clientId=" + clientId,
                "keycloak.client.clientSecret=" + clientSecret
        );
    }

    private String getClientSecret(KeycloakContainer container, String realm, String clientId) {
        runDiagnostics(container, realm);
        // Create Keycloak admin client
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(container.getAuthServerUrl())
                .realm("master")
                .username(container.getAdminUsername())
                .password(container.getAdminPassword())
                .clientId("admin-cli")
                .build();

        try {
            // Get the client by clientId
            ClientRepresentation client = keycloak.realm(realm)
                    .clients()
                    .findByClientId(clientId)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Client not found: " + clientId));

            // Get the client secret
            CredentialRepresentation credential = keycloak.realm(realm)
                    .clients()
                    .get(client.getId())
                    .getSecret();

            return credential.getValue();
        } catch (RuntimeException e) {
            System.out.println("=== KC-DIAG: getClientSecret failed: " + e);
            String logs = container.getLogs();
            System.out.println("=== KC-DIAG: last container log lines ===");
            String[] lines = logs.split("\n");
            for (int i = Math.max(0, lines.length - 60); i < lines.length; i++) {
                System.out.println("KC-LOG| " + lines[i]);
            }
            throw e;
        } finally {
            keycloak.close();
        }
    }

    // TODO: temporary diagnostics for the 403-at-context-load failure — remove once root-caused
    private void runDiagnostics(KeycloakContainer container, String realm) {
        String base = container.getAuthServerUrl();
        System.out.println("=== KC-DIAG: authServerUrl=" + base
                + " adminUser=" + container.getAdminUsername());
        probe("GET /realms/master", base + "/realms/master", null);
        probe("GET /realms/" + realm, base + "/realms/" + realm, null);
        String tokenBody = "grant_type=password&client_id=admin-cli&username="
                + container.getAdminUsername() + "&password=" + container.getAdminPassword();
        probe("POST master token", base + "/realms/master/protocol/openid-connect/token", tokenBody);
    }

    private void probe(String label, String url, String formBody) {
        try {
            java.net.http.HttpClient http = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest.Builder req = java.net.http.HttpRequest.newBuilder(java.net.URI.create(url));
            if (formBody != null) {
                req.header("Content-Type", "application/x-www-form-urlencoded")
                        .POST(java.net.http.HttpRequest.BodyPublishers.ofString(formBody));
            }
            java.net.http.HttpResponse<String> resp =
                    http.send(req.build(), java.net.http.HttpResponse.BodyHandlers.ofString());
            String body = resp.body() == null ? "" : resp.body();
            System.out.println("=== KC-DIAG: " + label + " -> HTTP " + resp.statusCode()
                    + " body[0..300]=" + body.substring(0, Math.min(300, body.length())));
        } catch (Exception e) {
            System.out.println("=== KC-DIAG: " + label + " -> EXCEPTION " + e);
        }
    }
}
