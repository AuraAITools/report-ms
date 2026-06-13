package com.aura.app.architecture;

import com.aura.core.authorization.PermissionRegistry;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Guards against drift between {@link PermissionRegistry} and the SpiceDB schema. A permission
 * listed in the registry but absent from the schema fails at runtime with FAILED_PRECONDITION,
 * which surfaces as a 500 on lookupPermissions/lookupGroupPermissions.
 */
@DisplayName("PermissionRegistry must stay in sync with the SpiceDB schema")
class PermissionRegistrySchemaSyncTest {

    private static final Pattern DEFINITION_PATTERN = Pattern.compile("^definition\\s+(\\w+)\\s*\\{");
    private static final Pattern PERMISSION_PATTERN = Pattern.compile("^\\s*permission\\s+(\\w+)");

    private static final Map<String, Set<String>> SCHEMA_PERMISSIONS = parseSchemaPermissions();

    @TestFactory
    @DisplayName("Should define every registry resource permission in the schema")
    Stream<DynamicTest> resourcePermissionsExistInSchema() {
        return registryEntries(PermissionRegistry.RESOURCE_PERMISSIONS);
    }

    @TestFactory
    @DisplayName("Should define every registry user_group permission in the schema")
    Stream<DynamicTest> userGroupPermissionsExistInSchema() {
        return registryEntries(PermissionRegistry.USER_GROUP_PERMISSIONS);
    }

    private Stream<DynamicTest> registryEntries(Map<String, List<String>> registry) {
        return registry.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(permission -> DynamicTest.dynamicTest(
                                entry.getKey() + "#" + permission,
                                () -> {
                                    assertThat(SCHEMA_PERMISSIONS)
                                            .as("definition '%s' is missing from the SpiceDB schema", entry.getKey())
                                            .containsKey(entry.getKey());
                                    assertThat(SCHEMA_PERMISSIONS.get(entry.getKey()))
                                            .as("permission '%s' is not defined on '%s' in the SpiceDB schema",
                                                    permission, entry.getKey())
                                            .contains(permission);
                                })));
    }

    private static Map<String, Set<String>> parseSchemaPermissions() {
        Map<String, Set<String>> permissionsByDefinition = new HashMap<>();
        try (Stream<Path> files = Files.list(findSchemaRoot())) {
            files.filter(f -> f.getFileName().toString().endsWith(".zed"))
                    .forEach(f -> parseFile(f, permissionsByDefinition));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read SpiceDB schema files", e);
        }
        return permissionsByDefinition;
    }

    private static void parseFile(Path file, Map<String, Set<String>> permissionsByDefinition) {
        String currentDefinition = null;
        try {
            for (String line : Files.readAllLines(file)) {
                Matcher definition = DEFINITION_PATTERN.matcher(line.strip());
                if (definition.find()) {
                    currentDefinition = definition.group(1);
                    permissionsByDefinition.computeIfAbsent(currentDefinition, k -> new HashSet<>());
                    continue;
                }
                Matcher permission = PERMISSION_PATTERN.matcher(line);
                if (permission.find() && currentDefinition != null) {
                    permissionsByDefinition.get(currentDefinition).add(permission.group(1));
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read schema file " + file, e);
        }
    }

    private static Path findSchemaRoot() {
        Path candidate = Paths.get("application/src/main/resources/spicedb");
        if (Files.isDirectory(candidate)) return candidate;
        candidate = Paths.get("src/main/resources/spicedb");
        if (Files.isDirectory(candidate)) return candidate;
        throw new IllegalStateException("Cannot find SpiceDB schema directory");
    }
}
