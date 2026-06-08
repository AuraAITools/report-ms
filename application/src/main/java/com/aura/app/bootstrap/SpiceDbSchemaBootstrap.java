package com.aura.app.bootstrap;

import com.authzed.api.v1.SchemaServiceGrpc;
import com.authzed.api.v1.WriteSchemaRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpiceDbSchemaBootstrap implements ApplicationRunner {

    private final SchemaServiceGrpc.SchemaServiceBlockingStub schemaServiceStub;

    @Override
    public void run(ApplicationArguments args) {
        String schema = loadSchemaFromClasspath();
        schemaServiceStub.writeSchema(WriteSchemaRequest.newBuilder().setSchema(schema).build());
        log.info("SpiceDB schema written ({} chars)", schema.length());
    }

    private String loadSchemaFromClasspath() {
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver()
                    .getResources("classpath*:spicedb/*.zed");
            StringBuilder schema = new StringBuilder();
            Arrays.stream(resources)
                    .sorted(Comparator.comparing(Resource::getFilename))
                    .forEach(resource -> schema.append(stripImports(readResource(resource))).append('\n'));
            return schema.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load SpiceDB schema", e);
        }
    }

    private String readResource(Resource resource) {
        try {
            return resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read SpiceDB schema file: " + resource.getFilename(), e);
        }
    }

    // SpiceDB's WriteSchema gRPC API does not honor `use import` / `import '...'` directives — those are zed CLI
    // features that resolve cross-file references for `zed validate`. At runtime we concatenate every .zed file and
    // strip the import scaffolding so the wire schema is a single self-contained document.
    private String stripImports(String content) {
        return content.lines()
                .filter(line -> {
                    String trimmed = line.trim();
                    return !trimmed.startsWith("use import")
                            && !trimmed.startsWith("use typechecking")
                            && !trimmed.startsWith("import ");
                })
                .reduce((a, b) -> a + "\n" + b)
                .orElse("");
    }
}
