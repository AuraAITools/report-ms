package com.aura.tests.component.containers.spicedb;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@Slf4j
public class SpiceDbTestContainer {

    private static GenericContainer<?> container;

    private SpiceDbTestContainer() {
    }

    public static GenericContainer<?> getSingleton() {
        if (container == null) {
            container = new GenericContainer<>("authzed/spicedb:latest")
                    .withCommand("serve", "--grpc-preshared-key", "test-key", "--datastore-engine", "memory")
                    .withExposedPorts(50051)
                    .waitingFor(Wait.forLogMessage(".*grpc server started serving.*", 1));
            log.info("Starting SpiceDB container");
            container.start();
        }
        return container;
    }

    public static String getTarget() {
        var c = getSingleton();
        return c.getHost() + ":" + c.getMappedPort(50051);
    }

    public static String getPresharedKey() {
        return "test-key";
    }
}
