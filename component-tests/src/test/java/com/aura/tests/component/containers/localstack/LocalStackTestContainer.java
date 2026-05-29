package com.aura.tests.component.containers.localstack;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

@Slf4j
public class LocalStackTestContainer {

    public static final String BUCKET_NAME = "aura-test";
    public static final String ACCESS_KEY = "test";
    public static final String SECRET_KEY = "test";

    private static LocalStackContainer container;

    private LocalStackTestContainer() {
    }

    public static LocalStackContainer getSingleton() {
        if (container == null) {
            container = new LocalStackContainer(DockerImageName.parse("localstack/localstack:3.8"))
                    .withServices(LocalStackContainer.Service.S3);
            log.info("Starting LocalStack container");
            container.start();
        }
        return container;
    }

    public static String getEndpoint() {
        return getSingleton().getEndpoint().toString();
    }

    public static String getRegion() {
        return getSingleton().getRegion();
    }
}
