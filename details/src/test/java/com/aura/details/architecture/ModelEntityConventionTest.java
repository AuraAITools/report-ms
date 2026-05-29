package com.aura.details.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.persistence.Entity;
import org.junit.jupiter.api.DisplayName;


import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

/**
 * ArchUnit tests to enforce model entity conventions.
 *
 * <p>Note: Lombok annotations (@Getter, @Setter, @EqualsAndHashCode, @ToString.Exclude)
 * have @Retention(SOURCE) and are not visible to ArchUnit at runtime.
 * Those conventions are enforced by {@link ModelEntitySourceConventionTest} which
 * inspects the source files directly.
 */
@DisplayName("Model entity conventions")
@AnalyzeClasses(packages = "com.aura.details.springdata.models")
public class ModelEntityConventionTest {

    @ArchTest
    static final ArchRule entity_classes_should_end_with_model =
            classes().that().areAnnotatedWith(Entity.class)
                    .should().haveSimpleNameEndingWith("Model")
                    .as("All @Entity classes should be named *Model");

    @ArchTest
    static final ArchRule entity_classes_should_reside_in_models_package =
            classes().that().areAnnotatedWith(Entity.class)
                    .should().resideInAPackage("com.aura.details.springdata.models..")
                    .as("All @Entity classes should reside in the models package");
}
