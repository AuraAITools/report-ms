package com.aura.tests.component.containers.keycloak;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import lombok.extern.slf4j.Slf4j;

// TODO: extract variables to a separate properties

@Slf4j
public class KeycloakTestContainer {

    private static KeycloakContainer keycloak;

    private KeycloakTestContainer() {
    }

    public static KeycloakContainer getSingleton() {
        if (keycloak == null) {
            keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:25.0")
                    .withAdminUsername("admin")
                    .withAdminPassword("admin")
                    .withRealmImportFile("/keycloak/realm-import.json");
            log.info("Running keycloak");
            keycloak.start();
        }
        return keycloak;
    }
}
