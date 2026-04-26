package com.aura.tests.component.initializers;

import com.authzed.api.v1.SchemaServiceGrpc;
import com.authzed.api.v1.WriteSchemaRequest;
import com.authzed.grpcutil.BearerToken;
import com.aura.tests.component.containers.spicedb.SpiceDbTestContainer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.support.TestPropertySourceUtils;

@Slf4j
public class SpiceDbInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
        String target = SpiceDbTestContainer.getTarget();
        String presharedKey = SpiceDbTestContainer.getPresharedKey();

        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spicedb.target=" + target,
                "spicedb.preshared-key=" + presharedKey,
                "spicedb.plaintext=true"
        );

        writeSchema(target, presharedKey);
        log.info("SpiceDB initialized at {}", target);
    }

    private void writeSchema(String target, String presharedKey) {
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
        try {
            SchemaServiceGrpc.SchemaServiceBlockingStub schemaService =
                    SchemaServiceGrpc.newBlockingStub(channel)
                            .withCallCredentials(new BearerToken(presharedKey));

            String schema = loadSchemaFromClasspath();
            schemaService.writeSchema(WriteSchemaRequest.newBuilder().setSchema(schema).build());
            log.info("SpiceDB schema written successfully");
        } finally {
            channel.shutdownNow();
        }
    }

    private String loadSchemaFromClasspath() {
        try {
            return new ClassPathResource("spicedb/schema.zed")
                    .getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load SpiceDB schema", e);
        }
    }
}
