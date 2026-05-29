package com.aura.details.architecture;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;


import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies Lombok annotation conventions on @Entity model classes by inspecting source files.
 *
 * <p>Lombok annotations have @Retention(SOURCE) so ArchUnit cannot see them at runtime.
 * This test reads the .java source files directly to verify conventions:
 * <ul>
 *   <li>All @Entity classes must have @Getter</li>
 *   <li>All @Entity classes must have @Setter</li>
 *   <li>All @Entity classes must have @EqualsAndHashCode</li>
 *   <li>All lazy relationship fields must have @ToString.Exclude</li>
 * </ul>
 */
@DisplayName("Model entity source conventions")
class ModelEntitySourceConventionTest {

    private static final Path MODELS_ROOT = findModelsRoot();

    @DisplayName("Entity classes should have @Getter")
    @TestFactory
    Stream<DynamicTest> entity_classes_should_have_getter() throws IOException {
        return entitySourceFiles().map(path -> DynamicTest.dynamicTest(
                path.getFileName().toString() + " should have @Getter",
                () -> assertThat(Files.readString(path))
                        .as("@Entity class %s must have @Getter", path.getFileName())
                        .contains("@Getter")
        ));
    }

    @DisplayName("Entity classes should have @Setter")
    @TestFactory
    Stream<DynamicTest> entity_classes_should_have_setter() throws IOException {
        return entitySourceFiles().map(path -> DynamicTest.dynamicTest(
                path.getFileName().toString() + " should have @Setter",
                () -> assertThat(Files.readString(path))
                        .as("@Entity class %s must have @Setter", path.getFileName())
                        .contains("@Setter")
        ));
    }

    @DisplayName("Entity classes should have @EqualsAndHashCode")
    @TestFactory
    Stream<DynamicTest> entity_classes_should_have_equals_and_hashcode() throws IOException {
        return entitySourceFiles().map(path -> DynamicTest.dynamicTest(
                path.getFileName().toString() + " should have @EqualsAndHashCode",
                () -> assertThat(Files.readString(path))
                        .as("@Entity class %s must have @EqualsAndHashCode", path.getFileName())
                        .contains("@EqualsAndHashCode")
        ));
    }

    @DisplayName("Lazy relationship fields should have @ToString.Exclude")
    @TestFactory
    Stream<DynamicTest> lazy_relationship_fields_should_have_tostring_exclude() throws IOException {
        return entitySourceFiles()
                .filter(path -> {
                    try {
                        String content = Files.readString(path);
                        return content.contains("FetchType.LAZY")
                                || content.contains("@OneToMany")
                                || content.contains("@ManyToMany");
                    } catch (IOException e) {
                        return false;
                    }
                })
                .map(path -> DynamicTest.dynamicTest(
                        path.getFileName().toString() + " lazy fields should have @ToString.Exclude",
                        () -> {
                            List<String> lines = Files.readAllLines(path);
                            for (int i = 0; i < lines.size(); i++) {
                                String line = lines.get(i).trim();
                                if (isLazyRelationshipAnnotation(line)) {
                                    // Look ahead for @ToString.Exclude before the field declaration (private/protected line)
                                    boolean foundExclude = false;
                                    for (int j = i; j < Math.min(i + 5, lines.size()); j++) {
                                        String lookAhead = lines.get(j).trim();
                                        if (lookAhead.contains("@ToString.Exclude")) {
                                            foundExclude = true;
                                            break;
                                        }
                                        if (lookAhead.startsWith("private ") || lookAhead.startsWith("protected ")) {
                                            break;
                                        }
                                    }
                                    assertThat(foundExclude)
                                            .as("Lazy field near line %d in %s must have @ToString.Exclude", i + 1, path.getFileName())
                                            .isTrue();
                                }
                            }
                        }
                ));
    }

    private static boolean isLazyRelationshipAnnotation(String line) {
        // @OneToMany and @ManyToMany are LAZY by default
        if (line.startsWith("@OneToMany") || line.startsWith("@ManyToMany")) {
            return !line.contains("FetchType.EAGER");
        }
        // @ManyToOne and @OneToOne are EAGER by default — only lazy if explicitly set
        if (line.startsWith("@ManyToOne") || line.startsWith("@OneToOne")) {
            return line.contains("FetchType.LAZY");
        }
        return false;
    }

    private Stream<Path> entitySourceFiles() throws IOException {
        return Files.walk(MODELS_ROOT)
                .filter(p -> p.toString().endsWith(".java"))
                .filter(p -> !p.toString().contains("/base/"))
                .filter(p -> !p.getFileName().toString().equals("package-info.java"))
                .filter(p -> {
                    try {
                        return Files.readString(p).contains("@Entity");
                    } catch (IOException e) {
                        return false;
                    }
                });
    }

    private static Path findModelsRoot() {
        // Try relative to project root (typical Maven test execution)
        Path candidate = Paths.get("details/src/main/java/com/aura/details/springdata/models");
        if (Files.isDirectory(candidate)) {
            return candidate;
        }
        // Try from details module root
        candidate = Paths.get("src/main/java/com/aura/details/springdata/models");
        if (Files.isDirectory(candidate)) {
            return candidate;
        }
        throw new IllegalStateException("Cannot find models source directory");
    }
}
