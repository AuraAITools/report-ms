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
            applyTestRealmSettings(keycloak);
        }
        return keycloak;
    }

    // sslRequired=external rejects host->container traffic with 403 "HTTPS required" when the
    // Docker gateway IP isn't in Keycloak's private-IP list (OrbStack/Colima/Docker Desktop on
    // macOS). kcadm runs as localhost inside the container, so it is exempt from the check.
    //
    // accessTokenLifespan is extended because step defs bake a token into the client's headers
    // at identity-switch time; the default 60s lifespan expires mid-scenario in slow scenarios
    // (multiple account creations), causing flaky 401s.
    private static void applyTestRealmSettings(KeycloakContainer container) {
        try {
            execKcadm(container, "config", "credentials", "--server", "http://localhost:8080",
                    "--realm", "master", "--user", container.getAdminUsername(),
                    "--password", container.getAdminPassword());
            execKcadm(container, "update", "realms/master", "-s", "sslRequired=NONE",
                    "-s", "accessTokenLifespan=1800");
            execKcadm(container, "update", "realms/aura", "-s", "sslRequired=NONE",
                    "-s", "accessTokenLifespan=1800");
        } catch (Exception e) {
            throw new IllegalStateException("Failed to apply Keycloak test realm settings", e);
        }
    }

    private static void execKcadm(KeycloakContainer container, String... args) throws Exception {
        String[] command = new String[args.length + 1];
        command[0] = "/opt/keycloak/bin/kcadm.sh";
        System.arraycopy(args, 0, command, 1, args.length);
        var result = container.execInContainer(command);
        if (result.getExitCode() != 0) {
            throw new IllegalStateException("kcadm failed (exit " + result.getExitCode() + "): "
                    + result.getStdout() + result.getStderr());
        }
    }
}
