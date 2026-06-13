package com.aura.app.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;


import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Enforces @PreAuthorize conventions on GraphQL controller methods.
 *
 * <p>ArchUnit tests (runtime bytecode):
 * <ul>
 *   <li>Every @QueryMapping must have @PreAuthorize</li>
 *   <li>Every @MutationMapping must have @PreAuthorize</li>
 * </ul>
 *
 * <p>Source-level tests (parse .java files):
 * <ul>
 *   <li>@PreAuthorize must use hasPermission()</li>
 *   <li>hasPermission() must have exactly 3 arguments</li>
 *   <li>Resource names must be from a known set</li>
 *   <li>Scope names must be from a known set</li>
 *   <li>#paramName references must match method parameter names</li>
 * </ul>
 */
@AnalyzeClasses(packages = "com.aura.app.controllers")
@DisplayName("PreAuthorize convention enforcement")
class PreAuthorizeConventionTest {

    private static final Set<String> VALID_RESOURCES = Set.of(
            "institution", "outlet", "course", "lesson",
            "account", "material", "system"
    );

    // Must stay in sync with the permissions actually defined in src/main/resources/spicedb/*.zed —
    // a scope listed here but absent from the schema fails at runtime with FAILED_PRECONDITION (500).
    private static final Set<String> VALID_SCOPES = Set.of(
            "view", "update", "delete",
            "create_institution", "create_outlet", "create_course",
            "create_lesson", "create_account", "create_subject", "create_topic",
            "create_lesson_plan", "create_outlet_room", "create_lesson_objective",
            "create_lesson_quiz",
            "create_material", "create_level", "create_school", "delete_school",
            "view_level", "update_level", "delete_level",
            "view_subject", "update_subject", "delete_subject",
            "view_topic", "update_topic", "delete_topic",
            "view_outlet_room", "update_outlet_room", "delete_outlet_room"
    );

    private static final Path CONTROLLERS_ROOT = findControllersRoot();

    private static final Pattern PREAUTHORIZE_PATTERN = Pattern.compile(
            "@PreAuthorize\\(\"(.+?)\"\\)");

    private static final Pattern HAS_PERMISSION_PATTERN = Pattern.compile(
            "hasPermission\\((.+?)\\)");

    private static final Pattern ARGUMENT_PATTERN = Pattern.compile(
            "@Argument\\((?:name\\s*=\\s*|value\\s*=\\s*)?\"([^\"]+)\"\\)");

    private static final Pattern JAVA_PARAM_PATTERN = Pattern.compile(
            "\\w+\\s+(\\w+)\\s*[,)]");

    // ===================== ArchUnit bytecode tests =====================

    @ArchTest
    static final ArchRule query_mappings_must_have_preauthorize =
            methods().that().areAnnotatedWith(QueryMapping.class)
                    .and().areDeclaredInClassesThat().doNotHaveSimpleName("PermissionGqlController")
                    .and().areDeclaredInClassesThat().doNotHaveSimpleName("InstitutionCreationRequestGqlController")
                    .should().beAnnotatedWith(PreAuthorize.class)
                    .as("All @QueryMapping methods must have @PreAuthorize (except PermissionGqlController, InstitutionCreationRequestGqlController)");

    @ArchTest
    static final ArchRule mutation_mappings_must_have_preauthorize =
            methods().that().areAnnotatedWith(MutationMapping.class)
                    .and().areDeclaredInClassesThat().doNotHaveSimpleName("PermissionGqlController")
                    .and().areDeclaredInClassesThat().doNotHaveSimpleName("InstitutionCreationRequestGqlController")
                    .should().beAnnotatedWith(PreAuthorize.class)
                    .as("All @MutationMapping methods must have @PreAuthorize (except PermissionGqlController, InstitutionCreationRequestGqlController)");

    // ===================== Source-level convention tests =====================

    @TestFactory
    @DisplayName("@PreAuthorize must use hasPermission()")
    Stream<DynamicTest> preauthorize_must_use_hasPermission() throws IOException {
        return findPreAuthorizeAnnotations().stream()
                .map(match -> DynamicTest.dynamicTest(
                        match.file().getFileName() + ":" + match.lineNumber() + " must use hasPermission()",
                        () -> assertThat(match.value())
                                .as("@PreAuthorize at %s:%d must call hasPermission()", match.file().getFileName(), match.lineNumber())
                                .contains("hasPermission(")
                ));
    }

    @TestFactory
    @DisplayName("hasPermission() must have exactly 3 arguments")
    Stream<DynamicTest> hasPermission_must_have_three_arguments() throws IOException {
        return findPreAuthorizeAnnotations().stream()
                .filter(match -> match.value().contains("hasPermission("))
                .map(match -> DynamicTest.dynamicTest(
                        match.file().getFileName() + ":" + match.lineNumber() + " hasPermission must have 3 args",
                        () -> {
                            Matcher m = HAS_PERMISSION_PATTERN.matcher(match.value());
                            assertThat(m.find())
                                    .as("Could not parse hasPermission() at %s:%d", match.file().getFileName(), match.lineNumber())
                                    .isTrue();
                            String argsStr = m.group(1);
                            String[] args = argsStr.split(",(?=(?:[^']*'[^']*')*[^']*$)");
                            assertThat(args)
                                    .as("hasPermission() at %s:%d must have exactly 3 arguments, found: %s",
                                            match.file().getFileName(), match.lineNumber(), argsStr)
                                    .hasSize(3);
                        }
                ));
    }

    @TestFactory
    @DisplayName("Resource names must be from the valid set")
    Stream<DynamicTest> resource_names_must_be_valid() throws IOException {
        return findPreAuthorizeAnnotations().stream()
                .filter(match -> match.value().contains("hasPermission("))
                .map(match -> DynamicTest.dynamicTest(
                        match.file().getFileName() + ":" + match.lineNumber() + " resource name must be valid",
                        () -> {
                            String resource = extractArgument(match.value(), 1);
                            assertThat(resource).as("Could not extract resource name at %s:%d",
                                    match.file().getFileName(), match.lineNumber()).isNotNull();
                            assertThat(VALID_RESOURCES)
                                    .as("Invalid resource '%s' at %s:%d. Valid resources: %s",
                                            resource, match.file().getFileName(), match.lineNumber(), VALID_RESOURCES)
                                    .contains(resource);
                        }
                ));
    }

    @TestFactory
    @DisplayName("Scope names must be from the valid set")
    Stream<DynamicTest> scope_names_must_be_valid() throws IOException {
        return findPreAuthorizeAnnotations().stream()
                .filter(match -> match.value().contains("hasPermission("))
                .map(match -> DynamicTest.dynamicTest(
                        match.file().getFileName() + ":" + match.lineNumber() + " scope name must be valid",
                        () -> {
                            String scope = extractArgument(match.value(), 2);
                            assertThat(scope).as("Could not extract scope name at %s:%d",
                                    match.file().getFileName(), match.lineNumber()).isNotNull();
                            assertThat(VALID_SCOPES)
                                    .as("Invalid scope '%s' at %s:%d. Valid scopes: %s",
                                            scope, match.file().getFileName(), match.lineNumber(), VALID_SCOPES)
                                    .contains(scope);
                        }
                ));
    }

    @TestFactory
    @DisplayName("#paramName references must match method parameters")
    Stream<DynamicTest> param_references_must_match_method_parameters() throws IOException {
        return findPreAuthorizeAnnotations().stream()
                .filter(match -> match.value().contains("#"))
                .map(match -> DynamicTest.dynamicTest(
                        match.file().getFileName() + ":" + match.lineNumber() + " #param must match method parameter",
                        () -> {
                            String firstArg = extractRawArgument(match.value(), 0);
                            assertThat(firstArg).as("Could not extract first argument").isNotNull();
                            assertThat(firstArg).startsWith("#");

                            // Extract root variable name (before dot for SpEL property access)
                            String fullRef = firstArg.substring(1);
                            String rootParamName = fullRef.contains(".")
                                    ? fullRef.substring(0, fullRef.indexOf('.'))
                                    : fullRef;

                            List<String> fileLines = Files.readAllLines(match.file());

                            // Collect both @Argument names and Java parameter names from method signature
                            Set<String> parameterNames = new java.util.HashSet<>();
                            for (int i = match.lineNumber(); i < Math.min(match.lineNumber() + 5, fileLines.size()); i++) {
                                // Match @Argument("name") values
                                Matcher argMatcher = ARGUMENT_PATTERN.matcher(fileLines.get(i));
                                while (argMatcher.find()) {
                                    parameterNames.add(argMatcher.group(1));
                                }
                                // Match Java parameter names (type paramName or type paramName,)
                                Matcher javaParamMatcher = JAVA_PARAM_PATTERN.matcher(fileLines.get(i));
                                while (javaParamMatcher.find()) {
                                    parameterNames.add(javaParamMatcher.group(1));
                                }
                            }

                            assertThat(parameterNames)
                                    .as("#%s at %s:%d must match a method parameter. Found: %s",
                                            rootParamName, match.file().getFileName(), match.lineNumber(), parameterNames)
                                    .contains(rootParamName);
                        }
                ));
    }

    // ===================== Helpers =====================

    private String extractArgument(String preAuthorizeValue, int index) {
        String raw = extractRawArgument(preAuthorizeValue, index);
        if (raw == null) return null;
        if (raw.startsWith("'") && raw.endsWith("'")) {
            return raw.substring(1, raw.length() - 1);
        }
        return raw;
    }

    private String extractRawArgument(String preAuthorizeValue, int index) {
        Matcher m = HAS_PERMISSION_PATTERN.matcher(preAuthorizeValue);
        if (!m.find()) return null;
        String argsStr = m.group(1);
        String[] args = argsStr.split(",(?=(?:[^']*'[^']*')*[^']*$)");
        if (index >= args.length) return null;
        return args[index].trim();
    }

    record PreAuthorizeMatch(Path file, int lineNumber, String value) {
    }

    private List<PreAuthorizeMatch> findPreAuthorizeAnnotations() throws IOException {
        List<PreAuthorizeMatch> matches = new ArrayList<>();
        try (Stream<Path> files = Files.walk(CONTROLLERS_ROOT)) {
            files.filter(p -> p.toString().endsWith("GqlController.java"))
                    .filter(p -> !p.getFileName().toString().equals("PermissionGqlController.java"))
                    .filter(p -> !p.getFileName().toString().equals("InstitutionCreationRequestGqlController.java"))
                    .forEach(path -> {
                        try {
                            List<String> lines = Files.readAllLines(path);
                            for (int i = 0; i < lines.size(); i++) {
                                Matcher m = PREAUTHORIZE_PATTERN.matcher(lines.get(i));
                                if (m.find()) {
                                    matches.add(new PreAuthorizeMatch(path, i + 1, m.group(1)));
                                }
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        return matches;
    }

    private static Path findControllersRoot() {
        Path candidate = Paths.get("application/src/main/java/com/aura/app/controllers");
        if (Files.isDirectory(candidate)) return candidate;
        candidate = Paths.get("src/main/java/com/aura/app/controllers");
        if (Files.isDirectory(candidate)) return candidate;
        throw new IllegalStateException("Cannot find controllers source directory");
    }
}
